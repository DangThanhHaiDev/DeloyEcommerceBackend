package com.HaiDang.controller;

import com.HaiDang.exception.CartItemException;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.CartItem;
import com.HaiDang.model.User;
import com.HaiDang.response.CartItemResponse;
import com.HaiDang.service.CartItemService;
import com.HaiDang.service.CartItemServiceImpl;
import com.HaiDang.service.CartService;
import com.HaiDang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart_item")
public class CartItemController {
    @Autowired
    CartItemService cartItemService;
    @Autowired
    UserService userService;
    @Autowired
    CartService cartService;

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> deleteCartItem(@PathVariable("cartItemId") long id, @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), id);
        cartService.findUserCart(user.getId());

        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setMessage("Successfully");
        cartItemResponse.setSuccess(true);

        return new ResponseEntity<CartItemResponse>(cartItemResponse, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable("cartItemId")long id,
                                                           @RequestHeader("Authorization")String jwt,
                                                           @RequestBody CartItem cartItem) throws UserException, CartItemException {

        User user = userService.findUserProfileByJwt(jwt);
        System.out.println("wjiadjasdnnka------===:"+cartItem.toString());
        cartItemService.updateCartItem(user.getId(), id, cartItem);
        CartItemResponse response = new CartItemResponse();
        response.setSuccess(true);
        response.setMessage("Successfully");
        cartService.findUserCart(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
