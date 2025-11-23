package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    // Фінансові метрики
    private Double todayRevenue;
    private Double weekRevenue;
    private Double monthRevenue;
    private Double revenueChange; // % зміни від минулого періоду
    
    // Замовлення
    private Long todayOrders;
    private Long weekOrders;
    private Long monthOrders;
    private Long activeOrders; // Активні зараз
    
    // Відгуки
    private Double todayAverageRating;
    private Long todayReviewsCount;
    private Double weekAverageRating;
    
    // Працівники
    private List<EmployeeOnShiftDto> currentShift;
    private EmployeeScheduleStatsDto topEmployee;
    
    // Популярні страви (топ-5)
    private List<PopularDishDto> topDishes;
    
    // Сповіщення
    private Integer lowStockCount; // К-ть продуктів з низьким запасом
    private Integer pendingRequestsCount; // К-ть запитів на зміну графіку
    
    // Графіки для charts
    private Map<String, Double> weeklyRevenueChart; // День -> Виручка
    private Map<String, Long> hourlyOrdersChart; // Година -> К-ть замовлень
}

