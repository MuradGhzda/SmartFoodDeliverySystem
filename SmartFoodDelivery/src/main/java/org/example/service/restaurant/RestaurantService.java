package org.example.service.restaurant;

import org.example.model.restaurant.Restaurant;
import org.springframework.data.domain.Page;
import java.util.List;

public interface RestaurantService {
    Restaurant createRestaurant(Restaurant restaurant);
    void deleteRestaurant(Long id);
    Restaurant updateRestaurant(Restaurant restaurant);
    Restaurant getRestaurantById(Long id);
    List<Restaurant> getAllRestaurants();
    long getRestaurantCount();
    Page<Restaurant> getRestaurants(String cuisine, String sortBy, int page, int size);
    List<Restaurant> getPopularRestaurants();
    List<String> getAllCuisineTypes();
    Page<Restaurant> getRestaurantsByFilters(
            String cuisine,
            Double minRating,
            Integer maxDeliveryTime,
            Double maxDeliveryFee,
            Boolean isOpen,
            int page,
            int size,
            String sortBy
    );
    Page<Restaurant> searchRestaurants(String query, String cuisine, String sortBy, int page, int size);

    Restaurant save(Restaurant restaurant);
}