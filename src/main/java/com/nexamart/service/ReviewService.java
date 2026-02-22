package com.nexamart.service;

import java.util.List;

import com.nexamart.modal.Product;
import com.nexamart.modal.Review;
import com.nexamart.modal.User;
import com.nexamart.request.CreateReviewRequest;

public interface ReviewService {

	Review createReview(CreateReviewRequest req, User user, Product product);

	List<Review> getReviewByProductId(Long productId);

	Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;

	void deleteReview(Long reviewId, Long userId) throws Exception;

	Review getReviewById(Long reviewId) throws Exception;

}
