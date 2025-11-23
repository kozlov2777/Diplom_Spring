package com.example.demo.services;

import com.example.demo.dto.IngredientDto;
import com.example.demo.models.Ingredients;
import com.example.demo.repositories.IngredientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService {
    private final IngredientRepository ingredientRepository;

    public InventoryService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<IngredientDto> getAllIngredients() {
        return ingredientRepository.getAllIngredientsWithStatus();
    }

    public Integer getLowStockCount() {
        return ingredientRepository.countLowStock();
    }

    public void updateIngredient(Long id, Double quantity, Double minQuantity) {
        Ingredients ingredient = ingredientRepository.findById(id).orElse(null);
        if (ingredient != null) {
            ingredient.setQuantity(quantity);
            ingredient.setMinQuantity(minQuantity);
            ingredientRepository.save(ingredient);
        }
    }
}

