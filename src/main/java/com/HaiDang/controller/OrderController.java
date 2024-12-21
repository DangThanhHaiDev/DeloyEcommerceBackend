package com.HaiDang.controller;

import com.HaiDang.exception.OrderException;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.Address;
import com.HaiDang.model.Order;
import com.HaiDang.model.User;
import com.HaiDang.request.AddressRequest;
import com.HaiDang.service.OrderService;
import com.HaiDang.service.UserService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody AddressRequest address, @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        System.out.println(address.isExist());
        Order order = orderService.createOrder(user, address, address.isExist());
        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getAllProductByUser(@RequestHeader("Authorization") String jwt,
                                                           @RequestParam(required = false)List<String> filter) throws UserException {
        if(filter!=null){
            filter.stream().forEach(f -> System.out.println(f));
        }
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.findOrdersByUser(user.getId(), filter);
        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id")Long orderId) throws OrderException {
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
    @GetMapping("/status")
    public ResponseEntity<List<Order>> getOrderByStatus(@RequestParam("status")String[] status, @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.getOrderByStatus(user.getId(), status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
