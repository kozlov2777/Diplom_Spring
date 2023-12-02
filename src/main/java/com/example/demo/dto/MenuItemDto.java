package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuItemDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private double totalCalories;
}
