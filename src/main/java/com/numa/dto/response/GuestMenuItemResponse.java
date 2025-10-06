package com.numa.dto.response;

import com.numa.domain.entity.MenuItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for guest menu item information
 */
public class GuestMenuItemResponse {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Integer sortOrder;
    private Boolean isActive;
    private Boolean isAvailable;
    private List<String> allergens;

    public GuestMenuItemResponse() {}

    public GuestMenuItemResponse(MenuItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.imageUrl = item.getImageUrl();
        this.sortOrder = item.getSortOrder();
        this.isActive = item.getIsActive();
        this.isAvailable = item.getIsAvailable();
        this.allergens = item.getAllergens() != null ? List.of(item.getAllergens()) : List.of();
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }
}
