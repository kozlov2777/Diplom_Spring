package com.example.demo.repositories;

import com.example.demo.dto.ScheduleDto;
import com.example.demo.dto.EmployeeScheduleStatsDto;
import com.example.demo.models.WorkSchedule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
    @Query("SELECT new com.example.demo.dto.ScheduleDto(ws.id, ws.employee.id, ws.employee.firstName, ws.employee.lastName, " +
            "ws.shift.id, ws.shift.name, ws.workDate, ws.isDayOff) " +
            "FROM WorkSchedule ws WHERE ws.workDate BETWEEN :startDate AND :endDate ORDER BY ws.workDate, ws.shift.id")
    List<ScheduleDto> getScheduleByDateRange(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.example.demo.dto.ScheduleDto(ws.id, ws.employee.id, ws.employee.firstName, ws.employee.lastName, " +
            "ws.shift.id, ws.shift.name, ws.workDate, ws.isDayOff) " +
            "FROM WorkSchedule ws WHERE ws.employee.id = :employeeId AND ws.workDate BETWEEN :startDate AND :endDate " +
            "ORDER BY ws.workDate")
    List<ScheduleDto> getEmployeeSchedule(Long employeeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(ws) FROM WorkSchedule ws WHERE ws.workDate = :date AND ws.shift.id = :shiftId AND ws.isDayOff = false")
    Long countWorkersOnShift(LocalDate date, Long shiftId);

    @Query("SELECT COUNT(ws) FROM WorkSchedule ws WHERE ws.workDate = :date AND ws.shift.id = :shiftId " +
            "AND ws.employee.role.id = :roleId AND ws.isDayOff = false")
    Long countWorkersByRoleOnShift(LocalDate date, Long shiftId, Long roleId);

    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.workDate BETWEEN :startDate AND :endDate")
    List<WorkSchedule> findByDateRange(LocalDate startDate, LocalDate endDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM WorkSchedule ws WHERE ws.workDate BETWEEN :startDate AND :endDate")
    void deleteByDateRange(LocalDate startDate, LocalDate endDate);

    @Query("SELECT new com.example.demo.dto.EmployeeScheduleStatsDto(e.id, e.firstName, e.lastName, " +
            "COUNT(CASE WHEN ws.isDayOff = false THEN 1 END), " +
            "COUNT(CASE WHEN ws.isDayOff = true THEN 1 END)) " +
            "FROM Employees e " +
            "LEFT JOIN WorkSchedule ws ON e.id = ws.employee.id AND ws.workDate BETWEEN :startDate AND :endDate " +
            "WHERE e.role.id IN (3, 4) " +
            "GROUP BY e.id, e.firstName, e.lastName")
    List<EmployeeScheduleStatsDto> getEmployeeStats(LocalDate startDate, LocalDate endDate);
}

