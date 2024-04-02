package com.example.demo.controllers;

import com.example.demo.dto.OrderByStatusDto;
import com.example.demo.dto.OrderDetailByTableDto;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderDto;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import com.example.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final TableRepository tableRepository;
    private final EmployeeRepository employeeRepository;
    private final StatusRepository statusRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;
    @Autowired
    public OrderController(OrderService orderService, TableRepository tableRepository, EmployeeRepository employeeRepository, StatusRepository statusRepository, MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository) {
        this.orderService = orderService;
        this.tableRepository = tableRepository;
        this.employeeRepository = employeeRepository;
        this.statusRepository = statusRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping("/orders_view")
    public String viewOrders(Model model) {
        List<OrderDto> orderList = orderService.getOrders();
        model.addAttribute("orderList", orderList);
        return "order_list";
    }

    @GetMapping("/order_detail/{order_id}")
    public String viewOrderDetail(@PathVariable("order_id") Long orderId, Model model) {
        List<OrderDetailDto> orderDetailList = orderService.getOrderById(orderId);
        model.addAttribute("orderDetailList", orderDetailList);
        return "order_detail";
    }

    @GetMapping("/order_detail_by_table_number/{tableNumber}")
    public String getOrderDetailByTableNumber(@PathVariable Long tableNumber, Model model) {
        List<OrderDetailByTableDto> detailByTable = orderService.getOrderDetailsByTableNumber(tableNumber);
        model.addAttribute("detailByTable", detailByTable);
        return "order_detail_by_table_number";
    }

    @GetMapping("/")
    public String getOrdersByStatus(Model model) {
        List<OrderByStatusDto> orderStatus1 = orderService.getOrdersByStatus(1L);
        List<OrderByStatusDto> orderStatus2 = orderService.getOrdersByStatus(2L);

        model.addAttribute("orderStatus1", orderStatus1);
        model.addAttribute("orderStatus2", orderStatus2);

        return "order_by_status";
    }

    @RequestMapping("/update_order_status/{order_id}")
    public String updateOrderStatus(@PathVariable Long order_id) {
        orderService.updateOrderStatus(2L, order_id);
        return "redirect:/";
    }

    @RequestMapping("/update_order_status_and_table/{order_id}")
    public String updateOrderStatusAndTable(@PathVariable Long order_id) {
        orderService.updateOrderStatus(3L, order_id);
        orderService.updateTableStatus(1L,order_id);
        return "redirect:/";
    }

    @RequestMapping("/new_order")
    public String createNewOrder(@RequestParam("table_id_id") Long tableId,
                                 @RequestParam("employee_id") Long employeeId,
                                 @RequestParam("status_id") Long statusId,
                                 @RequestParam("item") Long itemId,
                                 @RequestParam("quantity") int quantity) {
        Tables table = tableRepository.findById(tableId).orElse(null);
        Employees employee = employeeRepository.findById(employeeId).orElse(null);
        Statuses status = statusRepository.findById(statusId).orElse(null);
        Menu_Items menuItem = menuItemRepository.findById(itemId).orElse(null);

        if (table != null && employee != null && status != null && menuItem != null) {
            Orders order = new Orders();
            order.setTable(table);
            order.setEmployee(employee);
            order.setStatus(status);
            order.setCreatedAt(LocalDateTime.now());

            Set<Menu_Items> items = new HashSet<>();
            for (int i = 0; i < quantity; i++) {
                items.add(menuItem);
            }
            order.setItems(items);

            orderService.save(order);
            orderService.updateTableStatus(2L, order.getId());
            Order_Items orderItems = new Order_Items();
            orderItems.setOrder(order);
            orderItems.setItem(menuItem);
            orderItems.setQuantity(quantity);
            orderItemRepository.save(orderItems);
            orderService.updateIngredientsAfterOrderCreation(menuItem.getId(), quantity);
            return "redirect:/";
        } else {
            return "redirect:/new_order";
        }
    }

    @GetMapping("/orders_by_date")
    public String getOrdersByDate(@RequestParam(name = "start_date", required = false) String startDateStr,
                                  @RequestParam(name = "end_date", required = false) String endDateStr,
                                  Model model) {
        if (startDateStr != null && endDateStr != null) {
            LocalDateTime startDate = LocalDateTime.parse(startDateStr + "T00:00:00");
            LocalDateTime endDate = LocalDateTime.parse(endDateStr + "T23:59:59");

            List<OrderDto> ordersByDate = orderService.getOrdersByDate(startDate, endDate);
            model.addAttribute("ordersByDate", ordersByDate);
        } else {
            return "orders_by_date";
        }
        return "orders_by_date";
    }




}