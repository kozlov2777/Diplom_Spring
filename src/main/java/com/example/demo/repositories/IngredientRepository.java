package com.example.demo.repositories;

import com.example.demo.dto.IngredientDto;
import com.example.demo.models.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredients, Long> {
    @Query("SELECT new com.example.demo.dto.IngredientDto(i.id, i.name, i.quantity, i.unit, i.minQuantity, " +
            "CASE WHEN i.quantity <= i.minQuantity THEN true ELSE false END) " +
            "FROM Ingredients i ORDER BY i.name")
    List<IngredientDto> getAllIngredientsWithStatus();

    @Query("SELECT COUNT(i) FROM Ingredients i WHERE i.quantity <= i.minQuantity")
    Integer countLowStock();
}

