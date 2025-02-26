package org.example.repository.restaurant;

import org.example.model.restaurant.MenuCategory;
import org.example.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.example.model.restaurant.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    List<MenuCategory> findByRestaurantOrderByDisplayOrder(Restaurant restaurant);
    List<MenuCategory> findByRestaurantAndIsActiveTrue(Restaurant restaurant);
    List<MenuCategory> findByRestaurantIdOrderByDisplayOrder(Long restaurantId);
    @Modifying
    @Query("DELETE FROM MenuCategory mc WHERE mc.restaurant = :restaurant")
    void deleteByRestaurant(Restaurant restaurant);
    long countByRestaurant(Restaurant restaurant);
}
