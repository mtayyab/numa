package com.numa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;

/**
 * Request DTO for menu category operations.
 */
public class MenuCategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 255, message = "Category name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    private Integer sortOrder;

    private Boolean isActive;

    private LocalTime availableFrom;

    private LocalTime availableUntil;

    // Constructors
    public MenuCategoryRequest() {}

    public MenuCategoryRequest(String name, String description, String imageUrl, 
                              Integer sortOrder, Boolean isActive, 
                              LocalTime availableFrom, LocalTime availableUntil) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.availableFrom = availableFrom;
        this.availableUntil = availableUntil;
    }

    // Getters and Setters
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
}
