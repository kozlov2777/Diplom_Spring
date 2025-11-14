package com.example.demo.services;

import com.example.demo.dto.SalaryDetailDto;
import com.example.demo.dto.SalarySettingsDto;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SalaryService {
    private final EmployeeRepository employeeRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final SalarySettingsRepository salarySettingsRepository;
    private final SalaryCalculationRepository salaryCalculationRepository;
    private final RoleRepository roleRepository;

    public SalaryService(EmployeeRepository employeeRepository,
                        WorkScheduleRepository workScheduleRepository,
                        OrderRepository orderRepository,
                        ReviewRepository reviewRepository,
                        SalarySettingsRepository salarySettingsRepository,
                        SalaryCalculationRepository salaryCalculationRepository,
                        RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.salarySettingsRepository = salarySettingsRepository;
        this.salaryCalculationRepository = salaryCalculationRepository;
        this.roleRepository = roleRepository;
    }

    public List<SalaryDetailDto> calculateSalaries(LocalDate startDate, LocalDate endDate) {
        List<Employees> employees = employeeRepository.findAll().stream()
                .filter(e -> e.getRole().getId() == 3L || e.getRole().getId() == 4L)
                .toList();

        List<SalaryDetailDto> salaries = new ArrayList<>();

        for (Employees employee : employees) {
            SalaryDetailDto salary = calculateEmployeeSalary(employee, startDate, endDate);
            salaries.add(salary);
            
            // Зберігаємо розрахунок в історію
            saveSalaryCalculation(employee, salary, startDate, endDate);
        }

        salaries.sort((s1, s2) -> Double.compare(s2.getTotalSalary(), s1.getTotalSalary()));
        return salaries;
    }

    public SalaryDetailDto calculateEmployeeSalary(Employees employee, LocalDate startDate, LocalDate endDate) {
        SalaryDetailDto dto = new SalaryDetailDto();
        dto.setEmployeeId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setRoleName(employee.getRole().getName());

        // Отримуємо налаштування для ролі
        SalarySettings settings = salarySettingsRepository.findByRoleId(employee.getRole().getId())
                .orElse(getDefaultSettings(employee.getRole().getId()));

        dto.setHourlyRate(settings.getHourlyRate());
        dto.setOrderBonus(settings.getOrderBonus());
        dto.setReviewBonusCoefficient(settings.getReviewBonusCoefficient());
        dto.setAbsencePenalty(settings.getAbsencePenalty());

        // 1. Базова зарплата (години × ставка)
        List<WorkSchedule> schedules = workScheduleRepository.findByDateRange(startDate, endDate).stream()
                .filter(ws -> ws.getEmployee().getId().equals(employee.getId()))
                .toList();

        int hoursWorked = 0;
        int absences = 0;

        for (WorkSchedule schedule : schedules) {
            if (!schedule.getIsDayOff()) {
                hoursWorked += 8; // 8 годин на зміну
            } else {
                absences++;
            }
        }

        dto.setHoursWorked(hoursWorked);
        dto.setBaseSalary(hoursWorked * settings.getHourlyRate());

        // 2. Бонуси за замовлення
        Long ordersCount = orderRepository.countOrdersByEmployeeAndPeriod(employee.getId(), 
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        dto.setOrdersCompleted(ordersCount.intValue());
        dto.setOrderBonusTotal(ordersCount * settings.getOrderBonus());

        // 3. Премії за відгуки
        Map<String, Object> reviewStats = reviewRepository.getAverageRatingForEmployee(employee.getId(), 
                startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        
        Double avgRating = reviewStats.get("avgRating") != null ? (Double) reviewStats.get("avgRating") : 0.0;
        Long reviewsCount = reviewStats.get("count") != null ? (Long) reviewStats.get("count") : 0L;
        
        dto.setAverageRating(Math.round(avgRating * 10.0) / 10.0);
        dto.setReviewsCount(reviewsCount.intValue());
        dto.setReviewBonus(avgRating * settings.getReviewBonusCoefficient() * reviewsCount);

        // 4. Штрафи за прогули
        dto.setAbsences(absences);
        dto.setPenaltiesTotal(absences * settings.getAbsencePenalty());

        // 5. Підсумкова зарплата
        double total = dto.getBaseSalary() + dto.getOrderBonusTotal() + dto.getReviewBonus() - dto.getPenaltiesTotal();
        dto.setTotalSalary(Math.round(total * 100.0) / 100.0);

        // 6. Порівняння з минулим періодом
        LocalDate prevStart = startDate.minusMonths(1);
        LocalDate prevEnd = endDate.minusMonths(1);
        
        Optional<SalaryCalculation> prevCalc = salaryCalculationRepository
                .findByEmployeeAndPeriod(employee.getId(), prevStart, prevEnd);
        
        if (prevCalc.isPresent()) {
            double prevSalary = prevCalc.get().getTotalSalary();
            dto.setPreviousSalary(prevSalary);
            dto.setSalaryChange(dto.getTotalSalary() - prevSalary);
            dto.setSalaryChangePercent(prevSalary > 0 ? 
                    Math.round((dto.getSalaryChange() / prevSalary) * 100.0 * 10.0) / 10.0 : 0.0);
        }

        // 7. Визначення рівня продуктивності
        dto.setPerformanceLevel(calculatePerformanceLevel(dto));

        return dto;
    }

    private String calculatePerformanceLevel(SalaryDetailDto dto) {
        double efficiency = 0;
        
        if (dto.getHoursWorked() > 0) {
            efficiency = (dto.getOrdersCompleted() * 10.0 + dto.getAverageRating() * 5.0) / dto.getHoursWorked();
        }
        
        if (efficiency >= 2.0 && dto.getAverageRating() >= 4.5) {
            return "🏆 Майстер";
        } else if (efficiency >= 1.5 && dto.getAverageRating() >= 4.0) {
            return "💎 Професіонал";
        } else if (efficiency >= 1.0) {
            return "⭐ Досвідчений";
        } else {
            return "🌱 Новачок";
        }
    }

    private void saveSalaryCalculation(Employees employee, SalaryDetailDto dto, LocalDate startDate, LocalDate endDate) {
        Optional<SalaryCalculation> existing = salaryCalculationRepository
                .findByEmployeeAndPeriod(employee.getId(), startDate, endDate);
        
        SalaryCalculation calc = existing.orElse(new SalaryCalculation());
        calc.setEmployee(employee);
        calc.setPeriodStart(startDate);
        calc.setPeriodEnd(endDate);
        calc.setHoursWorked(dto.getHoursWorked());
        calc.setBaseSalary(dto.getBaseSalary());
        calc.setOrdersCompleted(dto.getOrdersCompleted());
        calc.setOrderBonus(dto.getOrderBonusTotal());
        calc.setAverageRating(dto.getAverageRating());
        calc.setReviewBonus(dto.getReviewBonus());
        calc.setAbsences(dto.getAbsences());
        calc.setPenalties(dto.getPenaltiesTotal());
        calc.setTotalSalary(dto.getTotalSalary());
        calc.setCalculatedAt(LocalDateTime.now());
        
        salaryCalculationRepository.save(calc);
    }

    private SalarySettings getDefaultSettings(Long roleId) {
        SalarySettings settings = new SalarySettings();
        Roles role = roleRepository.findById(roleId).orElse(null);
        settings.setRole(role);
        settings.setHourlyRate(100.0); // За замовчуванням 100 грн/год
        settings.setOrderBonus(10.0);   // 10 грн за замовлення
        settings.setReviewBonusCoefficient(20.0); // 20 грн за зірку за відгук
        settings.setAbsencePenalty(200.0); // 200 грн штраф
        return settings;
    }

    public List<SalarySettingsDto> getAllSettings() {
        List<SalarySettings> settings = salarySettingsRepository.findAll();
        List<SalarySettingsDto> dtos = new ArrayList<>();
        
        for (SalarySettings setting : settings) {
            dtos.add(new SalarySettingsDto(
                    setting.getId(),
                    setting.getRole().getId(),
                    setting.getRole().getName(),
                    setting.getHourlyRate(),
                    setting.getOrderBonus(),
                    setting.getReviewBonusCoefficient(),
                    setting.getAbsencePenalty()
            ));
        }
        
        return dtos;
    }

    public void saveSalarySettings(Long roleId, Double hourlyRate, Double orderBonus, 
                                   Double reviewBonus, Double absencePenalty) {
        SalarySettings settings = salarySettingsRepository.findByRoleId(roleId)
                .orElse(new SalarySettings());
        
        if (settings.getId() == null) {
            Roles role = roleRepository.findById(roleId).orElse(null);
            settings.setRole(role);
        }
        
        settings.setHourlyRate(hourlyRate);
        settings.setOrderBonus(orderBonus);
        settings.setReviewBonusCoefficient(reviewBonus);
        settings.setAbsencePenalty(absencePenalty);
        
        salarySettingsRepository.save(settings);
    }
}

