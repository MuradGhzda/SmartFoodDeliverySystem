package org.example.service.restaurant.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.restaurant.Restaurant;
import org.example.repository.restaurant.MenuCategoryRepository;
import org.example.repository.restaurant.MenuItemRepository;
import org.example.repository.restaurant.RestaurantRepository;
import org.example.service.restaurant.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    @Autowired
    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository,
            MenuCategoryRepository menuCategoryRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.menuCategoryRepository = menuCategoryRepository;
    }

    @Override
    public Page<Restaurant> getRestaurants(String cuisine, String sortBy, int page, int size) {
        Sort sort = createSort(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        if (cuisine != null && !cuisine.isEmpty()) {
            return restaurantRepository.findByCuisineType(cuisine, pageRequest);
        }

        return restaurantRepository.findAll(pageRequest);
    }

    @Override
    public List<String> getAllCuisineTypes() {
        return restaurantRepository.findDistinctCuisineTypes();
    }

    private Sort createSort(String sortBy) {
        if (sortBy == null) {
            return Sort.by(Sort.Direction.DESC, "rating");
        }

        return switch (sortBy) {
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            case "deliveryTime" -> Sort.by(Sort.Direction.ASC, "deliveryTime");
            case "minOrderAmount" -> Sort.by(Sort.Direction.ASC, "minOrderAmount");
            case "name" -> Sort.by(Sort.Direction.ASC, "name");
            default -> Sort.by(Sort.Direction.DESC, "rating");
        };
    }

    @Override
    public List<Restaurant> getPopularRestaurants() {
        return restaurantRepository.findTop6ByOrderByRatingDesc();
    }

    @Override
    public Page<Restaurant> getRestaurantsByFilters(
            String cuisine,
            Double minRating,
            Integer maxDeliveryTime,
            Double maxDeliveryFee,
            Boolean isOpen,
            int page,
            int size,
            String sortBy
    ) {
        Sort sort = createSort(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return restaurantRepository.findByFilters(
                cuisine,
                minRating,
                maxDeliveryTime,
                maxDeliveryFee,
                isOpen,
                pageRequest
        );
    }

    @Override
    public Page<Restaurant> searchRestaurants(String query, String cuisine, String sortBy, int page, int size) {
        Sort sort = createSort(sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        if (query != null && !query.isEmpty()) {
            return restaurantRepository.searchByNameOrDescription(query, pageRequest);
        }

        return getRestaurants(cuisine, sortBy, page, size);
    }

    @Override
    public long getRestaurantCount() {
        return restaurantRepository.count();
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restoran bulunamadı"));

        // Check if restaurant has any associated orders or other constraints
        if (!restaurant.getOrders().isEmpty()) {
            throw new IllegalStateException("Bu restorana ait sipariş bulunduğundan silinemez.");
        }

        // Clear relationships to avoid potential cascade issues
        restaurant.getMenuCategories().clear();
        restaurant.getOrders().clear();

        // Delete the restaurant
        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant updateRestaurant(Restaurant restaurant) {
        // Find existing restaurant
        Restaurant existingRestaurant = restaurantRepository.findById(restaurant.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restoran bulunamadı"));

        // Selectively update fields
        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setDescription(restaurant.getDescription());
        existingRestaurant.setCuisineType(restaurant.getCuisineType());
        existingRestaurant.setRating(restaurant.getRating());
        existingRestaurant.setDeliveryTime(restaurant.getDeliveryTime());
        existingRestaurant.setDeliveryFee(restaurant.getDeliveryFee());
        existingRestaurant.setMinOrderAmount(restaurant.getMinOrderAmount());
        existingRestaurant.setOpen(restaurant.isOpen());
        existingRestaurant.setAddress(restaurant.getAddress());
        existingRestaurant.setPhone(restaurant.getPhone());
        existingRestaurant.setImageUrl(restaurant.getImageUrl());

        // Note: createdAt and updatedAt will be handled by @CreationTimestamp and @UpdateTimestamp

        return restaurantRepository.save(existingRestaurant);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restoran bulunamadı"));
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
}