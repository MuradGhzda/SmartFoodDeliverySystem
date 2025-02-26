package org.example.model.restaurant;

import jakarta.persistence.*;
import lombok.Data;
import org.example.model.order.Order;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "cuisine_type")
    private String cuisineType;

    private Double rating;

    @Column(name = "delivery_time")
    private Integer deliveryTime;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee;

    @Column(name = "min_order_amount")
    private BigDecimal minOrderAmount;

    @Column(name = "is_open")
    private boolean isOpen;

    private String address;

    private String phone;

    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuCategory> menuCategories = new ArrayList<>();

    // Remove the direct menuItems relationship
    // Instead, we'll access menu items through categories
    public List<MenuItem> getMenuItems() {
        return menuCategories.stream()
                .flatMap(category -> category.getItems().stream())
                .collect(java.util.stream.Collectors.toList());
    }
}