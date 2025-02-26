package org.example.dto.order;

import lombok.Data;
import org.example.enums.PaymentMethod;

@Data
public class OrderRequest {
    private Long addressId;
    private String specialInstructions;
    private PaymentMethod paymentMethod;

    // No-args constructor
    public OrderRequest() {}
}