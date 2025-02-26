package org.example.controller.order;

import org.example.model.order.Order;
import org.example.service.order.impl.OrderServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    @GetMapping
    public String getOrders(Model model) {
        List<Order> orders = orderServiceImpl.getCurrentUserOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        Order order = orderServiceImpl.getOrderById(id);
        model.addAttribute("order", order);
        return "order-detail";
    }
    @GetMapping("/orders")
    public String orderManagement(Model model) {
        List<Order> orders = orderServiceImpl.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";  // templates/orders.html
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderServiceImpl.deleteOrder(id);
        return "redirect:/admin/orders";
    }
}