package com.example.demo.services;

import com.example.demo.dto.OrderByStatusDto;
import com.example.demo.dto.OrderDetailByTableDto;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderDto;

import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.models.Orders;
import com.example.demo.repositories.OrderRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderService(OrderRepository orderRepository, SimpMessagingTemplate messagingTemplate) {
        this.orderRepository = orderRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<OrderDto> getOrders() {
        return orderRepository.getOrders();
    }

    public List<OrderDetailDto> getOrderById(Long id){
        return orderRepository.getOrderDetails(id);
    }

    public List<OrderDetailByTableDto> getOrderDetailsByTableNumber(Long tableNumber) {
        return orderRepository.getOrderDetailsByTableNumber(tableNumber);
    }

    public List<OrderByStatusDto> getOrdersByStatus(long l) {
        return orderRepository.getOrdersByStatus(l);
    }

    public void updateOrderStatus(Long status_id, Long orderId) {
        orderRepository.updateOrderStatus(status_id, orderId);

        // Якщо замовлення стало "готовим" (3) – сповістимо офіціантів
        if (status_id == 3L) {
            messagingTemplate.convertAndSend("/topic/orders/ready", orderId);
        }
    }

    public void updateTableStatus(Long table_status_id, Long orderId) {
        orderRepository.updateTableStatus(table_status_id, orderId);
    }

    public void save(Orders orders){
        orderRepository.save(orders);

        // Нове замовлення – сповіщаємо кухарів
        messagingTemplate.convertAndSend("/topic/orders/new", orders.getId());
    }

    public List<OrderDto> getOrdersByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.getOrdersByDate(startDate, endDate);
    }

    public void updateIngredientsAfterOrderCreation(Long id, int count) {
        orderRepository.updateQuantityInIngredient(id, count);
    }
}
