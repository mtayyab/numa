package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * MenuCategory entity representing categories in a restaurant's menu.
 * Categories help organize menu items and can have availability windows.
 */
@Entity
@Table(name = "menu_categories", indexes = {
    @Index(name = "idx_categories_restaurant_id", columnList = "restaurant_id"),
    @Index(name = "idx_categories_sort_order", columnList = "restaurant_id, sort_order")
})
public class MenuCategory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotBlank(message = "Category name is required")
    @Size(max = 255, message = "Category name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Column(name = "image_url")
    private String imageUrl;

    @Min(value = 0, message = "Sort order must be non-negative")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "available_from")
    private LocalTime availableFrom;

    @Column(name = "available_until")
    private LocalTime availableUntil;

    // Relationships
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC, name ASC")
    private List<MenuItem> menuItems = new ArrayList<>();

    // Constructors
    public MenuCategory() {
        super();
    }

    public MenuCategory(Restaurant restaurant, String name) {
        this();
        this.restaurant = restaurant;
        this.name = name;
    }

    public MenuCategory(Restaurant restaurant, String name, String description, Integer sortOrder) {
        this(restaurant, name);
        this.description = description;
        this.sortOrder = sortOrder;
    }

    // Business methods
    public boolean isAvailableNow() {
        if (!isActive) {
            return false;
        }

        if (availableFrom == null && availableUntil == null) {
            return true; // Available all day
        }

        LocalTime now = LocalTime.now();
        
        if (availableFrom != null && availableUntil != null) {
            if (availableFrom.isBefore(availableUntil)) {
                // Same day availability (e.g., 9:00 AM - 5:00 PM)
                return !now.isBefore(availableFrom) && !now.isAfter(availableUntil);
            } else {
                // Crosses midnight (e.g., 10:00 PM - 2:00 AM)
                return !now.isBefore(availableFrom) || !now.isAfter(availableUntil);
            }
        } else if (availableFrom != null) {
            return !now.isBefore(availableFrom);
        } else if (availableUntil != null) {
            return !now.isAfter(availableUntil);
        }

        return true;
    }

    public long getAvailableItemsCount() {
        return menuItems.stream()
                .filter(item -> item.isActive() && item.isAvailable())
                .count();
    }

    public List<MenuItem> getAvailableItems() {
        return menuItems.stream()
                .filter(item -> item.isActive() && item.isAvailable())
                .toList();
    }

    // Getters and setters
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalTime getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalTime availableFrom) {
        this.availableFrom = availableFrom;
    }

    public LocalTime getAvailableUntil() {
        return availableUntil;
    }

    public void setAvailableUntil(LocalTime availableUntil) {
        this.availableUntil = availableUntil;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
