package org.example.dto.restaurant;

import lombok.Data;
import org.example.dto.restaurant.MenuItemOptionResponse;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private boolean isVegetarian;
    private boolean isVegan;
    private boolean isSpicy;
    private List<MenuItemOptionResponse> options;
}
