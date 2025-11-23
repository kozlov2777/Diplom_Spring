package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShiftChangeRequestDto {
    private Long id;
    private Long employeeId;
    private String firstName;
    private String lastName;
    private LocalDate workDate;
    private String shiftName;
    private String reason;
    private String status;
    private LocalDateTime requestedAt;
}

