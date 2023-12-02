package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.MenuItemService;
import com.example.demo.services.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final TableService tableService;
    private final EmployeeService employeeService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService, TableService tableService, EmployeeService employeeService) {
        this.menuItemService = menuItemService;
        this.tableService = tableService;
        this.employeeService = employeeService;
    }

    @GetMapping("/menu")
    public String getMenuItems(Model model) {
        List<MenuItemDto> menuItems = menuItemService.getMenuItems();
        model.addAttribute("menu", menuItems);
        return "menu";
    }

    @GetMapping("/item_detail/{order_id}")
    public String item_detail(@PathVariable("order_id") Long orderId, Model model) {
        List <ItemDetailDto> orderDetailList = menuItemService.getItemsDetail(orderId);
        model.addAttribute("orderDetailList", orderDetailList);
        return "item_detail";
    }

    @GetMapping("/new_order")
    public String get_new_order(Model model) {
        List <ItemsForCreateDTO>  itemsDetailForCreate = menuItemService.getItemsDetailForCreate();
        List<Long> get_free_tables = tableService.getFreeTable();
        List<EmployeeListDto> employeeList = employeeService.getEmployeeList();
        List<StatusesDto> statusesList = menuItemService.statusesList();
        model.addAttribute("itemsDetailForCreate", itemsDetailForCreate);
        model.addAttribute("get_free_tables", get_free_tables);
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("statusesList", statusesList);
        return "new_order";
    }
}
