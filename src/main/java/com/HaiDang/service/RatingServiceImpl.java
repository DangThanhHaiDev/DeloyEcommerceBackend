package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Product;
import com.HaiDang.model.Rating;
import com.HaiDang.model.User;
import com.HaiDang.repository.RatingRepository;
import com.HaiDang.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService{
    @Autowired
    RatingRepository ratingRepository;
    @Autowired
    ProductService productService;
    @Override
    public Rating createRating(RatingRequest ratingRequest, User user) throws ProductException {
        Product product = productService.findProductById(ratingRequest.getProductId());
        Rating rating = Rating.builder()
                .createdAt(LocalDateTime.now())
                .rating(ratingRequest.getRating())
                .user(user)
                .product(product)
                .build();
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatingsByProductId(Long productId) {
        return ratingRepository.findRatingsByProductId(productId);
    }
}
