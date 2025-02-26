package org.example.service.order;

import org.example.dto.order.OrderRequest;
import org.example.enums.OrderStatus;
import org.example.model.order.Order;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);
    List<Order> getCurrentUserOrders();
    Order getOrderById(Long id);
    long getTodayOrderCount();
    BigDecimal calculateTotalRevenue();

    void deleteOrder(Long orderId);
    List<Order> getAllOrders();

    @Transactional
    void updateOrderStatus(Long orderId, OrderStatus status);

}