package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Product;
import com.HaiDang.model.Review;
import com.HaiDang.model.User;
import com.HaiDang.repository.ReviewRepository;
import com.HaiDang.request.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    ProductService productService;

    @Override
    public Review createReview(ReviewRequest reviewRequest, User user) throws ProductException {
        Product product = productService.findProductById(reviewRequest.getProductId());
        Review review = Review.builder()
                .createdAt(LocalDateTime.now())
                .review(reviewRequest.getReview())
                .product(product)
                .user(user)
                .build();
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviewsByProductId(Long productId) {
        return reviewRepository.findReviewsByProductId(productId);
    }
}
