package com.example.backend4rate.services.impl;

import com.example.backend4rate.models.dto.Restaurant;
import com.example.backend4rate.models.entities.FavoriteRestaurantEntity;
import com.example.backend4rate.models.entities.RestaurantEntity;
import com.example.backend4rate.models.entities.UserAccountEntity;
import com.example.backend4rate.repositories.FavoriteRestaurantRepository;
import com.example.backend4rate.repositories.RestaurantRepository;
import com.example.backend4rate.services.RestaurantServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService implements RestaurantServiceInterface {

    private final RestaurantRepository restaurantRepository;
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, FavoriteRestaurantRepository favoriteRestaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        this.favoriteRestaurantRepository = favoriteRestaurantRepository;
    }

    @Override
    public List<Restaurant> searchRestaurant(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Restaurant getRestaurant(int idRestaurant) {
        return restaurantRepository.findById(idRestaurant)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> getFavoriteRestaurants() {
        // Pretpostavimo da imamo metod za dobijanje trenutnog korisnika
        Integer userId = getCurrentUserId();
        return favoriteRestaurantRepository.findByUserAccountId(userId)
                .stream()
                .map(FavoriteRestaurantEntity::getRestaurant)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addFavoriteRestaurant(int idRestaurant) {
        Integer userId = getCurrentUserId();
        Optional<FavoriteRestaurantEntity> existingFavorite = favoriteRestaurantRepository.findByUserAccountIdAndRestaurantId(userId, idRestaurant);
        if (existingFavorite.isPresent()) {
            return false;
        }

        FavoriteRestaurantEntity favorite = new FavoriteRestaurantEntity();
        favorite.setUserAccount(new UserAccountEntity(userId));
        favorite.setRestaurant(restaurantRepository.findById(idRestaurant).orElseThrow(() -> new RuntimeException("Restoran nije pronađen")));

        favoriteRestaurantRepository.save(favorite);
        return true;
    }

    @Override
    public boolean updateRestaurantInformation(Restaurant restaurant) {
        Optional<RestaurantEntity> existingRestaurant = restaurantRepository.findById(restaurant.getId());
        if (existingRestaurant.isPresent()) {
            RestaurantEntity entity = existingRestaurant.get();
            entity.setName(restaurant.getName());
            entity.setDescription(restaurant.getDescription());
            entity.setWorkTime(restaurant.getWorkTime());
            restaurantRepository.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFavoriteRestaurant(int idRestaurant) {
        Integer userId = getCurrentUserId();
        Optional<FavoriteRestaurantEntity> favorite = favoriteRestaurantRepository.findByUserAccountIdAndRestaurantId(userId, idRestaurant);
        if (favorite.isPresent()) {
            favoriteRestaurantRepository.delete(favorite.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Restaurant> getAllRestaurants(List<CategoryEntity> categories) {
        // Implementacija za filtriranje restorana po kategorijama
        // Ovo može uključivati dodatne metode u RestaurantRepository
        return restaurantRepository.findByCategoriesIn(categories)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Pomoćna metoda za konvertovanje entiteta u DTO
    private Restaurant convertToDto(RestaurantEntity entity) {
        Restaurant dto = new Restaurant();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setWorkTime(entity.getWorkTime());
        return dto;
    }

    // Pomoćna metoda za dobijanje ID trenutnog korisnika (npr. iz sesije ili tokena)
    private Integer getCurrentUserId() {
        // Ovo je samo placeholder implementacija. Trebalo bi implementirati prema autentifikaciji.
        return 1; // Primer: vraća ID 1 kao trenutnog korisnika
    }
}
