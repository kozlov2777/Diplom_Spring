package com.example.demo.services;

import com.example.demo.dto.EmployeeListDto;
import com.example.demo.dto.EmployeeSalaryDto;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeListDto> getEmployeeList(){
        return employeeRepository.getEmployeeList();
    }
    public List<EmployeeSalaryDto> getEmployeeSalaries() {
        return employeeRepository.getEmployeeSalaries();
    }
}

