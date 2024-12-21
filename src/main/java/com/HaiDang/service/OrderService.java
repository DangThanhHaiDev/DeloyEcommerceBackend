package com.HaiDang.service;

import com.HaiDang.exception.OrderException;
import com.HaiDang.model.Address;
import com.HaiDang.model.Order;
import com.HaiDang.model.User;
import com.HaiDang.request.AddressRequest;
import com.HaiDang.response.SalesResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface OrderService {
    public Order createOrder(User user, AddressRequest address, boolean isExist);
    public Order findOrderById(Long orderId) throws OrderException;
    public List<Order> findOrdersByUser(Long userId, List<String> filter);
    public Order placedOrder(Long orderId) throws OrderException;
    public Order confirmedOrder(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order canceledOrder(Long orderId) throws OrderException;
    List<Order> getAllOrders();
    public void deleteOrder(Long orderId) throws OrderException;
    public List<Order> getOrdersByFilter(LocalDateTime startDate, LocalDateTime endDate);
    int getOrdersNumberByUser(Long userId);
    List<Order> getOrderByStatus(Long userId, String[] status);
    public List<SalesResponse> getRevenueByYear(int year);
}
