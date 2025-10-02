package com.numa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request DTO for order status updates.
 */
public class OrderStatusUpdateRequest {

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PENDING|CONFIRMED|PREPARING|READY|COMPLETED|CANCELLED", 
             message = "Status must be one of: PENDING, CONFIRMED, PREPARING, READY, COMPLETED, CANCELLED")
    private String status;

    // Constructors
    public OrderStatusUpdateRequest() {}

    public OrderStatusUpdateRequest(String status) {
        this.status = status;
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
