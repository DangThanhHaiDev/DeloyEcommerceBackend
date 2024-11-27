package com.HaiDang.service;

import com.HaiDang.config.JwtProvider;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.User;
import com.HaiDang.repository.UserRepository;
import com.HaiDang.request.AuthRequest;
import com.HaiDang.request.LoginRequest;
import com.HaiDang.response.AuthResponse;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserDetailsServiceImplement userDetailsService;
    @Autowired
    CartService cartService;
    public ResponseEntity<AuthResponse> createUserHandler(AuthRequest authRequest) throws UserException {
        if(userRepository.findByEmail(authRequest.getEmail()) != null){
            throw new UserException("Email already existed with another User");
        }
        String email = authRequest.getEmail();
        String firstName = authRequest.getFirstName();
        String lastName = authRequest.getLastName();
        String password = passwordEncoder.encode(authRequest.getPassword());
        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        userRepository.save(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        String token = jwtProvider.generateToken(authentication);
        String message = "Signup success!";
        cartService.createCart(user);
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .message(message)
                .firstName(user.getFirstName())
                .build();
        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
    }
    public ResponseEntity<AuthResponse> loginUserHandler(LoginRequest loginRequest) throws UserException {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authentication = authenticated(email, password);
        String token = jwtProvider.generateToken(authentication);
        String message = "Signin success!";
        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .message(message)
                .build();
        System.out.println(authResponse);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }
    public Authentication authenticated(String email, String password) throws UserException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new UserException("Invalid password");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        return authentication;
    }
}
