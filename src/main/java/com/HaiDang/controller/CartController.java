package com.HaiDang.controller;

import com.HaiDang.exception.ProductException;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.Cart;
import com.HaiDang.model.User;
import com.HaiDang.request.AddItemRequest;
import com.HaiDang.response.ProductResponse;
import com.HaiDang.service.CartService;
import com.HaiDang.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart Management", description = "find user cart, add item to cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;
    @GetMapping("/")
    @Operation(description = "find cart by user id")
    public ResponseEntity<Cart> getUserCartByJwt(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }
    @PutMapping("/add")
    @Operation(description = "add item to cart")
    public ResponseEntity<ProductResponse> addItemToCart(@RequestBody AddItemRequest addItemRequest,
                                                         @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        cartService.addCartItem(user.getId(), addItemRequest);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setSuccess(true);
        productResponse.setMessage("Item is added successfully");
        return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
    }
}
