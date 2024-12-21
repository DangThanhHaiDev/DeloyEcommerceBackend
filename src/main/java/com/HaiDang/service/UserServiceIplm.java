package com.HaiDang.service;

import com.HaiDang.config.JwtProvider;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.Address;
import com.HaiDang.model.Product;
import com.HaiDang.model.User;
import com.HaiDang.repository.OrderRepository;
import com.HaiDang.repository.UserRepository;
import com.HaiDang.response.UserResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceIplm implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    OrderRepository orderRepository;
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
        Date date = jwtProvider.getExpirationDate(jwt);
        if(date.before(new Date())){
            return null;
        }

        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("User not found with Email: "+email);
        }
        return user;
    }

    @Override
    public List<UserResponse> getAllCustomer() {
        List<UserResponse> userResponses = new ArrayList<>();
        List<User> users = userRepository.findAllCustomer();
        for (User u : users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setBlock(u.isBlock());
            userResponse.setCreatedAt(u.getCreatedAt());
            userResponse.setEmail(u.getEmail());
            userResponse.setFirstName(u.getFirstName());
            userResponse.setLastName(u.getLastName());
            userResponse.setOrdersNumber(orderRepository.getNumberOrderByUser(u.getId()));
            userResponse.setMoney(orderRepository.getTotalMoneyByUser(u.getId()));
            userResponse.setId(u.getId());
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    @Override
    public void blockUser(Long userId) {
        userRepository.BlockUser(userId);
    }

    @Override
    public void unBlockUser(Long userId) {
        userRepository.unBlockUser(userId);
    }

    @Override
    public PagedModel<User> getAllUsersAdmin(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<User> users = userRepository.findUsersAdmin();
        int start = (int)pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), users.size());
        List<User> usersFilter = users.subList(start, end);
        Page<User> userPage = new PageImpl<>(usersFilter, pageable, users.size());
        PagedModel<User> pagedModel = PagedModel.of(userPage.getContent(), new PagedModel.PageMetadata(userPage.getSize(), userPage.getNumber(), userPage.getTotalElements(), userPage.getTotalPages()));
        return pagedModel;
    }

    @Override
    public PagedModel<User> getUserByEmail(Integer pageNumber, Integer pageSize, String email) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<User> users = userRepository.findUserByEmail(email);
        int start = (int)pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), users.size());
        List<User> usersFilter = users.subList(start, end);
        Page<User> userPage = new PageImpl<>(usersFilter, pageable, users.size());
        PagedModel<User> pagedModel = PagedModel.of(userPage.getContent(), new PagedModel.PageMetadata(userPage.getSize(), userPage.getNumber(), userPage.getTotalElements(), userPage.getTotalPages()));
        return pagedModel;
    }

    @Override
    public List<Address> getAllAddresses(Long userId) {
        return userRepository.findAllAddresses(userId);
    }

}
