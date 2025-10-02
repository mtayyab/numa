package com.numa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for table operations.
 */
public class TableRequest {

    @NotBlank(message = "Table number is required")
    @Size(max = 50, message = "Table number must not exceed 50 characters")
    private String tableNumber;

    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    // Constructors
    public TableRequest() {}

    public TableRequest(String tableNumber, Integer capacity, String location, String description) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.location = location;
        this.description = description;
    }

    // Getters and setters
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
}
