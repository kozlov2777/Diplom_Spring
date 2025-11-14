package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeOnShiftDto {
    private String firstName;
    private String lastName;
    private String roleName;
    private String shiftName;
}

