package org.example.controller.restaurant;

import org.example.model.restaurant.Restaurant;
import org.example.service.cart.impl.CartServiceImpl;
import org.example.service.restaurant.impl.RestaurantServiceImpl;
import org.example.service.restaurant.impl.MenuCategoryServiceImpl;
import org.example.service.restaurant.impl.MenuItemServiceImpl;
import org.example.model.restaurant.MenuCategory;
import org.example.model.restaurant.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantServiceImpl restaurantServiceImpl;
    private final MenuCategoryServiceImpl menuCategoryServiceImpl;
    private final MenuItemServiceImpl menuItemServiceImpl;
    private final CartServiceImpl cartServiceImpl;

    public RestaurantController(RestaurantServiceImpl restaurantServiceImpl,
                                MenuCategoryServiceImpl menuCategoryServiceImpl,
                                MenuItemServiceImpl menuItemServiceImpl,
                                CartServiceImpl cartServiceImpl) {
        this.restaurantServiceImpl = restaurantServiceImpl;
        this.menuCategoryServiceImpl = menuCategoryServiceImpl;
        this.menuItemServiceImpl = menuItemServiceImpl;
        this.cartServiceImpl = cartServiceImpl;
    }

    @GetMapping
    public String getAllRestaurants(
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String query,
            Model model
    ) {
        Page<Restaurant> restaurants;
        if (query != null && !query.isEmpty()) {
            restaurants = restaurantServiceImpl.searchRestaurants(query, cuisine, sortBy, page, size);
            model.addAttribute("searchQuery", query);
        } else {
            restaurants = restaurantServiceImpl.getRestaurants(cuisine, sortBy, page, size);
        }

        // Filtreleme için cuisine tiplerini getir
        List<String> cuisineTypes = restaurantServiceImpl.getAllCuisineTypes();

        // Model'e verileri ekle
        model.addAttribute("restaurants", restaurants.getContent());
        model.addAttribute("cuisines", cuisineTypes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", restaurants.getTotalPages());
        model.addAttribute("selectedCuisine", cuisine);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("hasNext", restaurants.hasNext());
        model.addAttribute("hasPrevious", restaurants.hasPrevious());

        return "restaurants";
    }

    @GetMapping("/{id}")
    public String getRestaurantDetails(@PathVariable Long id,
                                       @RequestParam(required = false) Long category,
                                       Model model) {
        Restaurant restaurant = restaurantServiceImpl.getRestaurantById(id);
        List<MenuCategory> categories = menuCategoryServiceImpl.findByRestaurantId(id);
        List<MenuItem> menuItems;

        if (category != null) {
            menuItems = menuItemServiceImpl.findByCategoryId(category);
            model.addAttribute("activeCategory", category);
        } else {
            menuItems = menuItemServiceImpl.findByRestaurantId(id);
            model.addAttribute("activeCategory",
                    categories.isEmpty() ? null : categories.get(0).getId());
        }

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("categories", categories);
        model.addAttribute("menuItems", menuItems);

        return "restaurant-detail";
    }

    @GetMapping("/api/restaurants/categories/{categoryId}/items")
    @ResponseBody
    public List<MenuItem> getCategoryItems(@PathVariable Long categoryId) {
        return menuItemServiceImpl.findByCategoryId(categoryId);
    }
    @GetMapping("/restaurants")
    public String restaurantManagement(Model model) {
        List<Restaurant> restaurants = restaurantServiceImpl.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "restaurants";  // templates/restaurants.html
    }

    @GetMapping("/restaurants/add")
    public String showAddRestaurantForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "restaurant-add";  // templates/restaurant-add.html
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
            existingRestaurant.setDescription(restaurant.getDescription());
            existingRestaurant.setRating(restaurant.getRating());
            existingRestaurant.setDeliveryTime(restaurant.getDeliveryTime());
            existingRestaurant.setDeliveryFee(restaurant.getDeliveryFee());
            existingRestaurant.setMinOrderAmount(restaurant.getMinOrderAmount());
            existingRestaurant.setOpen(restaurant.isOpen());

            restaurantServiceImpl.save(existingRestaurant);

            redirectAttributes.addFlashAttribute("successMessage", "Restoran başarıyla güncellendi!");
            return "redirect:/admin/restaurants";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Restoran güncellenirken bir hata oluştu: " + e.getMessage());
            return "redirect:/admin/restaurants";
        }
    }

    @PostMapping("/restaurants/add")
    public String addRestaurant(@ModelAttribute Restaurant restaurant) {
        restaurantServiceImpl.createRestaurant(restaurant);
        return "redirect:/admin/restaurants";
    }

    @PostMapping("/restaurants/{id}/delete")
    public String deleteRestaurant(@PathVariable Long id) {
        restaurantServiceImpl.deleteRestaurant(id);
        return "redirect:/admin/restaurants";
    }

}