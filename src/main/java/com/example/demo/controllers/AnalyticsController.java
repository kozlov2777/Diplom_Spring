package com.example.demo.controllers;

import com.example.demo.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics")
    public String analytics(@RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate,
                           Model model) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusDays(30);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();
        
        Map<String, Double> dailyRevenue = analyticsService.getDailyRevenue(start, end);
        Double averageCheck = analyticsService.getAverageCheck(start.atStartOfDay(), end.atTime(23, 59, 59));
        
        model.addAttribute("dailyRevenue", dailyRevenue);
        model.addAttribute("averageCheck", averageCheck);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        
        return "analytics";
    }
}

