package com.example.demo.services;

import com.example.demo.dto.ShiftChangeRequestDto;
import com.example.demo.models.Employees;
import com.example.demo.models.ShiftChangeRequest;
import com.example.demo.models.WorkSchedule;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.ShiftChangeRequestRepository;
import com.example.demo.repositories.WorkScheduleRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShiftChangeRequestService {
    private final ShiftChangeRequestRepository shiftChangeRequestRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final EmployeeRepository employeeRepository;

    public ShiftChangeRequestService(ShiftChangeRequestRepository shiftChangeRequestRepository,
                                    WorkScheduleRepository workScheduleRepository,
                                    EmployeeRepository employeeRepository) {
        this.shiftChangeRequestRepository = shiftChangeRequestRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<ShiftChangeRequestDto> getPendingRequests() {
        return shiftChangeRequestRepository.getPendingRequests();
    }

    public List<ShiftChangeRequestDto> getEmployeeRequests(Long employeeId) {
        return shiftChangeRequestRepository.getEmployeeRequests(employeeId);
    }

    public void createRequest(Long scheduleId, Long employeeId, String reason) {
        WorkSchedule schedule = workScheduleRepository.findById(scheduleId).orElse(null);
        Employees employee = employeeRepository.findById(employeeId).orElse(null);

        if (schedule != null && employee != null) {
            ShiftChangeRequest request = new ShiftChangeRequest();
            request.setSchedule(schedule);
            request.setEmployee(employee);
            request.setReason(reason);
            request.setStatus("PENDING");
            request.setRequestedAt(LocalDateTime.now());
            
            shiftChangeRequestRepository.save(request);
        }
    }

    public void approveRequest(Long requestId, Long adminId) {
        ShiftChangeRequest request = shiftChangeRequestRepository.findById(requestId).orElse(null);
        if (request != null) {
            request.setStatus("APPROVED");
            request.setReviewedAt(LocalDateTime.now());
            
            Employees admin = employeeRepository.findById(adminId).orElse(null);
            request.setReviewedBy(admin);
            
            // Позначаємо день як вихідний
            WorkSchedule schedule = request.getSchedule();
            schedule.setIsDayOff(true);
            workScheduleRepository.save(schedule);
            
            shiftChangeRequestRepository.save(request);
        }
    }

    public void rejectRequest(Long requestId, Long adminId) {
        ShiftChangeRequest request = shiftChangeRequestRepository.findById(requestId).orElse(null);
        if (request != null) {
            request.setStatus("REJECTED");
            request.setReviewedAt(LocalDateTime.now());
            
            Employees admin = employeeRepository.findById(adminId).orElse(null);
            request.setReviewedBy(admin);
            
            shiftChangeRequestRepository.save(request);
        }
    }
}

