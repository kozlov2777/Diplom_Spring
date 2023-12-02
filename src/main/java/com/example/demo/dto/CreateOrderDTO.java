package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class CreateOrderDTO {
    private Long tableId;
    private LocalDateTime createdAt;
    private Long employeeId;
    private Long statusId;
    private List<Long> itemIds;
    private List<Integer> quantities;


}
