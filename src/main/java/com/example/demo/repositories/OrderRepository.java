package com.example.demo.repositories;


import com.example.demo.dto.OrderByStatusDto;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrderDetailByTableDto;
import com.example.demo.models.Orders;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("select new com.example.demo.dto.OrderDto(o.id, o.table.id, o.createdAt, sum(oi.quantity*m.price)) from Orders o join Order_Items oi on o.id = oi.order.id join Menu_Items m on oi.item.id=m.id group by o.id order by o.createdAt desc ")
    List<OrderDto> getOrders();

    @Query("SELECT new com.example.demo.dto.OrderDetailDto(oi.id, m.name, oi.quantity, m.price, o.createdAt, e.lastName, o.table.id, s.name) " +
            "FROM Order_Items oi " +
            "JOIN Menu_Items m ON oi.item.id = m.id " +
            "JOIN Orders o ON oi.order.id = o.id " +
            "JOIN Employees e ON o.employee.id = e.id " +
            "JOIN Statuses s ON o.status.id = s.id " +
            "WHERE oi.order.id = :orderId")
    List<OrderDetailDto> getOrderDetails(Long orderId);

    @Query("SELECT new com.example.demo.dto.OrderDetailByTableDto(o.id, m.name, oi.quantity, o.createdAt, SUM(m.price * oi.quantity)) " +
            "FROM Orders o " +
            "JOIN Order_Items oi ON o.id = oi.order.id " +
            "JOIN Menu_Items m ON oi.item.id = m.id " +
            "JOIN Tables t ON t.id = o.table.id " +
            "WHERE t.id = :tableNumber " +
            "GROUP BY o.id, m.id " +
            "ORDER BY o.createdAt DESC")
    List<OrderDetailByTableDto> getOrderDetailsByTableNumber(Long tableNumber);

    @Query("SELECT new com.example.demo.dto.OrderByStatusDto(o.id, o.table.id, m.name, oi.quantity, o.createdAt, SUM(m.price * oi.quantity), s.name, e.firstName) " +
            "FROM Orders o " +
            "JOIN Order_Items oi ON o.id = oi.order.id " +
            "JOIN Menu_Items m ON oi.item.id = m.id " +
            "JOIN Employees e ON o.employee.id = e.id " +
            "JOIN Statuses s ON o.status.id = s.id " +
            "WHERE o.status.id = :statusId " +
            "GROUP BY o.id, m.id " +
            "ORDER BY o.createdAt DESC")
    List<OrderByStatusDto> getOrdersByStatus(Long statusId);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.status.id = ?1 WHERE o.id = ?2")
    void updateOrderStatus(Long status_id, Long orderId);



    @Transactional
    @Modifying
    @Query("UPDATE Tables t SET t.tableStatus.id = ?1 WHERE t.id = (SELECT o.table.id FROM Orders o WHERE o.id = ?2)")
    void updateTableStatus(Long table_status_id, Long orderId);


    @Query("SELECT new com.example.demo.dto.OrderDto(o.id, o.table.id, o.createdAt, sum(oi.quantity*m.price)) from Orders o join Order_Items oi on o.id = oi.order.id join Menu_Items m on oi.item.id=m.id where o.createdAt between :startDate and :endDate group by o.id order by o.createdAt desc ")
    List<OrderDto> getOrdersByDate(LocalDateTime startDate, LocalDateTime endDate);










}
