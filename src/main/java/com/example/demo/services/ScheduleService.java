package com.example.demo.services;

import com.example.demo.dto.EmployeeScheduleStatsDto;
import com.example.demo.dto.ScheduleDto;
import com.example.demo.dto.ScheduleSettingsDto;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class ScheduleService {
    private final WorkScheduleRepository workScheduleRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final ScheduleSettingsRepository scheduleSettingsRepository;

    public ScheduleService(WorkScheduleRepository workScheduleRepository, 
                          EmployeeRepository employeeRepository,
                          ShiftRepository shiftRepository,
                          ScheduleSettingsRepository scheduleSettingsRepository) {
        this.workScheduleRepository = workScheduleRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRepository = shiftRepository;
        this.scheduleSettingsRepository = scheduleSettingsRepository;
    }

    public List<ScheduleDto> getScheduleByWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        return workScheduleRepository.getScheduleByDateRange(startDate, endDate);
    }

    public List<ScheduleDto> getEmployeeSchedule(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return workScheduleRepository.getEmployeeSchedule(employeeId, startDate, endDate);
    }

    public void generateSchedule(LocalDate startDate, LocalDate endDate) {
        // Видаляємо старий графік якщо є
        workScheduleRepository.deleteByDateRange(startDate, endDate);

        List<Employees> waiters = employeeRepository.findAll().stream()
                .filter(e -> e.getRole().getId() == 3L || e.getRole().getId() == 4L)
                .toList();

        List<Shift> shifts = shiftRepository.findAll();

        if (waiters.isEmpty() || shifts.isEmpty()) {
            return;
        }

        // Отримуємо налаштування для кожної зміни
        Map<Long, ScheduleSettings> settingsMap = new HashMap<>();
        for (Shift shift : shifts) {
            scheduleSettingsRepository.findByShiftId(shift.getId())
                    .ifPresent(settings -> settingsMap.put(shift.getId(), settings));
        }

        // Лічильник змін для кожного працівника
        Map<Long, Integer> employeeShiftCount = new HashMap<>();
        Map<Long, Integer> employeeDaysOffCount = new HashMap<>();
        for (Employees emp : waiters) {
            employeeShiftCount.put(emp.getId(), 0);
            employeeDaysOffCount.put(emp.getId(), 0);
        }

        LocalDate currentDate = startDate;
        int dayIndex = 0;

        while (!currentDate.isAfter(endDate)) {
            for (Shift shift : shifts) {
                ScheduleSettings settings = settingsMap.get(shift.getId());
                int waitersNeeded = settings != null ? settings.getWaitersCount() : 2;
                int cooksNeeded = settings != null ? settings.getCooksCount() : 1;

                // Вибираємо офіціантів
                List<Employees> availableWaiters = waiters.stream()
                        .filter(e -> e.getRole().getId() == 3L)
                        .sorted(Comparator.comparingInt(e -> employeeShiftCount.get(e.getId())))
                        .toList();

                for (int i = 0; i < Math.min(waitersNeeded, availableWaiters.size()); i++) {
                    Employees waiter = availableWaiters.get(i);
                    saveSchedule(waiter, shift, currentDate, false);
                    employeeShiftCount.put(waiter.getId(), employeeShiftCount.get(waiter.getId()) + 1);
                }

                // Вибираємо кухарів
                List<Employees> availableCooks = waiters.stream()
                        .filter(e -> e.getRole().getId() == 4L)
                        .sorted(Comparator.comparingInt(e -> employeeShiftCount.get(e.getId())))
                        .toList();

                for (int i = 0; i < Math.min(cooksNeeded, availableCooks.size()); i++) {
                    Employees cook = availableCooks.get(i);
                    saveSchedule(cook, shift, currentDate, false);
                    employeeShiftCount.put(cook.getId(), employeeShiftCount.get(cook.getId()) + 1);
                }
            }

            // Кожні 5 днів даємо вихідний працівникам з найбільшою кількістю змін
            dayIndex++;
            if (dayIndex % 5 == 0) {
                List<Employees> needDayOff = waiters.stream()
                        .filter(e -> employeeDaysOffCount.get(e.getId()) < 2)
                        .sorted((e1, e2) -> Integer.compare(
                                employeeShiftCount.get(e2.getId()), 
                                employeeShiftCount.get(e1.getId())))
                        .limit(waiters.size() / 3)
                        .toList();

                for (Employees emp : needDayOff) {
                    saveSchedule(emp, shifts.get(0), currentDate.plusDays(1), true);
                    employeeDaysOffCount.put(emp.getId(), employeeDaysOffCount.get(emp.getId()) + 1);
                }
            }

            currentDate = currentDate.plusDays(1);
        }
    }

    private void saveSchedule(Employees employee, Shift shift, LocalDate date, boolean isDayOff) {
        WorkSchedule schedule = new WorkSchedule();
        schedule.setEmployee(employee);
        schedule.setShift(shift);
        schedule.setWorkDate(date);
        schedule.setIsDayOff(isDayOff);
        workScheduleRepository.save(schedule);
    }

    public void updateSchedule(Long scheduleId, Long shiftId, Boolean isDayOff) {
        WorkSchedule schedule = workScheduleRepository.findById(scheduleId).orElse(null);
        if (schedule != null) {
            if (shiftId != null) {
                Shift shift = shiftRepository.findById(shiftId).orElse(null);
                if (shift != null) {
                    schedule.setShift(shift);
                }
            }
            if (isDayOff != null) {
                schedule.setIsDayOff(isDayOff);
            }
            workScheduleRepository.save(schedule);
        }
    }

    public void deleteSchedule(Long scheduleId) {
        workScheduleRepository.deleteById(scheduleId);
    }

    public List<EmployeeScheduleStatsDto> getEmployeeStats(LocalDate startDate, LocalDate endDate) {
        return workScheduleRepository.getEmployeeStats(startDate, endDate);
    }

    public ScheduleSettingsDto getSettings(Long shiftId) {
        ScheduleSettings settings = scheduleSettingsRepository.findByShiftId(shiftId).orElse(null);
        if (settings != null) {
            return new ScheduleSettingsDto(settings.getShift().getId(), settings.getShift().getName(), 
                    settings.getWaitersCount(), settings.getCooksCount());
        }
        return null;
    }

    public void saveSettings(Long shiftId, Integer waitersCount, Integer cooksCount) {
        ScheduleSettings settings = scheduleSettingsRepository.findByShiftId(shiftId)
                .orElse(new ScheduleSettings());
        
        if (settings.getId() == null) {
            Shift shift = shiftRepository.findById(shiftId).orElse(null);
            settings.setShift(shift);
        }
        
        settings.setWaitersCount(waitersCount);
        settings.setCooksCount(cooksCount);
        scheduleSettingsRepository.save(settings);
    }
}

