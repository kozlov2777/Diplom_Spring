package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchaseDto {
    private Long id;
    private String ingredientName;
    private Double quantity;
    private String unit;
    private Double price;
    private LocalDateTime purchasedAt;
    private String purchasedByFirstName;
    private String purchasedByLastName;
}

