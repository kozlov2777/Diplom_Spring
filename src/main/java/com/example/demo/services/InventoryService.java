package com.example.demo.services;

import com.example.demo.dto.IngredientDto;
import com.example.demo.dto.PurchaseDto;
import com.example.demo.models.Employees;
import com.example.demo.models.Ingredients;
import com.example.demo.models.Purchases;
import com.example.demo.repositories.EmployeeRepository;
import com.example.demo.repositories.IngredientRepository;
import com.example.demo.repositories.PurchaseRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {
    private final IngredientRepository ingredientRepository;
    private final PurchaseRepository purchaseRepository;
    private final EmployeeRepository employeeRepository;

    public InventoryService(IngredientRepository ingredientRepository,
                           PurchaseRepository purchaseRepository,
                           EmployeeRepository employeeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.purchaseRepository = purchaseRepository;
        this.employeeRepository = employeeRepository;
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

    public void addPurchase(Long ingredientId, Double quantity, Double price, Long employeeId) {
        Ingredients ingredient = ingredientRepository.findById(ingredientId).orElse(null);
        Employees employee = employeeRepository.findById(employeeId).orElse(null);
        
        if (ingredient != null && employee != null) {
            // Додаємо закупівлю
            Purchases purchase = new Purchases();
            purchase.setIngredient(ingredient);
            purchase.setQuantity(quantity);
            purchase.setPrice(price);
            purchase.setPurchasedAt(LocalDateTime.now());
            purchase.setPurchasedBy(employee);
            purchaseRepository.save(purchase);
            
            // Оновлюємо кількість інгредієнта
            ingredient.setQuantity(ingredient.getQuantity() + quantity);
            ingredientRepository.save(ingredient);
        }
    }

    public List<PurchaseDto> getPurchaseHistory(LocalDateTime startDate, LocalDateTime endDate) {
        return purchaseRepository.getPurchasesByPeriod(startDate, endDate);
    }
}

