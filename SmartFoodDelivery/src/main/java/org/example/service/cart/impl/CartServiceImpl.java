package org.example.service.cart.impl;

import org.example.dto.cart.CartItemRequest;
import org.example.exception.ResourceNotFoundException;
import org.example.model.cart.Cart;
import org.example.model.cart.CartItem;
import org.example.model.restaurant.MenuItem;
import org.example.model.user.User;
import org.example.repository.cart.CartRepository;
import org.example.repository.restaurant.MenuItemRepository;
import org.example.service.cart.CartService;
import org.example.service.restaurant.impl.MenuItemServiceImpl;
import org.example.service.user.impl.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserServiceImpl userServiceImpl;
    private final MenuItemServiceImpl menuItemServiceImpl;
    private final MenuItemRepository menuItemRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           UserServiceImpl userServiceImpl,
                           MenuItemServiceImpl menuItemServiceImpl,
                           MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.userServiceImpl = userServiceImpl;
        this.menuItemServiceImpl = menuItemServiceImpl;
        this.menuItemRepository = menuItemRepository;
    }

    // Sepete ürün ekleme
    @Transactional
    public void addItem(Long menuItemId, Integer quantity) {
        if (menuItemId == null) {
            throw new IllegalArgumentException("Menu item ID cannot be null");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        try {
            // Kullanıcı kontrolü
            User currentUser = userServiceImpl.getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("Kullanıcı girişi yapılmamış");
            }

            // Menü item kontrolü
            MenuItem menuItem = menuItemServiceImpl.findById(menuItemId);
            if (menuItem == null || menuItem.getPrice() == null) {
                throw new IllegalStateException("Menü öğesi fiyatı bulunamadı");
            }

            // Cart oluşturma/bulma
            Cart cart = cartRepository.findByUser(currentUser)
                    .orElseGet(() -> {
                        Cart newCart = new Cart();
                        newCart.setUser(currentUser);
                        newCart.setItems(new ArrayList<>());
                        return cartRepository.save(newCart);
                    });

            // Restoran kontrolü
            if (cart.getRestaurant() != null &&
                    !cart.getRestaurant().getId().equals(menuItem.getCategory().getRestaurant().getId())) {
                throw new IllegalStateException("Sepetinizde başka bir restoranın ürünleri var");
            }

            // Restoranı ayarla
            if (cart.getRestaurant() == null) {
                cart.setRestaurant(menuItem.getCategory().getRestaurant());
            }

            // CartItem oluştur
            CartItem cartItem = new CartItem();
            cartItem.setMenuItem(menuItem);
            cartItem.setQuantity(quantity != null ? quantity : 1); // Default değer
            cartItem.setCart(cart);

            // Sepete ekle
            cart.getItems().add(cartItem);
            cart.calculateTotal();

            // Kaydet
            Cart savedCart = cartRepository.save(cart);
            System.out.println("Cart saved successfully. Total amount: " + savedCart.getTotalAmount());

        } catch (Exception e) {
            System.out.println("CartServiceImpl Exception: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Sepetten ürün silme
    public Cart removeItem(Long itemId) {
        Cart cart = getCurrentCart();
        cart.getItems().removeIf(item -> item.getId().equals(itemId));

        if (cart.getItems().isEmpty()) {
            cart.setRestaurant(null);
        }

        return cartRepository.save(cart);
    }

    // Sepeti temizleme
    public void clearCart() {
        Cart cart = getCurrentCart();
        cart.getItems().clear();
        cart.setRestaurant(null);
        cartRepository.save(cart);
    }

    // Mevcut sepeti getirme
    public Cart getCurrentCart() {
        User currentUser = userServiceImpl.getCurrentUser();
        return cartRepository.findByUser(currentUser)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(currentUser);
                    cart.setItems(new ArrayList<>());
                    return cartRepository.save(cart);
                });
    }

    // Ürün miktarını artırma
    public void increaseQuantity(Long itemId) {
        Cart cart = getCurrentCart();
        cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(item.getQuantity() + 1);
                    cartRepository.save(cart);
                });
    }

    // Ürün miktarını azaltma
    public void decreaseQuantity(Long itemId) {
        Cart cart = getCurrentCart();
        cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    if (item.getQuantity() > 1) {
                        item.setQuantity(item.getQuantity() - 1);
                        cartRepository.save(cart);
                    } else {
                        removeItem(itemId);
                    }
                });
    }

    // CartItemRequest'i CartItem'a çevirme
    @Override
    public CartItem convertRequestToCartItem(CartItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        CartItem cartItem = new CartItem();
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setSpecialInstructions(request.getSpecialInstructions());
        return cartItem;
    }
}