package com.example.demo.repositories;

import com.example.demo.dto.ItemDetailDto;
import com.example.demo.dto.ItemsForCreateDTO;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.dto.StatusesDto;
import com.example.demo.models.Menu_Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<Menu_Items, Long> {
    @Query("SELECT new com.example.demo.dto.MenuItemDto(m.id, m.name, m.description, m.price, m.category.name, " +
            "ROUND(SUM(i.calories * ii.quantity), 3)) " +
            "FROM Menu_Items m " +
            "JOIN Ingredient_Items ii ON ii.item.id = m.id " +
            "JOIN Ingredients i ON i.id = ii.ingredient.id " +
            "GROUP BY m.id, m.name, m.description, m.price, m.category.name")
    List<MenuItemDto> getMenuItems();

    @Query("SELECT new com.example.demo.dto.ItemDetailDto(mi.name, ii.quantity, i.name, ii.unit, i.calories) " +
            "FROM Menu_Items mi " +
            "JOIN Ingredient_Items ii ON ii.item.id = mi.id " +
            "JOIN Ingredients i ON i.id = ii.ingredient.id " +
            "WHERE mi.id = ?1")
    List<ItemDetailDto> getItemDetails(Long orderId);

    @Query("SELECT new com.example.demo.dto.ItemsForCreateDTO(m.id, m.name, m.description, m.price)FROM Menu_Items m ")
    List<ItemsForCreateDTO> getMenuItemsForCreate();

    @Query("SELECT new com.example.demo.dto.StatusesDto(s.id, s.name) FROM Statuses s")
    List<StatusesDto> getStatusesList();

}