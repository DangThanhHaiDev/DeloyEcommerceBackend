package com.HaiDang.controller;

import com.HaiDang.exception.ProductException;
import com.HaiDang.exception.UserException;
import com.HaiDang.model.Product;
import com.HaiDang.model.Review;
import com.HaiDang.model.User;
import com.HaiDang.request.ReviewRequest;
import com.HaiDang.service.ReviewService;
import com.HaiDang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    UserService userService;
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest reviewRequest,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(reviewRequest, user);
        return new ResponseEntity<Review>(review, HttpStatus.CREATED);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getAllReviewsByProduct(@PathVariable("productId") Long productId){
        List<Review> reviews = reviewService.getAllReviewsByProductId(productId);
        return new ResponseEntity<List<Review>>(reviews, HttpStatus.OK);
    }
}
