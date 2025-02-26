package org.example.dto.order;

import lombok.Data;
import org.example.dto.restaurant.MenuItemOptionResponse;
import org.example.dto.restaurant.MenuItemResponse;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemResponse {
    private Long id;
    private MenuItemResponse menuItem;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String specialInstructions;
    private List<MenuItemOptionResponse> selectedOptions;
}
