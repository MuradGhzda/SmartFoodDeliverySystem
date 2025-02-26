package org.example.repository.order;

import org.example.enums.OrderStatus;
import org.example.model.order.Order;
import org.example.model.restaurant.Restaurant;
import org.example.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Changed createdAt to orderTime
    List<Order> findByUserOrderByOrderTimeDesc(User user);
    List<Order> findByRestaurantOrderByOrderTimeDesc(Restaurant restaurant);
    List<Order> findByStatus(OrderStatus status);
    Page<Order> findByUser(User user, Pageable pageable);

    long countByOrderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}