package org.example.model.cart;

import jakarta.persistence.*;
import lombok.Data;
import org.example.model.restaurant.Restaurant;
import org.example.model.user.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "total_amount")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "subtotal")
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee = new BigDecimal("15.00");

    public void calculateTotal() {
        // Null safety and initialization
        if (items == null || items.isEmpty()) {
            this.subtotal = BigDecimal.ZERO;
            this.totalAmount = this.deliveryFee;
            return;
        }

        // Calculate subtotal with null checks and stream optimization
        this.subtotal = items.stream()
                .filter(item -> item != null &&
                        item.getMenuItem() != null &&
                        item.getMenuItem().getPrice() != null &&
                        item.getQuantity() != null)
                .map(item -> item.getMenuItem().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate total amount
        this.totalAmount = this.subtotal.add(
                Optional.ofNullable(this.deliveryFee)
                        .orElse(BigDecimal.ZERO)
        );
    }

    // Null-safe helper methods
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public BigDecimal getSubtotal() {
        calculateTotal(); // Ensure calculation is up to date
        return Optional.ofNullable(subtotal)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getDeliveryFee() {
        return Optional.ofNullable(deliveryFee)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getTotalAmount() {
        calculateTotal(); // Ensure calculation is up to date
        return Optional.ofNullable(totalAmount)
                .orElse(BigDecimal.ZERO);
    }

    // Restaurant compatibility check
    public boolean isFromSameRestaurant(Restaurant newRestaurant) {
        if (restaurant == null || newRestaurant == null) {
            return true; // Empty cart or no restaurant selected
        }
        return restaurant.getId().equals(newRestaurant.getId());
    }

    // Method to add item to cart
    public void addItem(CartItem cartItem) {
        if (cartItem == null) return;

        // Check restaurant compatibility
        if (restaurant == null) {
            restaurant = cartItem.getMenuItem().getRestaurant();
        } else if (!isFromSameRestaurant(cartItem.getMenuItem().getRestaurant())) {
            throw new IllegalArgumentException("Cannot add items from different restaurants");
        }

        // Check if item already exists
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getMenuItem().getId().equals(cartItem.getMenuItem().getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update existing item quantity
            CartItem existing = existingItem.get();
            existing.setQuantity(existing.getQuantity() + cartItem.getQuantity());
        } else {
            // Add new item
            cartItem.setCart(this);
            items.add(cartItem);
        }

        // Recalculate totals
        calculateTotal();
    }

    // Method to remove item from cart
    public void removeItem(Long cartItemId) {
        items.removeIf(item -> item.getId().equals(cartItemId));
        calculateTotal();
    }
}