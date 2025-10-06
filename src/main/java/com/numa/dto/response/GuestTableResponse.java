package com.numa.dto.response;

import com.numa.domain.entity.RestaurantTable;
import com.numa.domain.enums.TableStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for guest table information
 */
public class GuestTableResponse {
    private UUID id;
    private UUID restaurantId;
    private String tableNumber;
    private Integer capacity;
    private String locationDescription;
    private String description; // Added for frontend compatibility
    private TableStatus status;
    private String qrCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GuestTableResponse() {}

    public GuestTableResponse(RestaurantTable table) {
        this.id = table.getId();
        this.restaurantId = table.getRestaurant().getId();
        this.tableNumber = table.getTableNumber();
        this.capacity = table.getCapacity();
        this.locationDescription = table.getLocationDescription();
        this.description = table.getLocationDescription(); // Use locationDescription as description
        this.status = table.getStatus();
        this.qrCode = table.getQrCode();
        this.createdAt = table.getCreatedAt();
        this.updatedAt = table.getUpdatedAt();
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
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
}
