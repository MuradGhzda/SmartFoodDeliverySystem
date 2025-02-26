package org.example.dto.restaurant;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemOptionResponse {
    private Long id;
    private String name;
    private BigDecimal priceAddition;
}
