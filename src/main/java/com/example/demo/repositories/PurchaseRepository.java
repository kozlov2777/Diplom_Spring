package com.example.demo.repositories;

import com.example.demo.dto.PurchaseDto;
import com.example.demo.models.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchases, Long> {
    @Query("SELECT new com.example.demo.dto.PurchaseDto(p.id, p.ingredient.name, p.quantity, p.ingredient.unit, " +
            "p.price, p.purchasedAt, p.purchasedBy.firstName, p.purchasedBy.lastName) " +
            "FROM Purchases p WHERE p.purchasedAt BETWEEN :startDate AND :endDate " +
            "ORDER BY p.purchasedAt DESC")
    List<PurchaseDto> getPurchasesByPeriod(LocalDateTime startDate, LocalDateTime endDate);
}

