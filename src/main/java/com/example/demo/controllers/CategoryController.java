package com.example.demo.controllers;

import com.example.demo.dto.CategoryDto;
import com.example.demo.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/category")
    public String getEmployeeSalaries(Model model) {
        List<CategoryDto> category = categoryService.getCategories();
        model.addAttribute("category", category);
        return "category_list";
    }
}
