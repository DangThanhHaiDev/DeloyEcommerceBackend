package com.HaiDang.service;

import com.HaiDang.exception.OrderException;
import com.HaiDang.model.*;
import com.HaiDang.repository.*;
import com.HaiDang.request.AddressRequest;
import com.HaiDang.response.SalesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    CartRepository cartRepository;

    @Override
    public Order createOrder(User user, AddressRequest address, boolean isExist) {
        Address address1 = null;
        if (!isExist) {
            address1 = new Address();
            address1.setCity(address.getCity());
            address1.setMobile(address.getMobile());
            address1.setStreetAddress(address.getStreetAddress());
            address1.setUser(user);
            addressRepository.save(address1);
            user.getAddresses().add(address1);
            userRepository.save(user);
        }


        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        Cart cart = cartService.findUserCart(user.getId());
        for (CartItem ci : cart.getCartItems()) {
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
        order.setShppingAddress(address1);
        order.setOrderItems(orderItems);
        order.setTotalItem(cart.getTotalItem());
        order.setTotalPrice(cart.getTotalPrice());
        order.setDiscountedPrice(cart.getTotalDiscountedPrice());
        order.setOrderStatus("PENDING");
        order.getPaymentDetails().setStatus("PENDING");
        order.setShppingAddress(address1);
        order.setUser(user);
        Order newOrder = orderRepository.save(order);
        for (OrderItem oi : orderItems) {
            orderItemService.createOrderItem(oi);
        }
        cart.getCartItems().clear();
        cartRepository.save(cart);
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
    public List<Order> findOrdersByUser(Long userId, List<String> filter) {
        List<Order> orders = orderRepository.getUserOrders(userId);
        if(filter!=null && !filter.isEmpty() && !filter.get(0).equals("null")){
            orders = orders.stream().filter(o -> filter.contains(o.getOrderStatus()))
                    .collect(Collectors.toList());
        }
        return orders;
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

    @Override
    public List<Order> getOrdersByFilter(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.getOrderByFilter(startDate, endDate);
        return orders;
    }

    @Override
    public int getOrdersNumberByUser(Long userId) {
        return orderRepository.getNumberOrderByUser(userId);
    }

    @Override
    public List<Order> getOrderByStatus(Long userId, String[] status) {
        List<Order> orders = orderRepository.getOrderByStatus(userId);
        List<Order> orderList = orders.stream().filter(o-> Arrays.asList(status).contains(o.getOrderStatus())).collect(Collectors.toList());
        return orderList;
    }

    @Override
    public List<SalesResponse> getRevenueByYear(int year) {
        return orderRepository.findRevenueByYear(year);
    }
}
