package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import com.numa.domain.enums.TableStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * RestaurantTable entity representing physical tables in a restaurant.
 * Each table has a unique QR code for guest access.
 */
@Entity
@Table(name = "restaurant_tables", 
       indexes = {
           @Index(name = "idx_tables_restaurant_id", columnList = "restaurant_id"),
           @Index(name = "idx_tables_qr_code", columnList = "qr_code"),
           @Index(name = "idx_tables_status", columnList = "status")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_restaurant_table_number", 
                           columnNames = {"restaurant_id", "table_number"})
       })
public class RestaurantTable extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotBlank(message = "Table number is required")
    @Size(max = 20, message = "Table number must not exceed 20 characters")
    @Column(name = "table_number", nullable = false)
    private String tableNumber;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 20, message = "Capacity must not exceed 20")
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Size(max = 255, message = "Location description must not exceed 255 characters")
    @Column(name = "location_description")
    private String locationDescription;

    @NotBlank(message = "QR code is required")
    @Size(max = 255, message = "QR code must not exceed 255 characters")
    @Column(name = "qr_code", nullable = false, unique = true)
    private String qrCode;

    @Size(max = 500, message = "QR code URL must not exceed 500 characters")
    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TableStatus status = TableStatus.AVAILABLE;

    @Column(name = "current_session_id")
    private UUID currentSessionId;

    @Column(name = "last_cleaned_at")
    private LocalDateTime lastCleanedAt;

    // Constructors
    public RestaurantTable() {
        super();
    }

    public RestaurantTable(Restaurant restaurant, String tableNumber, Integer capacity) {
        this();
        this.restaurant = restaurant;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.qrCode = generateQrCode();
    }

    // Business methods
    public boolean isAvailable() {
        return status == TableStatus.AVAILABLE;
    }

    public boolean isOccupied() {
        return status == TableStatus.OCCUPIED;
    }

    public boolean needsCleaning() {
        return status == TableStatus.NEEDS_CLEANING;
    }

    public void occupy(UUID sessionId) {
        this.status = TableStatus.OCCUPIED;
        this.currentSessionId = sessionId;
    }

    public void makeAvailable() {
        this.status = TableStatus.AVAILABLE;
        this.currentSessionId = null;
        this.lastCleanedAt = LocalDateTime.now();
    }

    public void markForCleaning() {
        this.status = TableStatus.NEEDS_CLEANING;
        this.currentSessionId = null;
    }

    public String getDisplayName() {
        return "Table " + tableNumber;
    }

    public String getQrCodeData() {
        return String.format("/restaurant/%s/table/%s", 
                           restaurant.getSlug(), 
                           qrCode);
    }

    private String generateQrCode() {
        // Generate a unique QR code for this table
        return "TBL_" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    // Getters and setters
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public UUID getCurrentSessionId() {
        return currentSessionId;
    }

    public void setCurrentSessionId(UUID currentSessionId) {
        this.currentSessionId = currentSessionId;
    }

    public LocalDateTime getLastCleanedAt() {
        return lastCleanedAt;
    }

    public void setLastCleanedAt(LocalDateTime lastCleanedAt) {
        this.lastCleanedAt = lastCleanedAt;
    }
}
