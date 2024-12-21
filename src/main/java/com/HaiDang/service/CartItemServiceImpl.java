package com.HaiDang.service;

import com.HaiDang.exception.CartItemException;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.Cart;
import com.HaiDang.model.CartItem;
import com.HaiDang.model.Product;
import com.HaiDang.model.User;
import com.HaiDang.repository.CartItemRepository;
import com.HaiDang.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProductRepository productRepository;
    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice());
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws UserException, CartItemException {
        CartItem item = findCartItemById(id);
        Optional<Product> product = productRepository.findById(cartItem.getProduct().getId());
        User user = userService.findUserById(userId);
        if(Objects.equals(user.getId(), item.getUserId())){
            item.setQuantity(cartItem.getQuantity());
        }
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        return cartItemRepository.isCartItemExist(cart, product, size, userId);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
            CartItem cartItem = findCartItemById(cartItemId);
            User user = userService.findUserById(userId);
            System.out.println(cartItem.getUserId()+"---"+userId);
            if(Objects.equals(userId, cartItem.getUserId())){
                cartItemRepository.delete(cartItem);
            }
        else{
            throw new CartItemException("You can't remove another users item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        if(optionalCartItem.isPresent()){
            return optionalCartItem.get();
        }
        throw new CartItemException("Cart Item not found with ID: "+cartItemId);
    }
}
