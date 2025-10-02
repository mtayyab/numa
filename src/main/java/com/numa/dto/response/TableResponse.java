package com.numa.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for table operations.
 */
public class TableResponse {

    private UUID id;
    private String tableNumber;
    private Integer capacity;
    private String location;
    private String description;
    private String status;
    private String qrCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public TableResponse() {}

    public TableResponse(UUID id, String tableNumber, Integer capacity, String location, 
                       String description, String status, String qrCode, 
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.location = location;
        this.description = description;
        this.status = status;
        this.qrCode = qrCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
