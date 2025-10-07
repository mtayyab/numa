package com.numa.service;

import com.numa.dto.request.RestaurantSettingsRequest;
import com.numa.dto.response.RestaurantSettingsResponse;
import com.numa.domain.entity.Restaurant;
import com.numa.repository.RestaurantRepository;
import com.numa.exception.ResourceNotFoundException;
import com.numa.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for managing restaurant settings
 */
@Service
@Transactional
public class SettingsService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    /**
     * Get restaurant settings for the current user
     */
    @Transactional(readOnly = true)
    public RestaurantSettingsResponse getRestaurantSettings() {
        UUID restaurantId = getCurrentUserRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        
        return new RestaurantSettingsResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getEmail(),
                restaurant.getAddressLine1(),
                restaurant.getCity(),
                restaurant.getCountry(),
                restaurant.getLogoUrl(),
                restaurant.getCurrencyCode(),
                restaurant.getLanguageCode(),
                restaurant.getTimezone(),
                2 // Default decimal places
        );
    }
    
    /**
     * Update restaurant settings
     */
    public RestaurantSettingsResponse updateRestaurantSettings(RestaurantSettingsRequest request) {
        UUID restaurantId = getCurrentUserRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        
        // Update restaurant fields
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setPhone(request.getPhone());
        restaurant.setEmail(request.getEmail());
        restaurant.setAddressLine1(request.getAddress());
        restaurant.setCity(request.getCity());
        restaurant.setCountry(request.getCountry());
        restaurant.setLogoUrl(request.getLogoUrl());
        restaurant.setCurrencyCode(request.getCurrencyCode());
        restaurant.setLanguageCode(request.getLanguageCode());
        restaurant.setTimezone(request.getTimezone());
        
        // Save updated restaurant
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        
        return new RestaurantSettingsResponse(
                savedRestaurant.getId(),
                savedRestaurant.getName(),
                savedRestaurant.getDescription(),
                savedRestaurant.getPhone(),
                savedRestaurant.getEmail(),
                savedRestaurant.getAddressLine1(),
                savedRestaurant.getCity(),
                savedRestaurant.getCountry(),
                savedRestaurant.getLogoUrl(),
                savedRestaurant.getCurrencyCode(),
                savedRestaurant.getLanguageCode(),
                savedRestaurant.getTimezone(),
                request.getDecimalPlaces() != null ? request.getDecimalPlaces() : 2
        );
    }
    
    /**
     * Get current user's restaurant ID from security context
     */
    private UUID getCurrentUserRestaurantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("No authenticated user found");
        }
        
        // Extract restaurant ID from authorities
        return authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(auth -> auth.startsWith("RESTAURANT_"))
                .map(auth -> auth.substring("RESTAURANT_".length()))
                .map(UUID::fromString)
                .findFirst()
                .orElseThrow(() -> new ValidationException("Restaurant ID not found in user context"));
    }
}
