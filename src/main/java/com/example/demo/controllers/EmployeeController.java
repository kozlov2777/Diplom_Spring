package com.example.demo.controllers;

import com.example.demo.dto.EmployeeCreateDTO;
import com.example.demo.dto.EmployeeSalaryDto;
import com.example.demo.dto.SalaryDetailDto;
import com.example.demo.dto.SalarySettingsDto;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.SalaryService;
import com.example.demo.services.ExcelExportService;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;


@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SalaryService salaryService;
    private final ExcelExportService excelExportService;

    public EmployeeController(EmployeeService employeeService, SalaryService salaryService, ExcelExportService excelExportService) {
        this.employeeService = employeeService;
        this.salaryService = salaryService;
        this.excelExportService = excelExportService;
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

    @GetMapping("/salary/export/excel")
    public ResponseEntity<byte[]> exportSalaryToExcel(@RequestParam String startDate,
                                                      @RequestParam String endDate) throws IOException {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<SalaryDetailDto> salaries = salaryService.calculateSalaries(start, end);
        byte[] excelFile = excelExportService.exportSalaryToExcel(salaries, start, end);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "salary_" + start + "_" + end + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile);
    }

    @GetMapping("/salary/settings")
    public String salarySettings(Model model) {
        List<SalarySettingsDto> settings = salaryService.getAllSettings();
        
        // Знаходимо налаштування для офіціантів (roleId = 3) та кухарів (roleId = 2)
        SalarySettingsDto waiterSettings = settings.stream()
                .filter(s -> s.getRoleId() == 3L)
                .findFirst()
                .orElse(new SalarySettingsDto(null, 3L, "Офіціант", 100.0, 10.0, 20.0, 200.0));
        
        SalarySettingsDto cookSettings = settings.stream()
                .filter(s -> s.getRoleId() == 2L)
                .findFirst()
                .orElse(new SalarySettingsDto(null, 2L, "Кухар", 120.0, 15.0, 25.0, 250.0));
        
        model.addAttribute("settings", settings);
        model.addAttribute("waiterSettings", waiterSettings);
        model.addAttribute("cookSettings", cookSettings);
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

