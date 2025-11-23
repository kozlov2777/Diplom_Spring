package com.example.demo.controllers;

import com.example.demo.dto.EmployeeScheduleStatsDto;
import com.example.demo.dto.ScheduleDto;
import com.example.demo.dto.ScheduleSettingsDto;
import com.example.demo.models.Shift;
import com.example.demo.repositories.ShiftRepository;
import com.example.demo.services.ScheduleService;
import com.example.demo.services.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

@Controller
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ShiftRepository shiftRepository;
    private final ExcelExportService excelExportService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, ShiftRepository shiftRepository, ExcelExportService excelExportService) {
        this.scheduleService = scheduleService;
        this.shiftRepository = shiftRepository;
        this.excelExportService = excelExportService;
    }

    @GetMapping("/schedule")
    public String viewSchedule(@RequestParam(required = false) String weekStart, Model model) {
        LocalDate startDate;
        if (weekStart != null && !weekStart.isEmpty()) {
            startDate = LocalDate.parse(weekStart);
        } else {
            // Поточний понеділок
            startDate = LocalDate.now().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
        }

        LocalDate endDate = startDate.plusDays(6);
        List<ScheduleDto> scheduleList = scheduleService.getScheduleByWeek(startDate);
        List<Shift> shifts = shiftRepository.findAll();

        // Групуємо по даті та зміні
        Map<LocalDate, Map<Long, List<ScheduleDto>>> groupedSchedule = new TreeMap<>();
        for (ScheduleDto schedule : scheduleList) {
            groupedSchedule
                    .computeIfAbsent(schedule.getWorkDate(), k -> new TreeMap<>())
                    .computeIfAbsent(schedule.getShiftId(), k -> new ArrayList<>())
                    .add(schedule);
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("scheduleList", scheduleList);
        model.addAttribute("groupedSchedule", groupedSchedule);
        model.addAttribute("shifts", shifts);
        model.addAttribute("previousWeek", startDate.minusWeeks(1));
        model.addAttribute("nextWeek", startDate.plusWeeks(1));

        return "schedule_view";
    }

    @GetMapping("/schedule/settings")
    public String scheduleSettings(Model model) {
        List<Shift> shifts = shiftRepository.findAll();
        List<ScheduleSettingsDto> settingsList = new ArrayList<>();
        
        for (Shift shift : shifts) {
            ScheduleSettingsDto settings = scheduleService.getSettings(shift.getId());
            if (settings == null) {
                settings = new ScheduleSettingsDto(shift.getId(), shift.getName(), 2, 1);
            }
            settingsList.add(settings);
        }
        
        model.addAttribute("settingsList", settingsList);
        return "schedule_settings";
    }

    @PostMapping("/schedule/settings/save")
    public String saveSettings(@RequestParam Long shiftId, 
                              @RequestParam Integer waitersCount, 
                              @RequestParam Integer cooksCount) {
        scheduleService.saveSettings(shiftId, waitersCount, cooksCount);
        return "redirect:/schedule/settings";
    }

    @PostMapping("/schedule/generate")
    public String generateSchedule(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        scheduleService.generateSchedule(start, end);
        return "redirect:/schedule?weekStart=" + start;
    }

    @PostMapping("/schedule/update/{scheduleId}")
    public String updateSchedule(@PathVariable Long scheduleId,
                                 @RequestParam(required = false) Long shiftId,
                                 @RequestParam(required = false) Boolean isDayOff,
                                 @RequestParam String weekStart) {
        scheduleService.updateSchedule(scheduleId, shiftId, isDayOff);
        return "redirect:/schedule?weekStart=" + weekStart;
    }

    @PostMapping("/schedule/delete/{scheduleId}")
    public String deleteSchedule(@PathVariable Long scheduleId, @RequestParam String weekStart) {
        scheduleService.deleteSchedule(scheduleId);
        return "redirect:/schedule?weekStart=" + weekStart;
    }

    @GetMapping("/schedule/stats")
    public String scheduleStats(@RequestParam(required = false) String startDate,
                               @RequestParam(required = false) String endDate,
                               Model model) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().minusMonths(1);
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

        List<EmployeeScheduleStatsDto> stats = scheduleService.getEmployeeStats(start, end);
        
        model.addAttribute("stats", stats);
        model.addAttribute("startDate", start);
        model.addAttribute("endDate", end);
        
        return "schedule_stats";
    }

    @GetMapping("/schedule/export/excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam String startDate, @RequestParam String endDate) throws IOException {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        
        List<ScheduleDto> scheduleList = scheduleService.getScheduleByWeek(start);
        byte[] excelFile = excelExportService.exportScheduleToExcel(scheduleList, start, end);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "schedule_" + start + "_" + end + ".xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelFile);
    }
}

