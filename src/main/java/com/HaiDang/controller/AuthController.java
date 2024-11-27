package com.HaiDang.controller;

import com.HaiDang.exception.UserException;
import com.HaiDang.model.User;
import com.HaiDang.repository.UserRepository;
import com.HaiDang.request.AuthRequest;
import com.HaiDang.request.LoginRequest;
import com.HaiDang.response.AuthResponse;
import com.HaiDang.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody AuthRequest authRequest) throws UserException {
        return authService.createUserHandler(authRequest);
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest loginRequest) throws UserException {
        return authService.loginUserHandler(loginRequest);
    }
}
