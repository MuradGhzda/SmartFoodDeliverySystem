package org.example.dto.cart;

import lombok.Data;
import org.example.dto.restaurant.MenuItemOptionResponse;
import org.example.dto.restaurant.MenuItemResponse;

import java.util.List;

@Data
public class CartItemResponse {
    private Long id;
    private MenuItemResponse menuItem;
    private Integer quantity;
    private String specialInstructions;
    private List<MenuItemOptionResponse> selectedOptions;
}