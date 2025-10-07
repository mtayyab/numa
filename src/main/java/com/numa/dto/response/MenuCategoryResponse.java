package com.numa.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for menu category operations.
 */
public class MenuCategoryResponse {

    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer sortOrder;
    private Boolean isActive;
    private LocalTime availableFrom;
    private LocalTime availableUntil;
    private List<MenuItemResponse> menuItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public MenuCategoryResponse() {}

    public MenuCategoryResponse(UUID id, String name, String description, String imageUrl,
                               Integer sortOrder, Boolean isActive, LocalTime availableFrom,
                               LocalTime availableUntil, List<MenuItemResponse> menuItems,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;
        this.menuItems = menuItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<MenuItemResponse> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemResponse> menuItems) {
        this.menuItems = menuItems;
    }
}
