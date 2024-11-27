package com.HaiDang.service;

import com.HaiDang.exception.OrderException;
import com.HaiDang.model.*;
import com.HaiDang.repository.AddressRepository;
import com.HaiDang.repository.OrderItemRepository;
import com.HaiDang.repository.OrderRepository;
import com.HaiDang.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartService cartService;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Override
    public Order createOrder(User user, Address address) {
        address.setUser(user);
        addressRepository.save(address);
        user.getAddresses().add(address);
        userRepository.save(user);

        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        Cart cart = cartService.findUserCart(user.getId());
        for (CartItem ci : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(ci.getProduct());
            orderItem.setPrice(ci.getPrice());
            orderItem.setDiscountedPrice(ci.getDiscountedPrice());
            orderItem.setSize(ci.getSize());
            orderItem.setQuantity(ci.getQuantity());
            orderItem.setUserId(user.getId());
            orderItem.setDeliveryDate(LocalDateTime.now());
            orderItems.add(orderItem);
            orderItem.setOrder(order);
       }
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setShppingAddress(address);
        order.setOrderItems(orderItems);
        order.setTotalItem(cart.getTotalItem());
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderStatus("PENDING");
        order.getPaymentDetails().setStatus("PENDING");
        order.setShppingAddress(address);
        order.setUser(user);
        Order newOrder = orderRepository.save(order);
        for (OrderItem oi : orderItems){
            orderItemService.createOrderItem(oi);
        }
        return newOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isPresent()){
            return optionalOrder.get();
        }
        throw new OrderException("Order not found with ID: "+orderId);
    }

    @Override
    public List<Order> findOrdersByUser(Long userId) {
        return orderRepository.getUserOrders(userId);
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
       Order order = findOrderById(orderId);
       order.setOrderStatus("PLACE");
       order.getPaymentDetails().setStatus("COMPLETED");
       return order;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order canceledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CANCELED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepository.deleteById(orderId );
    }
}
