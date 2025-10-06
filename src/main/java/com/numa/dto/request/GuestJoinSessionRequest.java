package com.numa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for guest joining a dining session
 */
public class GuestJoinSessionRequest {
    
    @NotBlank(message = "Guest name is required")
    @Size(min = 1, max = 100, message = "Guest name must be between 1 and 100 characters")
    private String guestName;
    
    @NotBlank(message = "Table QR code is required")
    private String tableQrCode;
    
    @NotNull(message = "Restaurant ID is required")
    private java.util.UUID restaurantId;
    
    // Constructors
    public GuestJoinSessionRequest() {}
    
    public GuestJoinSessionRequest(String guestName, String tableQrCode, java.util.UUID restaurantId) {
        this.guestName = guestName;
        this.tableQrCode = tableQrCode;
        this.restaurantId = restaurantId;
    }
    
    // Getters and Setters
    public String getGuestName() {
        return guestName;
    }
    
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    
    public String getTableQrCode() {
        return tableQrCode;
    }
    
    public void setTableQrCode(String tableQrCode) {
        this.tableQrCode = tableQrCode;
    }
    
    public java.util.UUID getRestaurantId() {
        return restaurantId;
    }
    
    public void setRestaurantId(java.util.UUID restaurantId) {
        this.restaurantId = restaurantId;
    }
}
