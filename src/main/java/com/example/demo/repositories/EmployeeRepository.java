package com.example.demo.repositories;

import com.example.demo.dto.EmployeeListDto;
import com.example.demo.dto.EmployeeSalaryDto;
import com.example.demo.models.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employees, Long> {
    @Query("SELECT new com.example.demo.dto.EmployeeSalaryDto(e.firstName, e.lastName, COUNT(o.id) * 30 + 12000) " +
            "FROM Employees e " +
            "JOIN Orders o ON o.employee.id = e.id " +
            "GROUP BY e.id")
    List<EmployeeSalaryDto> getEmployeeSalaries();

    @Query("SELECT new com.example.demo.dto.EmployeeListDto(e.id, e.lastName)FROM Employees e WHERE e.role.id = 2")
    List<EmployeeListDto> getEmployeeList();



}
