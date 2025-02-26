package org.example.repository.favorite;

import org.example.model.favorite.Favorite;
import org.example.model.restaurant.Restaurant;
import org.example.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    boolean existsByUserAndRestaurant(User user, Restaurant restaurant);
    void deleteByUserAndRestaurant(User user, Restaurant restaurant);
}