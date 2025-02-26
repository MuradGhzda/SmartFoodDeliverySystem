package org.example.dto.order;

import lombok.Data;
import org.example.dto.address.AddressResponse;
import org.example.dto.restaurant.RestaurantResponse;
import org.example.enums.OrderStatus;
import org.example.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
@Data
public class OrderResponse {
    private Long id;
    private RestaurantResponse restaurant;
    private AddressResponse address;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private BigDecimal deliveryFee;
    private PaymentMethod paymentMethod;
    private String specialInstructions;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private List<OrderItemResponse> items;
}