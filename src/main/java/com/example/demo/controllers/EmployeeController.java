package com.example.demo.controllers;

import com.example.demo.dto.EmployeeCreateDTO;
import com.example.demo.dto.EmployeeSalaryDto;
import com.example.demo.dto.SalaryDetailDto;
import com.example.demo.dto.SalarySettingsDto;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.SalaryService;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SalaryService salaryService;

    public EmployeeController(EmployeeService employeeService, SalaryService salaryService) {
        this.employeeService = employeeService;
        this.salaryService = salaryService;
    }

    @GetMapping("/salary")
    public String getEmployeeSalaries(@RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate,
                                     Model model) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().withDayOfMonth(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        List<SalaryDetailDto> salaries = salaryService.calculateSalaries(start, end);
        
        model.addAttribute("salaries", salaries);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        
        return "salary";
    }

    @GetMapping("/salary/settings")
    public String salarySettings(Model model) {
        List<SalarySettingsDto> settings = salaryService.getAllSettings();
        model.addAttribute("settings", settings);
        return "salary_settings";
    }

    @PostMapping("/salary/settings/save")
    public String saveSalarySettings(@RequestParam Long roleId,
                                    @RequestParam Double hourlyRate,
                                    @RequestParam Double orderBonus,
                                    @RequestParam Double reviewBonus,
                                    @RequestParam Double absencePenalty) {
        salaryService.saveSalarySettings(roleId, hourlyRate, orderBonus, reviewBonus, absencePenalty);
        return "redirect:/salary/settings";
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

