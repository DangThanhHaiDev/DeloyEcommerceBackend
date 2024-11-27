package com.HaiDang.service;

import com.HaiDang.exception.CartException;
import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Cart;
import com.HaiDang.model.CartItem;
import com.HaiDang.model.Product;
import com.HaiDang.model.User;
import com.HaiDang.repository.CartRepository;
import com.HaiDang.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductService productService;
    @Autowired
    CartItemService cartItemService;
    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public String
    addCartItem(Long userId, AddItemRequest addItemRequest) throws ProductException {
      Cart cart = cartRepository.findByUserId(userId);
      Product product = productService.findProductById(addItemRequest.getProductId());
      if(cartItemService.isCartItemExist(cart, product, addItemRequest.getSize(), userId) == null){
          CartItem cartItem = new CartItem();
                  cartItem.setUserId(userId);
                  cartItem.setSize(addItemRequest.getSize());
                  cartItem.setProduct(product);
                  cartItem.setQuantity(addItemRequest.getQuantity());
                  cartItem.setCart(cart);
          cart.getCartItems().add(cartItem);
          cartItemService.createCartItem(cartItem);
          cartRepository.save(cart);
      }
      findUserCart(userId);
      return "Item add to cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        double totalPrice = 0;
        double totalDiscountedPrice = 0;
        int totalItem = 0;

        for(CartItem cartItem : cart.getCartItems()){
            totalPrice += cartItem.getPrice() * cartItem.getQuantity();
            totalDiscountedPrice += cartItem.getDiscountedPrice()*cartItem.getQuantity();
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);

        return cartRepository.save(cart);
    }
}
