package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;
import jakarta.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Tables table;
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private Set<Menu_Items> items;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Statuses status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employees employee;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<Order_Items> orderItems;
}
