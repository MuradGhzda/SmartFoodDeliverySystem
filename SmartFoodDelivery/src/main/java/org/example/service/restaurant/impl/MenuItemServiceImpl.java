package org.example.service.restaurant.impl;

import org.example.dto.restaurant.MenuItemImageStatus;
import org.example.exception.ResourceNotFoundException;
import org.example.model.restaurant.MenuItem;
import java.util.ArrayList;
import org.example.repository.restaurant.MenuItemRepository;
import org.example.service.restaurant.MenuItemService;
import org.example.service.util.impl.FoodImageServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final FoodImageServiceImpl foodImageServiceImpl;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository,
                               FoodImageServiceImpl foodImageServiceImpl) {
        this.menuItemRepository = menuItemRepository;
        this.foodImageServiceImpl = foodImageServiceImpl;
    }

    public MenuItem findById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));
    }

    public List<MenuItem> findByCategoryId(Long categoryId) {
        return menuItemRepository.findByCategoryIdAndIsActiveTrue(categoryId);
    }

    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return menuItemRepository.findByCategoryRestaurantIdAndIsActiveTrue(restaurantId);
    }
    @Transactional
    public void updateAllMenuItemImages() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        int updatedCount = 0;
        int totalItems = menuItems.size();

        System.out.println("Starting to update " + totalItems + " menu items...");

        for (MenuItem item : menuItems) {
            try {
                System.out.println("\nProcessing (" + (updatedCount + 1) + "/" + totalItems + "): " + item.getName());

                String imageUrl = foodImageServiceImpl.getFoodImage(item.getName());

                if (!imageUrl.equals("/images/items/default-food.jpg")) {
                    item.setImageUrl(imageUrl);
                    menuItemRepository.save(item);
                    updatedCount++;
                    System.out.println("✓ Updated: " + item.getName());
                    System.out.println("  New URL: " + imageUrl);
                } else {
                    System.out.println("✗ Failed to find image for: " + item.getName());
                }

                // API rate limit için bekleme (2 saniye)
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                System.out.println("\n⚠ Process interrupted after updating " + updatedCount + " items");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.out.println("✗ Error updating " + item.getName() + ": " + e.getMessage());
            }
        }

        System.out.println("\n=== Update Summary ===");
        System.out.println("Total items: " + totalItems);
        System.out.println("Updated items: " + updatedCount);
        System.out.println("Failed items: " + (totalItems - updatedCount));
    }
    public List<MenuItemImageStatus> checkMenuItemImages() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        List<MenuItemImageStatus> statuses = new ArrayList<>();

        for (MenuItem item : menuItems) {
            MenuItemImageStatus status = new MenuItemImageStatus();
            status.setMenuItemId(item.getId());
            status.setName(item.getName());
            status.setHasImage(item.getImageUrl() != null && !item.getImageUrl().isEmpty());
            status.setImageUrl(item.getImageUrl());
            statuses.add(status);
        }

        return statuses;
    }
}