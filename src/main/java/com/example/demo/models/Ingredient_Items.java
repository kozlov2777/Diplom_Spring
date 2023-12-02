package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ingredient_items")
public class Ingredient_Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private Menu_Items item;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredients ingredient;
    private Integer quantity;
    private String unit;
}