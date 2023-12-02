package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data

public class OrderDetailDto {
    private Long id;
    private String itemName;
    private int quantity;
    private double price;
    private LocalDateTime createdAt;
    private String lastName;
    private Long tableId;
    private String status;

    public OrderDetailDto(Long id, String itemName, int quantity, double price, LocalDateTime createdAt, String lastName, Long tableId, String status) {
        this.id = id;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = createdAt;
        this.lastName = lastName;
        this.tableId = tableId;
        this.status = status;
    }
}
