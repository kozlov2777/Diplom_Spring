package com.example.demo.repositories;

import com.example.demo.dto.ShiftChangeRequestDto;
import com.example.demo.models.ShiftChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShiftChangeRequestRepository extends JpaRepository<ShiftChangeRequest, Long> {
    @Query("SELECT new com.example.demo.dto.ShiftChangeRequestDto(scr.id, scr.employee.id, scr.employee.firstName, " +
            "scr.employee.lastName, scr.schedule.workDate, scr.schedule.shift.name, scr.reason, scr.status, scr.requestedAt) " +
            "FROM ShiftChangeRequest scr WHERE scr.status = 'PENDING' ORDER BY scr.requestedAt")
    List<ShiftChangeRequestDto> getPendingRequests();

    @Query("SELECT new com.example.demo.dto.ShiftChangeRequestDto(scr.id, scr.employee.id, scr.employee.firstName, " +
            "scr.employee.lastName, scr.schedule.workDate, scr.schedule.shift.name, scr.reason, scr.status, scr.requestedAt) " +
            "FROM ShiftChangeRequest scr WHERE scr.employee.id = :employeeId ORDER BY scr.requestedAt DESC")
    List<ShiftChangeRequestDto> getEmployeeRequests(Long employeeId);
}

