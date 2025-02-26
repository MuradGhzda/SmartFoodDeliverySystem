package org.example.service.order.impl;


import org.example.dto.order.OrderRequest;
import org.example.enums.OrderStatus;
import org.example.exception.ResourceNotFoundException;
import org.example.model.cart.Cart;
import org.example.model.cart.CartItem;
import org.example.model.order.Order;
import org.example.model.order.OrderItem;
import org.example.model.user.Address;
import org.example.model.user.User;
import org.example.repository.user.AddressRepository;
import org.example.repository.order.OrderRepository;
import org.example.service.cart.impl.CartServiceImpl;
import org.example.service.order.OrderService;
import org.example.service.user.impl.UserServiceImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@EnableScheduling
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserServiceImpl userServiceImpl;
    private final CartServiceImpl cartServiceImpl;
    private final AddressRepository addressRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserServiceImpl userServiceImpl,
                            CartServiceImpl cartServiceImpl,
                            AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userServiceImpl = userServiceImpl;
        this.cartServiceImpl = cartServiceImpl;
        this.addressRepository = addressRepository;
    }

    public Order createOrder(OrderRequest orderRequest) {
        User currentUser = userServiceImpl.getCurrentUser();
        Cart cart = cartServiceImpl.getCurrentCart();

        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Order order = new Order();
        order.setUser(currentUser);
        order.setAddress(address);
        order.setItems(convertCartItemsToOrderItems(cart.getItems()));
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderTime(LocalDateTime.now());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setSpecialInstructions(orderRequest.getSpecialInstructions());

        if (!cart.getItems().isEmpty()) {
            order.setRestaurant(cart.getItems().get(0).getMenuItem().getCategory().getRestaurant());
        }

        Order savedOrder = orderRepository.save(order);
        cartServiceImpl.clearCart();
        return savedOrder;
    }

    private List<OrderItem> convertCartItemsToOrderItems(List<CartItem> cartItems) {
        return cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(cartItem.getMenuItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getMenuItem().getPrice());
            orderItem.setSpecialInstructions(cartItem.getSpecialInstructions());
            orderItem.setSelectedOptions(new ArrayList<>(cartItem.getSelectedOptions()));
            return orderItem;
        }).collect(Collectors.toList());
    }

    public List<Order> getCurrentUserOrders() {
        User currentUser = userServiceImpl.getCurrentUser();
        // Changed to use the updated repository method
        return orderRepository.findByUserOrderByOrderTimeDesc(currentUser);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public long getTodayOrderCount() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.now().with(LocalTime.MAX);
        return orderRepository.countByOrderTimeBetween(startOfDay, endOfDay);
    }

    public BigDecimal calculateTotalRevenue() {
        return orderRepository.findAll().stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.COMPLETED &&
                order.getStatus() != OrderStatus.CANCELLED) {
            throw new IllegalStateException("Can only delete completed or cancelled orders");
        }

        orderRepository.delete(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Durumu güncelle
        order.setStatus(status);

        // COMPLETED durumuna geçtiyse teslim zamanını kaydet
        if (status == OrderStatus.COMPLETED) {
            order.setActualDeliveryTime(LocalDateTime.now());
        }

        orderRepository.save(order);
    }

//    @Scheduled(fixedRate = 300000) // 5 dakikada bir çalışır (5 * 60 * 1000 ms)
//    public void autoUpdateOrderStatus() {
//        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30); // 30 dakika öncesi
//        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
//
//        for (Order order : pendingOrders) {
//            if (order.getOrderTime().isBefore(threshold)) {
//                order.setStatus(OrderStatus.PREPARING);
//                orderRepository.save(order);
//            }
//        }
//
//        List<Order> preparingOrders = orderRepository.findByStatus(OrderStatus.PREPARING);
//        for (Order order : preparingOrders) {
//            if (order.getOrderTime().isBefore(threshold.minusMinutes(30))) { // 60 dakika oldu
//                order.setStatus(OrderStatus.ON_THE_WAY);
//                orderRepository.save(order);
//            }
//        }
//
//        List<Order> onTheWayOrders = orderRepository.findByStatus(OrderStatus.ON_THE_WAY);
//        for (Order order : onTheWayOrders) {
//            if (order.getOrderTime().isBefore(threshold.minusMinutes(60))) { // 90 dakika oldu
//                order.setStatus(OrderStatus.DELIVERED);
//                orderRepository.save(order);
//            }
//        }
//    }
}