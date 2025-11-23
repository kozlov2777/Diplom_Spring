package com.example.demo.services;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.PopularDishDto;
import com.example.demo.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final OrderRepository orderRepository;

    public AnalyticsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<PopularDishDto> getTopDishes(LocalDateTime start, LocalDateTime end, int limit) {
        // Спрощена версія - в реальності треба окремий запит
        List<OrderDto> orders = orderRepository.getOrdersByDate(start, end);
        // Тут має бути логіка підрахунку популярності страв
        return new ArrayList<>();
    }

    public Map<String, Double> getDailyRevenue(LocalDate start, LocalDate end) {
        Map<String, Double> revenue = new LinkedHashMap<>();
        LocalDate current = start;
        
        while (!current.isAfter(end)) {
            LocalDateTime dayStart = current.atStartOfDay();
            LocalDateTime dayEnd = current.atTime(23, 59, 59);
            
            List<OrderDto> orders = orderRepository.getOrdersByDate(dayStart, dayEnd);
            double total = orders.stream().mapToDouble(OrderDto::getTotal).sum();
            
            revenue.put(current.toString(), Math.round(total * 100.0) / 100.0);
            current = current.plusDays(1);
        }
        
        return revenue;
    }

    public Double getAverageCheck(LocalDateTime start, LocalDateTime end) {
        List<OrderDto> orders = orderRepository.getOrdersByDate(start, end);
        if (orders.isEmpty()) return 0.0;
        
        double total = orders.stream().mapToDouble(OrderDto::getTotal).sum();
        return Math.round((total / orders.size()) * 100.0) / 100.0;
    }
}

