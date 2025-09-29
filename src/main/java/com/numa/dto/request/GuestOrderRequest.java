package com.numa.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

/**
 * Request DTO for guest ordering items
 */
public class GuestOrderRequest {
    
    @NotNull(message = "Menu item ID is required")
    private Long menuItemId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;
    
    @Size(max = 500, message = "Special instructions must not exceed 500 characters")
    private String specialInstructions;
    
    private Long variationId;
    
    private Map<String, Object> customizations;
    
    // Constructors
    public GuestOrderRequest() {}
    
    public GuestOrderRequest(Long menuItemId, Integer quantity, String specialInstructions, Long variationId, Map<String, Object> customizations) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
        this.variationId = variationId;
        this.customizations = customizations;
    }
    
    // Getters and Setters
    public Long getMenuItemId() {
        return menuItemId;
    }
    
    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    public Long getVariationId() {
        return variationId;
    }
    
    public void setVariationId(Long variationId) {
        this.variationId = variationId;
    }
    
    public Map<String, Object> getCustomizations() {
        return customizations;
    }
    
    public void setCustomizations(Map<String, Object> customizations) {
        this.customizations = customizations;
    }
}
