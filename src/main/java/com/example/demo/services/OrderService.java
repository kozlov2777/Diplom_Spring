package com.example.demo.services;

import com.example.demo.dto.OrderByStatusDto;
import com.example.demo.dto.OrderDetailByTableDto;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderDto;
import java.util.List;
import com.example.demo.models.Orders;
import com.example.demo.repositories.OrderRepository;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
    }

    public void updateTableStatus(Long table_status_id, Long orderId) {
        orderRepository.updateTableStatus(table_status_id, orderId);
    }

    public void save(Orders orders){
        orderRepository.save(orders);
    }
}
