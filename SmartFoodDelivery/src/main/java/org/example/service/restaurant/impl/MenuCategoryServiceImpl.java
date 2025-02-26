package org.example.service.restaurant.impl;

import org.example.model.restaurant.MenuCategory;
import org.example.repository.restaurant.MenuCategoryRepository;
import org.example.service.restaurant.MenuCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class MenuCategoryServiceImpl implements MenuCategoryService {
    private final MenuCategoryRepository menuCategoryRepository;

    public MenuCategoryServiceImpl(MenuCategoryRepository menuCategoryRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
    }

    public List<MenuCategory> findByRestaurantId(Long restaurantId) {
        return menuCategoryRepository.findByRestaurantIdOrderByDisplayOrder(restaurantId);
    }
}