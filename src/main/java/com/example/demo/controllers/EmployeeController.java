package com.example.demo.controllers;

import com.example.demo.dto.EmployeeCreateDTO;
import com.example.demo.dto.EmployeeSalaryDto;
import com.example.demo.services.EmployeeService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

    @PostMapping("/register")
    public String registerEmployee(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String username, @RequestParam String password, @RequestParam Long roleId) {
        EmployeeCreateDTO employeeCreateDTO = new EmployeeCreateDTO(firstName, lastName, username, password, roleId);
        employeeService.registerEmployee(employeeCreateDTO);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerEmployee() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied"; // Перенаправте на сторінку з повідомленням про помилку доступу.
    }


}

