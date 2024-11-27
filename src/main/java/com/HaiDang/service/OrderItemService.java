package com.HaiDang.service;

import com.HaiDang.model.OrderItem;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemService {
    public OrderItem createOrderItem(OrderItem orderItem);
}
