package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String category_name;
    private Long menu_item_id;
    private String menu_item_name;
    private String description;
    private double price;
}
