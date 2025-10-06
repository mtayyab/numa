package com.numa.dto.response;

import com.numa.domain.entity.MenuCategory;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Response DTO for guest menu category data
 * Avoids lazy loading issues by only including necessary fields and mapping menu items
 */
public class GuestMenuCategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer sortOrder;
    private Boolean isActive;
    private LocalTime availableFrom;
    private LocalTime availableUntil;
    private List<GuestMenuItemResponse> menuItems; // Use DTO for menu items

    public GuestMenuCategoryResponse() {}

    public GuestMenuCategoryResponse(MenuCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.imageUrl = category.getImageUrl();
        this.sortOrder = category.getSortOrder();
        this.isActive = category.getIsActive();
        this.availableFrom = category.getAvailableFrom();
        this.availableUntil = category.getAvailableUntil();
        this.menuItems = category.getMenuItems() != null ? 
            category.getMenuItems().stream()
                .map(GuestMenuItemResponse::new)
                .collect(Collectors.toList()) : 
            List.of();
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<GuestMenuItemResponse> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<GuestMenuItemResponse> menuItems) {
        this.menuItems = menuItems;
    }
}
