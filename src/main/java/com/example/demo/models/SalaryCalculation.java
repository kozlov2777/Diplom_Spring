package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "salary_calculations")
public class SalaryCalculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    private LocalDate periodStart;
    private LocalDate periodEnd;
    
    private Integer hoursWorked;
    private Double baseSalary;
    
    private Integer ordersCompleted;
    private Double orderBonus;
    
    private Double averageRating;
    private Double reviewBonus;
    
    private Integer absences;
    private Double penalties;
    
    private Double totalSalary;
    private LocalDateTime calculatedAt;
}

