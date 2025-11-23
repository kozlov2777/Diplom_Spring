package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PopularDishDto {
    private String dishName;
    private Long ordersCount;
    private Double totalRevenue;
}

