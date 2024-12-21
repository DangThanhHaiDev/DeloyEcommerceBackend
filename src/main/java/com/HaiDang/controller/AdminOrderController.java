package com.HaiDang.controller;

import com.HaiDang.exception.OrderException;
import com.HaiDang.model.Order;
import com.HaiDang.response.ProductResponse;
import com.HaiDang.response.SalesResponse;
import com.HaiDang.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/admin/orders")
public class AdminOrderController {
    @Autowired
    OrderService orderService;
    @GetMapping("/")
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
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable("orderId") Long orderId) throws OrderException {
        Order order = orderService.canceledOrder(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shipOrder(@PathVariable("orderId") Long orderId) throws OrderException {
        Order order = orderService.shippedOrder(orderId);
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
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getOrdersByFilter(@RequestParam String startDate, @RequestParam String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        LocalDateTime startTime = start.atStartOfDay();
        LocalDateTime endTime = end.atTime(23,59,59);
        List<Order> orders = orderService.getOrdersByFilter(startTime, endTime);
        return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
    }
    @GetMapping("/revenue/{year}")
    public ResponseEntity<List<SalesResponse>> getRevenue(@PathVariable("year") Integer year){
        List<SalesResponse> list = orderService.getRevenueByYear(year);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
