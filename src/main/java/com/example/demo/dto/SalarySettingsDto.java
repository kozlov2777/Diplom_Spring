package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalarySettingsDto {
    private Long id;
    private Long roleId;
    private String roleName;
    private Double hourlyRate;
    private Double orderBonus;
    private Double reviewBonusCoefficient;
    private Double absencePenalty;
}

