package com.numa.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for order operations.
 */
public class OrderResponse {

    private UUID id;
    private String orderNumber;
    private String status;
    private String orderType;
    private BigDecimal total;
    private String tableNumber;
    private String customerName;
    private String customerPhone;
    private String specialInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> items;

    // Constructors
    public OrderResponse() {}

    public OrderResponse(UUID id, String orderNumber, String status, String orderType, 
                       BigDecimal total, String tableNumber, String customerName, 
                       String customerPhone, String specialInstructions, 
                       LocalDateTime createdAt, LocalDateTime updatedAt, 
                       List<OrderItemResponse> items) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.status = status;
        this.orderType = orderType;
        this.total = total;
        this.tableNumber = tableNumber;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.specialInstructions = specialInstructions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = items;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
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

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    /**
     * Inner class for order item response
     */
    public static class OrderItemResponse {
        private UUID id;
        private UUID menuItemId;
        private String name;
        private Integer quantity;
        private BigDecimal price;
        private String specialInstructions;

        // Constructors
        public OrderItemResponse() {}

        public OrderItemResponse(UUID id, UUID menuItemId, String name, Integer quantity, 
                               BigDecimal price, String specialInstructions) {
            this.id = id;
            this.menuItemId = menuItemId;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.specialInstructions = specialInstructions;
        }

        // Getters and setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public UUID getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(UUID menuItemId) {
            this.menuItemId = menuItemId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getSpecialInstructions() {
            return specialInstructions;
        }

        public void setSpecialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
        }
    }
}
