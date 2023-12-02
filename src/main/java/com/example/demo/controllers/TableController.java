package com.example.demo.controllers;

import com.example.demo.dto.TableStatusDto;
import com.example.demo.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class TableController {
    private final TableService tableService;
    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/tables_status")
    public String getTablesStatus(Model model) {
        List<TableStatusDto> tables = tableService.getTableStatusDTO();
        model.addAttribute("tables", tables);
        return "table_status";
    }
}
