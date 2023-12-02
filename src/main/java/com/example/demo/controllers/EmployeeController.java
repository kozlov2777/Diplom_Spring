package com.example.demo.controllers;

import com.example.demo.dto.EmployeeSalaryDto;
import com.example.demo.services.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/salary")
    public String getEmployeeSalaries(Model model) {
        List<EmployeeSalaryDto> employeeSalaries = employeeService.getEmployeeSalaries();
        model.addAttribute("salary", employeeSalaries);
        return "salary";
    }
}

