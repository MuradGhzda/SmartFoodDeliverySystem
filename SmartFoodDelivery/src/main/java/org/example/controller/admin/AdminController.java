package org.example.controller.admin;

import org.example.dto.restaurant.MenuItemImageStatus;
import org.example.enums.OrderStatus;
import org.example.model.order.Order;
import org.example.model.restaurant.Restaurant;
import org.example.model.user.User;
import org.example.service.order.impl.OrderServiceImpl;
import org.example.service.restaurant.impl.RestaurantServiceImpl;
import org.example.service.user.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.service.restaurant.impl.MenuItemServiceImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final MenuItemServiceImpl menuItemServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final RestaurantServiceImpl restaurantServiceImpl;
    private final OrderServiceImpl orderServiceImpl;

    public AdminController(MenuItemServiceImpl menuItemServiceImpl,
                           UserServiceImpl userServiceImpl,
                           RestaurantServiceImpl restaurantServiceImpl,
                           OrderServiceImpl orderServiceImpl) {
        this.menuItemServiceImpl = menuItemServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.restaurantServiceImpl = restaurantServiceImpl;
        this.orderServiceImpl = orderServiceImpl;
    }

    @GetMapping({"", "/"})
    public String adminHome() {
        return "redirect:/admin/dashboard";
    }

    // Dashboard sayfası
    @GetMapping({"/dashboard", "/"})
    public String dashboard(Model model) {
        long userCount = userServiceImpl.getUserCount();
        long restaurantCount = restaurantServiceImpl.getRestaurantCount();
        long todayOrderCount = orderServiceImpl.getTodayOrderCount();
        BigDecimal totalRevenue = orderServiceImpl.calculateTotalRevenue();

        model.addAttribute("userCount", userCount);
        model.addAttribute("restaurantCount", restaurantCount);
        model.addAttribute("todayOrderCount", todayOrderCount);
        model.addAttribute("totalRevenue", totalRevenue);

        // Aktif siparişleri de ekleyelim
        List<Order> activeOrders = orderServiceImpl.getAllOrders().stream()
                .filter(order -> order.getStatus() != OrderStatus.COMPLETED)
                .collect(Collectors.toList());
        model.addAttribute("activeOrders", activeOrders);

        return "admin/dashboard";
    }
    @GetMapping("/users")
    public String userManagement(Model model) {
        List<User> users = userServiceImpl.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userServiceImpl.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Kullanıcı başarıyla silindi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kullanıcı silinirken bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/update-role")
    public String updateUserRole(
            @PathVariable Long id,
            @RequestParam String role,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userServiceImpl.updateUserRole(id, role);
            redirectAttributes.addFlashAttribute("successMessage", "Kullanıcı rolü başarıyla güncellendi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Kullanıcı rolü güncellenirken bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // Restoranlar yönetimi
    @GetMapping("/restaurants")
    public String restaurantManagement(Model model) {
        model.addAttribute("restaurants", restaurantServiceImpl.getAllRestaurants());
        return "admin/restaurants";
    }

    @PostMapping("/restaurants/add")
    public String addRestaurant(
            @ModelAttribute Restaurant restaurant,
            RedirectAttributes redirectAttributes
    ) {
        try {
            restaurantServiceImpl.createRestaurant(restaurant);
            redirectAttributes.addFlashAttribute("successMessage", "Restoran başarıyla eklendi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Restoran eklenirken bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/admin/restaurants";
    }
    @GetMapping("/restaurants/{id}/edit")
    public String editRestaurantForm(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantServiceImpl.getRestaurantById(id);
        model.addAttribute("restaurant", restaurant);
        return "admin/restaurant-form";
    }

    @PostMapping("/restaurants/{id}/update")
    public String updateRestaurant(
            @PathVariable Long id,
            @ModelAttribute Restaurant restaurant,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Restaurant existingRestaurant = restaurantServiceImpl.getRestaurantById(id);

            // Selectively update fields
            existingRestaurant.setName(restaurant.getName());
            existingRestaurant.setAddress(restaurant.getAddress());
            existingRestaurant.setPhone(restaurant.getPhone());
            existingRestaurant.setCuisineType(restaurant.getCuisineType());
            existingRestaurant.setRating(restaurant.getRating());

            restaurantServiceImpl.save(existingRestaurant);

            redirectAttributes.addFlashAttribute("successMessage", "Restoran başarıyla güncellendi!");
            return "redirect:/admin/restaurants";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Restoran güncellenirken bir hata oluştu: " + e.getMessage());
            return "redirect:/admin/restaurants";
        }
    }

    @PostMapping("/restaurants/{id}/delete")
    public String deleteRestaurant(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            restaurantServiceImpl.deleteRestaurant(id);
            redirectAttributes.addFlashAttribute("successMessage", "Restoran başarıyla silindi!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Restoran silinirken bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/admin/restaurants";
    }

    // Siparişler yönetimi
    @GetMapping("/orders")
    public String orderManagement(Model model) {
        model.addAttribute("orders", orderServiceImpl.getAllOrders());
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        orderServiceImpl.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderServiceImpl.deleteOrder(id);
        return "redirect:/admin/orders";
    }

    // Resim işlemleri
    @GetMapping("/update-food-images")
    public String updateFoodImagesGet() {
        try {
            menuItemServiceImpl.updateAllMenuItemImages();
            return "redirect:/restaurants?message=Images updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/restaurants?error=" + e.getMessage();
        }
    }

    @PostMapping("/update-food-images")
    @ResponseBody
    public ResponseEntity<String> updateFoodImagesPost() {
        try {
            menuItemServiceImpl.updateAllMenuItemImages();
            return ResponseEntity.ok("Resimler başarıyla güncellendi");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Hata: " + e.getMessage());
        }
    }

    @GetMapping("/check-food-images")
    public ResponseEntity<List<MenuItemImageStatus>> checkFoodImages() {
        List<MenuItemImageStatus> statuses = menuItemServiceImpl.checkMenuItemImages();
        return ResponseEntity.ok(statuses);
    }
}