package com.HaiDang.controller;

import com.HaiDang.exception.OrderException;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.Address;
import com.HaiDang.model.Order;
import com.HaiDang.model.User;
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
    public ResponseEntity<Order> createOrder(@RequestBody Address address, @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.createOrder(user, address);
        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getAllProductByUser(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.findOrdersByUser(user.getId());
        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable("id")Long orderId) throws OrderException {
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}
