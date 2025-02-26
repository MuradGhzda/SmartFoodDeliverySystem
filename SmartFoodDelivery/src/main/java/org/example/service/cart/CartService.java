package org.example.service.cart;

import org.example.dto.cart.CartItemRequest;
import org.example.model.cart.Cart;
import org.example.model.cart.CartItem;

public interface CartService {
    void addItem(Long menuItemId, Integer quantity);
    Cart removeItem(Long itemId);
    void clearCart();
    Cart getCurrentCart();
    void increaseQuantity(Long itemId);
    void decreaseQuantity(Long itemId);
    CartItem convertRequestToCartItem(CartItemRequest request);
}