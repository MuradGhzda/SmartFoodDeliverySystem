package org.example.service.restaurant;

import org.example.model.restaurant.MenuCategory;

import java.util.List;

public interface MenuCategoryService {
    List<MenuCategory> findByRestaurantId(Long restaurantId);
}