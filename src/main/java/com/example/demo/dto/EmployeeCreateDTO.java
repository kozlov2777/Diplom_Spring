package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreateDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Long roleId;
}
