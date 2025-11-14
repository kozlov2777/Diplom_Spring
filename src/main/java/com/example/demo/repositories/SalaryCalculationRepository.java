package com.example.demo.repositories;

import com.example.demo.models.SalaryCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryCalculationRepository extends JpaRepository<SalaryCalculation, Long> {
    @Query("SELECT sc FROM SalaryCalculation sc WHERE sc.employee.id = :employeeId " +
            "AND sc.periodStart = :periodStart AND sc.periodEnd = :periodEnd")
    Optional<SalaryCalculation> findByEmployeeAndPeriod(Long employeeId, LocalDate periodStart, LocalDate periodEnd);
    
    @Query("SELECT sc FROM SalaryCalculation sc WHERE sc.periodStart = :periodStart AND sc.periodEnd = :periodEnd " +
            "ORDER BY sc.totalSalary DESC")
    List<SalaryCalculation> findByPeriod(LocalDate periodStart, LocalDate periodEnd);
}

