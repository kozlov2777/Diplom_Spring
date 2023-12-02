package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeSalaryDto {
    private String firstName;
    private String lastName;
    private double salary;
}
