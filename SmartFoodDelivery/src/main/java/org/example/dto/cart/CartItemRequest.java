package org.example.dto.cart;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long menuItemId;
    private Integer quantity;
    private String specialInstructions;

    // Lombok @Data annotation'ı getter/setter'ları otomatik oluşturacak
}