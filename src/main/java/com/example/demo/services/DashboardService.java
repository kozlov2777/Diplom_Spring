package com.example.demo.services;

import com.example.demo.dto.*;
import com.example.demo.repositories.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final ShiftChangeRequestRepository shiftChangeRequestRepository;
    private final IngredientRepository ingredientRepository;

    public DashboardService(OrderRepository orderRepository,
                           ReviewRepository reviewRepository,
                           WorkScheduleRepository workScheduleRepository,
                           ShiftChangeRequestRepository shiftChangeRequestRepository,
                           IngredientRepository ingredientRepository) {
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
        this.workScheduleRepository = workScheduleRepository;
        this.shiftChangeRequestRepository = shiftChangeRequestRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public DashboardDto getDashboardData() {
        DashboardDto dashboard = new DashboardDto();
        
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        LocalDate monthStart = today.withDayOfMonth(1);
        
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(23, 59, 59);
        LocalDateTime weekStartTime = weekStart.atStartOfDay();
        LocalDateTime monthStartTime = monthStart.atStartOfDay();
        
        // 1. Фінансові метрики
        dashboard.setTodayRevenue(calculateRevenue(todayStart, todayEnd));
        dashboard.setWeekRevenue(calculateRevenue(weekStartTime, todayEnd));
        dashboard.setMonthRevenue(calculateRevenue(monthStartTime, todayEnd));
        
        // Зміна відносно минулого тижня
        LocalDateTime prevWeekStart = weekStart.minusDays(7).atStartOfDay();
        LocalDateTime prevWeekEnd = weekStart.minusDays(1).atTime(23, 59, 59);
        Double prevWeekRevenue = calculateRevenue(prevWeekStart, prevWeekEnd);
        if (prevWeekRevenue > 0) {
            dashboard.setRevenueChange(((dashboard.getWeekRevenue() - prevWeekRevenue) / prevWeekRevenue) * 100);
        } else {
            dashboard.setRevenueChange(0.0);
        }
        
        // 2. Замовлення
        dashboard.setTodayOrders(countOrders(todayStart, todayEnd));
        dashboard.setWeekOrders(countOrders(weekStartTime, todayEnd));
        dashboard.setMonthOrders(countOrders(monthStartTime, todayEnd));
        dashboard.setActiveOrders(countActiveOrders());
        
        // 3. Відгуки
        Map<String, Object> todayReviews = getReviewStats(todayStart, todayEnd);
        dashboard.setTodayAverageRating(todayReviews.get("avgRating") != null ? (Double) todayReviews.get("avgRating") : 0.0);
        dashboard.setTodayReviewsCount(todayReviews.get("count") != null ? (Long) todayReviews.get("count") : 0L);
        
        Map<String, Object> weekReviews = getReviewStats(weekStartTime, todayEnd);
        dashboard.setWeekAverageRating(weekReviews.get("avgRating") != null ? (Double) weekReviews.get("avgRating") : 0.0);
        
        // 4. Поточна зміна
        dashboard.setCurrentShift(getCurrentShift());
        
        // 5. Топ працівник тижня
        List<EmployeeScheduleStatsDto> stats = workScheduleRepository.getEmployeeStats(weekStart, today);
        if (!stats.isEmpty()) {
            dashboard.setTopEmployee(stats.get(0));
        }
        
        // 6. Топ-5 страв
        dashboard.setTopDishes(getTopDishes(weekStartTime, todayEnd));
        
        // 7. Сповіщення
        dashboard.setLowStockCount(ingredientRepository.countLowStock());
        dashboard.setPendingRequestsCount(shiftChangeRequestRepository.getPendingRequests().size());
        
        // 8. Графіки
        dashboard.setWeeklyRevenueChart(getWeeklyRevenueChart(weekStart, today));
        dashboard.setHourlyOrdersChart(getHourlyOrdersChart(today));
        
        return dashboard;
    }

    private Double calculateRevenue(LocalDateTime start, LocalDateTime end) {
        try {
            List<OrderDto> orders = orderRepository.getOrdersByDate(start, end);
            return orders.stream()
                    .mapToDouble(OrderDto::getTotal)
                    .sum();
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Long countOrders(LocalDateTime start, LocalDateTime end) {
        try {
            return (long) orderRepository.getOrdersByDate(start, end).size();
        } catch (Exception e) {
            return 0L;
        }
    }

    private Long countActiveOrders() {
        try {
            // Замовлення зі статусом 1 або 2 (NEW, IN_PROGRESS)
            return (long) (orderRepository.getOrdersByStatus(1L).size() + 
                          orderRepository.getOrdersByStatus(2L).size());
        } catch (Exception e) {
            return 0L;
        }
    }

    private Map<String, Object> getReviewStats(LocalDateTime start, LocalDateTime end) {
        try {
            List<ReviewDto> reviews = reviewRepository.getAllReviews().stream()
                    .filter(r -> r.getCreatedAt().isAfter(start) && r.getCreatedAt().isBefore(end))
                    .collect(Collectors.toList());
            
            Map<String, Object> stats = new HashMap<>();
            if (!reviews.isEmpty()) {
                double avgRating = reviews.stream()
                        .mapToInt(ReviewDto::getRating)
                        .average()
                        .orElse(0.0);
                stats.put("avgRating", Math.round(avgRating * 10.0) / 10.0);
                stats.put("count", (long) reviews.size());
            } else {
                stats.put("avgRating", 0.0);
                stats.put("count", 0L);
            }
            return stats;
        } catch (Exception e) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("avgRating", 0.0);
            stats.put("count", 0L);
            return stats;
        }
    }

    private List<EmployeeOnShiftDto> getCurrentShift() {
        try {
            LocalDate today = LocalDate.now();
            List<ScheduleDto> schedules = workScheduleRepository.getScheduleByDateRange(today, today);
            
            return schedules.stream()
                    .filter(s -> !s.getIsDayOff())
                    .map(s -> new EmployeeOnShiftDto(
                            s.getFirstName(),
                            s.getLastName(),
                            "Працівник", // Можна додати роль якщо треба
                            s.getShiftName()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<PopularDishDto> getTopDishes(LocalDateTime start, LocalDateTime end) {
        // Використаємо існуючий запит для отримання топ страв
        // Це спрощена версія - в реальності треба окремий запит
        return new ArrayList<>(); // Буде реалізовано нижче в аналітиці
    }

    private Map<String, Double> getWeeklyRevenueChart(LocalDate start, LocalDate end) {
        Map<String, Double> chart = new LinkedHashMap<>();
        LocalDate current = start;
        
        while (!current.isAfter(end)) {
            LocalDateTime dayStart = current.atStartOfDay();
            LocalDateTime dayEnd = current.atTime(23, 59, 59);
            Double revenue = calculateRevenue(dayStart, dayEnd);
            
            String dayName = current.getDayOfWeek()
                    .getDisplayName(TextStyle.SHORT, new Locale("uk"));
            chart.put(dayName, Math.round(revenue * 100.0) / 100.0);
            
            current = current.plusDays(1);
        }
        
        return chart;
    }

    private Map<String, Long> getHourlyOrdersChart(LocalDate date) {
        Map<String, Long> chart = new LinkedHashMap<>();
        
        try {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);
            List<OrderDto> orders = orderRepository.getOrdersByDate(start, end);
            
            // Групуємо по годинах
            Map<Integer, Long> hourlyCount = orders.stream()
                    .collect(Collectors.groupingBy(
                            o -> o.getCreatedAt().getHour(),
                            Collectors.counting()
                    ));
            
            // Заповнюємо всі години (8:00 - 23:00)
            for (int hour = 8; hour <= 23; hour++) {
                String hourLabel = String.format("%02d:00", hour);
                chart.put(hourLabel, hourlyCount.getOrDefault(hour, 0L));
            }
        } catch (Exception e) {
            // Якщо помилка, повертаємо порожній графік
            for (int hour = 8; hour <= 23; hour++) {
                chart.put(String.format("%02d:00", hour), 0L);
            }
        }
        
        return chart;
    }
}

