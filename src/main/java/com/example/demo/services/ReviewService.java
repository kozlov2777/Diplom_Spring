package com.example.demo.services;

import com.example.demo.dto.ReviewCreateDTO;
import com.example.demo.dto.ReviewDto;
import com.example.demo.models.Orders;
import com.example.demo.models.Reviews;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.getAllReviews();
    }

    public boolean hasReview(Long orderId) {
        return reviewRepository.existsByOrderId(orderId);
    }

    public boolean orderExists(Long orderId) {
        return orderRepository.existsById(orderId);
    }

    public void createReview(ReviewCreateDTO reviewCreateDTO) {
        Orders order = orderRepository.findById(reviewCreateDTO.getOrderId()).orElse(null);
        
        if (order != null) {
            Reviews review = new Reviews();
            review.setOrder(order);
            review.setRating(reviewCreateDTO.getRating());
            review.setComment(reviewCreateDTO.getComment());
            review.setCustomerName(reviewCreateDTO.getCustomerName());
            review.setCreatedAt(LocalDateTime.now());
            
            reviewRepository.save(review);
        }
    }
}

