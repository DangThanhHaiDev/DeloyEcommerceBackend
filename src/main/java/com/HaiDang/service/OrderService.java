package com.HaiDang.service;

import com.HaiDang.exception.OrderException;
import com.HaiDang.model.Address;
import com.HaiDang.model.Order;
import com.HaiDang.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    public Order createOrder(User user, Address address);
    public Order findOrderById(Long orderId) throws OrderException;
    public List<Order> findOrdersByUser(Long userId);
    public Order placedOrder(Long orderId) throws OrderException;
    public Order confirmedOrder(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order canceledOrder(Long orderId) throws OrderException;
    List<Order> getAllOrders();
    public void deleteOrder(Long orderId) throws OrderException;
}
