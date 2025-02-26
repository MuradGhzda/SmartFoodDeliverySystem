package org.example.repository.restaurant;

import org.example.model.restaurant.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Page<Restaurant> findByCuisineType(String cuisine, Pageable pageable);

    @Query("SELECT DISTINCT r.cuisineType FROM Restaurant r")
    List<String> findDistinctCuisineTypes();

    List<Restaurant> findTop6ByOrderByRatingDesc();


    @Query("SELECT r FROM Restaurant r WHERE " +
            "(:cuisine IS NULL OR r.cuisineType = :cuisine) AND " +
            "(:minRating IS NULL OR r.rating >= :minRating) AND " +
            "(:maxDeliveryTime IS NULL OR r.deliveryTime <= :maxDeliveryTime) AND " +
            "(:maxDeliveryFee IS NULL OR r.deliveryFee <= :maxDeliveryFee) AND " +
            "(:isOpen IS NULL OR r.isOpen = :isOpen)")
    Page<Restaurant> findByFilters(
            @Param("cuisine") String cuisine,
            @Param("minRating") Double minRating,
            @Param("maxDeliveryTime") Integer maxDeliveryTime,
            @Param("maxDeliveryFee") Double maxDeliveryFee,
            @Param("isOpen") Boolean isOpen,
            Pageable pageable
    );

    @Query("SELECT r FROM Restaurant r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.cuisineType) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Restaurant> searchByNameOrDescription(@Param("query") String query, Pageable pageable);
}