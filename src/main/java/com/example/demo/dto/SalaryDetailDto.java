package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryDetailDto {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String roleName;
    
    // Базова зарплата
    private Integer hoursWorked;
    private Double hourlyRate;
    private Double baseSalary;
    
    // Бонуси за замовлення
    private Integer ordersCompleted;
    private Double orderBonus;
    private Double orderBonusTotal;
    
    // Премії за відгуки
    private Integer reviewsCount;
    private Double averageRating;
    private Double reviewBonusCoefficient;
    private Double reviewBonus;
    
    // Штрафи
    private Integer absences;
    private Double absencePenalty;
    private Double penaltiesTotal;
    
    // Підсумок
    private Double totalSalary;
    
    // Порівняння з минулим періодом
    private Double previousSalary;
    private Double salaryChange;
    private Double salaryChangePercent;
    
    // Рівень
    private String performanceLevel;
}

