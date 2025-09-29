package com.numa.dto.response;

import com.numa.domain.entity.Order;
import com.numa.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for guest order data
 */
public class GuestOrderResponse {
    
    private UUID orderId;
    private UUID sessionId;
    private String guestName;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime estimatedReadyAt;
    private List<OrderItemResponse> items;
    
    // Constructors
    public GuestOrderResponse() {}
    
    public GuestOrderResponse(UUID orderId, UUID sessionId, String guestName, OrderStatus status, 
                             BigDecimal totalAmount, LocalDateTime createdAt, 
                             LocalDateTime estimatedReadyAt, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.sessionId = sessionId;
        this.guestName = guestName;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.estimatedReadyAt = estimatedReadyAt;
        this.items = items;
    }
    
    // Getters and Setters
    public UUID getOrderId() {
        return orderId;
    }
    
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
    
    public UUID getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getGuestName() {
        return guestName;
    }
    
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getEstimatedReadyAt() {
        return estimatedReadyAt;
    }
    
    public void setEstimatedReadyAt(LocalDateTime estimatedReadyAt) {
        this.estimatedReadyAt = estimatedReadyAt;
    }
    
    public List<OrderItemResponse> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
    
    // Inner class for order items
    public static class OrderItemResponse {
        private UUID itemId;
        private String itemName;
        private String description;
        private BigDecimal price;
        private Integer quantity;
        private String specialInstructions;
        private String variationName;
        
        // Constructors
        public OrderItemResponse() {}
        
        public OrderItemResponse(UUID itemId, String itemName, String description, 
                                BigDecimal price, Integer quantity, String specialInstructions, 
                                String variationName) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.specialInstructions = specialInstructions;
            this.variationName = variationName;
        }
        
        // Getters and Setters
        public UUID getItemId() {
            return itemId;
        }
        
        public void setItemId(UUID itemId) {
            this.itemId = itemId;
        }
        
        public String getItemName() {
            return itemName;
        }
        
        public void setItemName(String itemName) {
            this.itemName = itemName;
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
        
        public String getVariationName() {
            return variationName;
        }
        
        public void setVariationName(String variationName) {
            this.variationName = variationName;
        }
    }
}
