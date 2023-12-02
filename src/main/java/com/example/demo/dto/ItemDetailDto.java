package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDetailDto {
    private String menuItemName;
    private int quantity;
    private String ingredientName;
    private String unit;
    private double calories;
}
