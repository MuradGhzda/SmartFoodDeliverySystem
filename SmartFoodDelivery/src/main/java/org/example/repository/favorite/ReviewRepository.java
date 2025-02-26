package org.example.repository.favorite;

import org.example.model.restaurant.Restaurant;
import org.example.model.favorite.Review;
import org.example.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurant(Restaurant restaurant);
    List<Review> findByUser(User user);
    Double findAverageRatingByRestaurant(Restaurant restaurant);
    Page<Review> findByRestaurantOrderByCreatedAtDesc(Restaurant restaurant, Pageable pageable);
}