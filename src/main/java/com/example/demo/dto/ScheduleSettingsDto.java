package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSettingsDto {
    private Long shiftId;
    private String shiftName;
    private Integer waitersCount;
    private Integer cooksCount;
}

