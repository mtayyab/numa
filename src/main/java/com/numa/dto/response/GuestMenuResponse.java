package com.numa.dto.response;

import java.util.List;

/**
 * Response DTO for guest menu data.
 * Uses lightweight DTOs to avoid lazy loading issues.
 */
public class GuestMenuResponse {
    
    private GuestRestaurantDTO restaurant;
    private List<GuestMenuCategoryDTO> categories;
    
    // Constructors
    public GuestMenuResponse() {}
    
    public GuestMenuResponse(GuestRestaurantDTO restaurant, List<GuestMenuCategoryDTO> categories) {
        this.restaurant = restaurant;
        this.categories = categories;
    }
    
    // Getters and Setters
    public GuestRestaurantDTO getRestaurant() {
        return restaurant;
    }
    
    public void setRestaurant(GuestRestaurantDTO restaurant) {
        this.restaurant = restaurant;
    }
    
    public List<GuestMenuCategoryDTO> getCategories() {
        return categories;
    }
    
    public void setCategories(List<GuestMenuCategoryDTO> categories) {
        this.categories = categories;
    }
}
