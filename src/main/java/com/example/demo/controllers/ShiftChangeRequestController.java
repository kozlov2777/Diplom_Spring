package com.example.demo.controllers;

import com.example.demo.dto.ShiftChangeRequestDto;
import com.example.demo.services.ShiftChangeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ShiftChangeRequestController {

    private final ShiftChangeRequestService shiftChangeRequestService;

    @Autowired
    public ShiftChangeRequestController(ShiftChangeRequestService shiftChangeRequestService) {
        this.shiftChangeRequestService = shiftChangeRequestService;
    }

    @GetMapping("/shift-change-requests")
    public String viewRequests(Model model) {
        List<ShiftChangeRequestDto> requests = shiftChangeRequestService.getPendingRequests();
        model.addAttribute("requests", requests);
        return "shift_change_requests";
    }

    @PostMapping("/shift-change-request/create")
    public String createRequest(@RequestParam Long scheduleId, 
                               @RequestParam Long employeeId,
                               @RequestParam String reason,
                               @RequestParam String weekStart) {
        shiftChangeRequestService.createRequest(scheduleId, employeeId, reason);
        return "redirect:/schedule?weekStart=" + weekStart;
    }

    @PostMapping("/shift-change-request/approve/{requestId}")
    public String approveRequest(@PathVariable Long requestId) {
        // В реальному додатку тут треба отримати ID адміна з сесії
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Для простоти передаємо 1L як adminId, але в production треба отримати з auth
        shiftChangeRequestService.approveRequest(requestId, 1L);
        return "redirect:/shift-change-requests";
    }

    @PostMapping("/shift-change-request/reject/{requestId}")
    public String rejectRequest(@PathVariable Long requestId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        shiftChangeRequestService.rejectRequest(requestId, 1L);
        return "redirect:/shift-change-requests";
    }
}

