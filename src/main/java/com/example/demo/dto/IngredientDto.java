package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientDto {
    private Long id;
    private String name;
    private Double quantity;
    private String unit;
    private Double minQuantity;
    private boolean isLowStock;
}

