package com.example.backend4rate.controllers;

import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.entities.CategoryEntity;
import com.example.backend4rate.services.RestaurantServiceInterface;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/restaurants")
public class RestaurantController {

    private final RestaurantServiceInterface restaurantService;

    public RestaurantController(RestaurantServiceInterface restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/search")
    public List<Restaurant> searchRestaurant(@RequestParam String name) {
        return restaurantService.searchRestaurant(name);
    }

    @GetMapping("/{id}")
    public Restaurant getRestaurant(@PathVariable int id) {
        return restaurantService.getRestaurant(id);
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/favorites")
    public List<Restaurant> getFavoriteRestaurants() {
        return restaurantService.getFavoriteRestaurants();
    }

    @PostMapping("/favorites/{id}")
    public boolean addFavoriteRestaurant(@PathVariable int id) {
        return restaurantService.addFavoriteRestaurant(id);
    }

    @PutMapping("/update")
    public boolean updateRestaurantInformation(@RequestBody Restaurant restaurant) {
        return restaurantService.updateRestaurantInformation(restaurant);
    }

    @DeleteMapping("/favorites/{id}")
    public boolean removeFavoriteRestaurant(@PathVariable int id) {
        return restaurantService.removeFavoriteRestaurant(id);
    }

    @GetMapping("/by-category")
    public List<Restaurant> getAllRestaurants(@RequestParam List<CategoryEntity> categories) {
        return restaurantService.getAllRestaurants(categories);
    }
}
