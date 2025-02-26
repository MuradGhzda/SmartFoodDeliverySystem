package org.example.dto.cart;

import lombok.Data;
import org.example.dto.cart.CartItemResponse;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
    private BigDecimal deliveryFee;
    private BigDecimal minimumOrder;
}