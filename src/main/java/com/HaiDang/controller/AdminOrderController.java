package com.HaiDang.controller;

import com.HaiDang.exception.OrderException;
import com.HaiDang.model.Order;
import com.HaiDang.response.ProductResponse;
import com.HaiDang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/orders")
public class AdminOrderController {
    @Autowired
    OrderService orderService;
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<List<Order>>(orders, HttpStatus.ACCEPTED);
    }
    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> orderConfirm(@PathVariable("orderId") Long orderId,
                                              @RequestHeader("Authorization") String jwt) throws OrderException {
            Order order = orderService.confirmedOrder(orderId);
            return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> orderShipped(@PathVariable("orderId") Long orderId,
                                              @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ProductResponse> orderDelete(@PathVariable("{orderId}") Long orderId,
                                                       @RequestHeader("Authorization") String jwt) throws OrderException {
        orderService.deleteOrder(orderId);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setMessage("product deleted successfully");
        productResponse.setSuccess(true);
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }
}
