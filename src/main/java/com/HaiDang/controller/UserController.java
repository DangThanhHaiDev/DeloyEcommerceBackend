package com.HaiDang.controller;

import com.HaiDang.exception.UserException;
import com.HaiDang.model.Address;
import com.HaiDang.model.Product;
import com.HaiDang.model.User;
import com.HaiDang.response.UserAdminResponse;
import com.HaiDang.response.UserResponse;
import com.HaiDang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<User> getUserByJwt(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllCustomer(){
        List<UserResponse> users = userService.getAllCustomer();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PostMapping("/block")
    public void BlockUser(@RequestBody Long id){
        userService.blockUser(id);
    }
    @PostMapping("/unBlock")
    public void unBlockUser(@RequestBody Long id){
        userService.unBlockUser(id);
    }

    @GetMapping("/adminUsers")
    public ResponseEntity<UserAdminResponse> getAllUsersAdmin(@RequestParam("pageNumber") Integer pageNumber,@RequestParam("pageSize") Integer pageSize){
        PagedModel<User> pagedModel = userService.getAllUsersAdmin(pageNumber, pageSize);
        UserAdminResponse userAdminResponse = new UserAdminResponse();
        userAdminResponse.setTotalElements(pagedModel.getMetadata().getTotalElements());
        userAdminResponse.setTotalPages(pagedModel.getMetadata().getTotalPages());
        List<User> userList = StreamSupport.stream(pagedModel.spliterator(), false)
                .collect(Collectors.toList());
        userAdminResponse.setUsers(userList);
        return new ResponseEntity<>(userAdminResponse, HttpStatus.OK);
    }

    @GetMapping("/search/email")
    public ResponseEntity<UserAdminResponse> searchByEmail(@RequestParam("pageNumber") Integer pageNumber,@RequestParam("pageSize") Integer pageSize,@RequestParam("email") String email){
        PagedModel<User> pagedModel = userService.getUserByEmail(pageNumber, pageSize, email);
        UserAdminResponse userAdminResponse = new UserAdminResponse();
        userAdminResponse.setTotalElements(pagedModel.getMetadata().getTotalElements());
        userAdminResponse.setTotalPages(pagedModel.getMetadata().getTotalPages());
        List<User> userList = StreamSupport.stream(pagedModel.spliterator(), false)
                .collect(Collectors.toList());
        userAdminResponse.setUsers(userList);
        return new ResponseEntity<>(userAdminResponse, HttpStatus.OK);
    }
    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAllAddresses(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Address> addresses = userService.getAllAddresses(user.getId());
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }
}

