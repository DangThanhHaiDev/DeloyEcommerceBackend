package com.HaiDang.service;

import com.HaiDang.config.JwtProvider;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.User;
import com.HaiDang.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceIplm implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtProvider jwtProvider;
    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        throw new UserException("User not found with Id: "+userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("User not found with Email: "+email);
        }
        return user;
    }
}
