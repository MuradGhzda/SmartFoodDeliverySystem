package org.example.controller.cart;

import org.example.dto.cart.CartItemRequest;
import org.example.dto.order.OrderRequest;
import org.example.enums.PaymentMethod;
import org.example.model.cart.Cart;
import org.example.model.order.Order;
import org.example.service.cart.impl.CartServiceImpl;
import org.example.service.order.impl.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {
    private final CartServiceImpl cartServiceImpl;
    private final OrderServiceImpl orderServiceImpl;

    public CartController(CartServiceImpl cartServiceImpl, OrderServiceImpl orderServiceImpl) {
        this.cartServiceImpl = cartServiceImpl;
        this.orderServiceImpl = orderServiceImpl;
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        try {
            Cart cart = cartServiceImpl.getCurrentCart();
            model.addAttribute("cart", cart);
            return "cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request) {
        try {
            System.out.println("Gelen istek: " + request.getMenuItemId() + ", " + request.getQuantity());
            cartServiceImpl.addItem(request.getMenuItemId(), request.getQuantity());
            return ResponseEntity.ok().body("Ürün başarıyla sepete eklendi");
        } catch (IllegalStateException e) {
            System.out.println("IllegalStateException: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Bir hata oluştu: " + e.getMessage());
        }
    }

    @PostMapping("/cart/checkout")
    public String checkout(@ModelAttribute OrderRequest orderRequest, RedirectAttributes redirectAttributes) {
        try {
            Cart cart = cartServiceImpl.getCurrentCart();
            if (cart.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Sepetiniz boş");
                return "redirect:/cart";
            }

            // PaymentMethod dönüşümü
            if (orderRequest.getPaymentMethod() == null) {
                orderRequest.setPaymentMethod(PaymentMethod.CASH); // Varsayılan değer
            }

            Order order = orderServiceImpl.createOrder(orderRequest);
            return "redirect:/orders";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Sipariş oluşturulurken bir hata oluştu: " + e.getMessage());
            return "redirect:/cart";
        }
    }
    @GetMapping("/cart/increase/{id}")
    public String increaseQuantity(@PathVariable Long id) {
        cartServiceImpl.increaseQuantity(id);
        return "redirect:/cart";
    }

    @GetMapping("/cart/decrease/{id}")
    public String decreaseQuantity(@PathVariable Long id) {
        cartServiceImpl.decreaseQuantity(id);
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{id}")
    public String removeItem(@PathVariable Long id) {
        cartServiceImpl.removeItem(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart() {
        cartServiceImpl.clearCart();
        return "redirect:/cart";
    }
}