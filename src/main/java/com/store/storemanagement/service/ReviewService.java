package com.store.storemanagement.service;

import com.store.storemanagement.dto.ReviewDTO;
import com.store.storemanagement.entity.Product;
import com.store.storemanagement.entity.Review;
import com.store.storemanagement.entity.User;
import com.store.storemanagement.exception.UserNotFoundException;
import com.store.storemanagement.repository.ProductRepository;
import com.store.storemanagement.repository.ReviewRepository;
import com.store.storemanagement.mapper.ReviewMapper;
import com.store.storemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper reviewMapper;
    private final UserRepository userRepository;

    public ReviewDTO addReview(ReviewDTO reviewDTO, String email) {
        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + reviewDTO.getProductId()));
        User user = userRepository.findById(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        Review review = reviewMapper.reviewDTOToReview(reviewDTO);
        review.setProduct(product);
        review.setUser(user);
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.reviewToReviewDTO(savedReview);
    }

    public List<ReviewDTO> getReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        List<Review> reviews = reviewRepository.findByProduct(product);
        return reviews.stream()
                .map(reviewMapper::reviewToReviewDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
        return reviewMapper.reviewToReviewDTO(review);
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
        reviewRepository.delete(review);
    }
}

