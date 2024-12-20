package com.HaiDang.service;

import com.HaiDang.exception.CartException;
import com.HaiDang.exception.CartItemException;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.Cart;
import com.HaiDang.model.CartItem;
import com.HaiDang.model.Product;
import org.springframework.stereotype.Service;

@Service
public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
    public CartItem findCartItemById(Long cartItemId) throws CartItemException;

}
