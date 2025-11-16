package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ScheduleDto {
    private Long scheduleId;
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String roleName;
    private Long shiftId;
    private String shiftName;
    private LocalDate workDate;
    private Boolean isDayOff;
}

