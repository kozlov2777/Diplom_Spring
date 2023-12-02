package com.example.demo.repositories;

import com.example.demo.dto.CategoryDto;
import com.example.demo.models.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Categories, Long> {
    @Query("SELECT new com.example.demo.dto.CategoryDto(c.id, c.name, mi.name, mi.description, mi.price)"+
    "FROM Categories c JOIN Menu_Items mi ON mi.category.id = c.id")
    List<CategoryDto> getCategories();
}
