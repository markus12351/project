package com.example.backend4rate.services;

import com.example.backend4rate.models.entities.RestaurantEntity;
import java.util.List;

public interface RestaurantServiceInterface {
    List<RestaurantEntity> getAllRestaurants();
    RestaurantEntity addRestaurant(RestaurantEntity restaurant);
    RestaurantEntity getRestaurantById(Integer id);
    void deleteRestaurant(Integer id);
}
