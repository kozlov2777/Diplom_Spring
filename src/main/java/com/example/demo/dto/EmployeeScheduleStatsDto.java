package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeScheduleStatsDto {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private Long workDaysCount;
    private Long daysOffCount;
}

