package com.example.demo.controllers;

import com.example.demo.dto.IngredientDto;
import com.example.demo.dto.PurchaseDto;
import com.example.demo.services.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
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
        model.addAttribute("ingredients", ingredients);
        return "inventory";
    }

    @PostMapping("/inventory/update")
    public String updateIngredient(@RequestParam Long id,
                                   @RequestParam Double quantity,
                                   @RequestParam Double minQuantity) {
        inventoryService.updateIngredient(id, quantity, minQuantity);
        return "redirect:/inventory";
    }

    @PostMapping("/inventory/purchase")
    public String addPurchase(@RequestParam Long ingredientId,
                             @RequestParam Double quantity,
                             @RequestParam Double price,
                             @RequestParam Long employeeId) {
        inventoryService.addPurchase(ingredientId, quantity, price, employeeId);
        return "redirect:/inventory";
    }

    @GetMapping("/inventory/purchases")
    public String purchaseHistory(@RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  Model model) {
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate + "T00:00:00") : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate + "T23:59:59") : LocalDateTime.now();
        
        List<PurchaseDto> purchases = inventoryService.getPurchaseHistory(start, end);
        model.addAttribute("purchases", purchases);
        model.addAttribute("startDate", start.toLocalDate());
        model.addAttribute("endDate", end.toLocalDate());
        
        return "purchase_history";
    }
}

