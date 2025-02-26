package org.example.model.cart;

import jakarta.persistence.*;
import lombok.Data;
import org.example.model.cart.Cart;
import org.example.model.restaurant.MenuItem;
import org.example.model.restaurant.MenuItemOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private Integer quantity;
    private String specialInstructions;

    @ManyToMany
    @JoinTable(
            name = "cart_item_options",
            joinColumns = @JoinColumn(name = "cart_item_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_option_id")
    )
    private List<MenuItemOption> selectedOptions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public List<MenuItemOption> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<MenuItemOption> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

}