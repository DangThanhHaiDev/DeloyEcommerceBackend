package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Cart;
import com.HaiDang.model.User;
import com.HaiDang.request.AddItemRequest;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    public Cart createCart(User user);
    public String addCartItem(Long userId, AddItemRequest addItemRequest) throws ProductException;
    public Cart findUserCart(Long userId);
}
