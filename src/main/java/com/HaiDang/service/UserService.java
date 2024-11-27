package com.HaiDang.service;

import com.HaiDang.exception.UserException;
import com.HaiDang.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User findUserById(Long userId) throws UserException;
    public User findUserProfileByJwt(String jwt) throws UserException;
}
