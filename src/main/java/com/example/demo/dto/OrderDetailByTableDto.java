package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderDetailByTableDto {
    private Long id;
    private String name;
    private int quantity;
    private LocalDateTime createdAt;
    private double total;

}