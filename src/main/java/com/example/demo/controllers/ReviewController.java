package com.example.demo.controllers;

import com.example.demo.dto.ReviewCreateDTO;
import com.example.demo.dto.ReviewDto;
import com.example.demo.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/feedback/{orderId}")
    public String showFeedbackForm(@PathVariable Long orderId, Model model) {
        if (!reviewService.orderExists(orderId)) {
            model.addAttribute("error", "Замовлення не знайдено");
            return "feedback_form";
        }

        if (reviewService.hasReview(orderId)) {
            model.addAttribute("error", "Ви вже залишили відгук для цього замовлення");
            return "feedback_form";
        }

        model.addAttribute("orderId", orderId);
        return "feedback_form";
    }

    @PostMapping("/feedback/{orderId}")
    public String submitFeedback(@PathVariable Long orderId,
                                 @RequestParam Integer rating,
                                 @RequestParam String comment,
                                 @RequestParam(required = false) String customerName,
                                 Model model) {
        if (reviewService.hasReview(orderId)) {
            model.addAttribute("error", "Ви вже залишили відгук для цього замовлення");
            return "feedback_form";
        }

        ReviewCreateDTO reviewCreateDTO = new ReviewCreateDTO(orderId, rating, comment, customerName);
        reviewService.createReview(reviewCreateDTO);

        model.addAttribute("success", "Дякуємо за ваш відгук!");
        return "feedback_form";
    }

    @GetMapping("/reviews")
    public String viewReviews(Model model) {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "reviews_list";
    }
}

