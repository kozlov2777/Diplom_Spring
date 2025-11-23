package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private Long orderId;
    private Integer rating;
    private String comment;
    private String customerName;
    private LocalDateTime createdAt;
}

