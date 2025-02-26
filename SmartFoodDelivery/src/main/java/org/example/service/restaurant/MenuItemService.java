package org.example.service.restaurant;

import org.example.dto.restaurant.MenuItemImageStatus;
import org.example.model.restaurant.MenuItem;

import java.util.List;

public interface MenuItemService {
    MenuItem findById(Long id);
    List<MenuItem> findByCategoryId(Long categoryId);
    List<MenuItem> findByRestaurantId(Long restaurantId);
    void updateAllMenuItemImages();
    List<MenuItemImageStatus> checkMenuItemImages();
}