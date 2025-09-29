package com.numa.dto.response;

import com.numa.domain.entity.MenuCategory;
import com.numa.domain.entity.Restaurant;

import java.util.List;

/**
 * Response DTO for guest menu data
 */
public class GuestMenuResponse {
    
    private Restaurant restaurant;
    private List<MenuCategory> categories;
    
    // Constructors
    public GuestMenuResponse() {}
    
    public GuestMenuResponse(Restaurant restaurant, List<MenuCategory> categories) {
        this.restaurant = restaurant;
        this.categories = categories;
    }
    
    // Getters and Setters
    public Restaurant getRestaurant() {
        return restaurant;
    }
    
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    
    public List<MenuCategory> getCategories() {
        return categories;
    }
    
    public void setCategories(List<MenuCategory> categories) {
        this.categories = categories;
    }
}
