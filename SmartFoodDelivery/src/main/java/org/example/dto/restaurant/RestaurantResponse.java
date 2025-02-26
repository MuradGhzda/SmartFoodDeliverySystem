package org.example.dto.restaurant;

import lombok.Data;
import org.example.dto.restaurant.MenuCategoryResponse;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RestaurantResponse {
    private Long id;
    private String name;
    private String description;
    private String cuisineType;
    private Double rating;
    private BigDecimal minimumOrder;
    private BigDecimal deliveryFee;
    private boolean isOpen;
    private List<MenuCategoryResponse> categories;
}
