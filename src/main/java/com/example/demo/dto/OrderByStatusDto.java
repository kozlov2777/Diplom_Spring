package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderByStatusDto {
    private Long id;
    private Long tableNumber;
    private String name;
    private int quantity;
    private LocalDateTime createdAt;
    private double total;
    private String status;
    private String employeeFirstName;


}

