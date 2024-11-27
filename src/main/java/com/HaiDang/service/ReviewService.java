package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Review;
import com.HaiDang.model.User;
import com.HaiDang.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    public Review createReview(ReviewRequest reviewRequest, User user) throws ProductException;
    public List<Review> getAllReviewsByProductId(Long productId);
}
