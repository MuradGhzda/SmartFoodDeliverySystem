package org.example.repository.restaurant;

import org.example.model.restaurant.MenuItem;
import org.example.model.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.example.model.restaurant.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @Query("SELECT mi FROM MenuItem mi WHERE mi.category.restaurant = :restaurant AND mi.isActive = true")
    List<MenuItem> findByRestaurantAndIsActiveTrue(@Param("restaurant") Restaurant restaurant);

    List<MenuItem> findByIsActiveTrue();

    List<MenuItem> findByCategoryIdAndIsActiveTrue(Long categoryId);

    List<MenuItem> findByCategoryRestaurantIdAndIsActiveTrue(Long restaurantId);

    @Modifying
    @Query("DELETE FROM MenuItem mi WHERE mi.category.restaurant = :restaurant")
    void deleteByRestaurant(Restaurant restaurant);
    // Find count of menu items for a restaurant
    @Query("SELECT COUNT(mi) FROM MenuItem mi WHERE mi.category.restaurant = :restaurant")
    long countByRestaurant(Restaurant restaurant);
}