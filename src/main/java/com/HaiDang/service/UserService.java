package com.HaiDang.service;

import com.HaiDang.exception.UserException;
import com.HaiDang.model.Address;
import com.HaiDang.model.User;
import com.HaiDang.response.UserResponse;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User findUserById(Long userId) throws UserException;
    public User findUserProfileByJwt(String jwt) throws UserException;
    public List<UserResponse> getAllCustomer();
    void blockUser(Long userId);
    void unBlockUser(Long userId);
    PagedModel<User> getAllUsersAdmin(Integer pageNumber, Integer pageSize);
    PagedModel<User> getUserByEmail(Integer pageNumber, Integer pageSize, String email);
    List<Address> getAllAddresses(Long userId);
}
