package com.example.demo.controllers;

import com.example.demo.dto.IngredientDto;
import com.example.demo.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory")
    public String inventory(Model model) {
        List<IngredientDto> ingredients = inventoryService.getAllIngredients();
        long lowStockCount = ingredients.stream().filter(IngredientDto::isLowStock).count();
        
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("lowStockCount", lowStockCount);
        return "inventory";
    }

    @PostMapping("/inventory/update")
    public String updateIngredient(@RequestParam Long id,
                                   @RequestParam Double quantity,
                                   @RequestParam Double minQuantity) {
        inventoryService.updateIngredient(id, quantity, minQuantity);
        return "redirect:/inventory";
    }
}

