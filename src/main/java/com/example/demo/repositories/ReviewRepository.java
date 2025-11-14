package com.example.demo.repositories;

import com.example.demo.dto.ReviewDto;
import com.example.demo.models.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {
    @Query("SELECT new com.example.demo.dto.ReviewDto(r.id, r.order.id, r.rating, r.comment, r.customerName, r.createdAt) " +
            "FROM Reviews r ORDER BY r.createdAt DESC")
    List<ReviewDto> getAllReviews();

    @Query("SELECT COUNT(r) > 0 FROM Reviews r WHERE r.order.id = :orderId")
    boolean existsByOrderId(Long orderId);

    @Query("SELECT AVG(r.rating) as avgRating, COUNT(r) as count FROM Reviews r " +
            "JOIN Orders o ON r.order.id = o.id " +
            "WHERE o.employee.id = :employeeId AND r.createdAt BETWEEN :startDate AND :endDate")
    java.util.Map<String, Object> getAverageRatingForEmployee(Long employeeId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}

