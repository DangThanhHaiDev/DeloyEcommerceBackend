package com.HaiDang.service;

import com.HaiDang.exception.ProductException;
import com.HaiDang.model.Rating;
import com.HaiDang.model.User;
import com.HaiDang.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {
    public Rating createRating(RatingRequest ratingRequest, User user) throws ProductException;
    public List<Rating> getAllRatingsByProductId(Long productId);
}
