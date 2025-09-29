package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import com.numa.domain.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * OrderItem entity representing individual items within an order.
 * Tracks quantity, pricing, and preparation status for each menu item.
 */
@Entity
@Table(name = "order_items", indexes = {
    @Index(name = "idx_order_items_order_id", columnList = "order_id"),
    @Index(name = "idx_order_items_menu_item_id", columnList = "menu_item_id"),
    @Index(name = "idx_order_items_status", columnList = "status"),
    @Index(name = "idx_order_items_analytics", columnList = "menu_item_id, created_at, quantity")
})
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id")
    private MenuItemVariation variation;

    @Size(max = 255, message = "Guest name must not exceed 255 characters")
    @Column(name = "guest_name")
    private String guestName;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 50, message = "Quantity must not exceed 50")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", message = "Unit price must be non-negative")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.0", message = "Total price must be non-negative")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Size(max = 1000, message = "Special instructions must not exceed 1000 characters")
    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "prepared_at")
    private LocalDateTime preparedAt;

    @Column(name = "served_at")
    private LocalDateTime servedAt;

    // Constructors
    public OrderItem() {
        super();
    }

    public OrderItem(Order order, MenuItem menuItem, Integer quantity) {
        this();
        this.order = order;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.unitPrice = menuItem.getPrice();
        calculateTotalPrice();
    }

    public OrderItem(Order order, MenuItem menuItem, MenuItemVariation variation, Integer quantity) {
        this(order, menuItem, quantity);
        this.variation = variation;
        if (variation != null) {
            this.unitPrice = menuItem.getPriceWithVariation(variation);
        }
        calculateTotalPrice();
    }

    // Business methods
    public void calculateTotalPrice() {
        this.totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }

    public boolean isConfirmed() {
        return status == OrderStatus.CONFIRMED;
    }

    public boolean isPreparing() {
        return status == OrderStatus.PREPARING;
    }

    public boolean isReady() {
        return status == OrderStatus.READY;
    }

    public boolean isServed() {
        return status == OrderStatus.SERVED;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }

    public void confirm() {
        if (isPending()) {
            this.status = OrderStatus.CONFIRMED;
        }
    }

    public void startPreparing() {
        if (isConfirmed()) {
            this.status = OrderStatus.PREPARING;
        }
    }

    public void markReady() {
        if (isPreparing()) {
            this.status = OrderStatus.READY;
            this.preparedAt = LocalDateTime.now();
        }
    }

    public void markServed() {
        if (isReady()) {
            this.status = OrderStatus.SERVED;
            this.servedAt = LocalDateTime.now();
        }
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public String getDisplayName() {
        StringBuilder name = new StringBuilder(menuItem.getName());
        if (variation != null) {
            name.append(" (").append(variation.getName()).append(")");
        }
        return name.toString();
    }

    public String getFullDescription() {
        StringBuilder description = new StringBuilder(getDisplayName());
        if (quantity > 1) {
            description.append(" x").append(quantity);
        }
        if (specialInstructions != null && !specialInstructions.trim().isEmpty()) {
            description.append(" - ").append(specialInstructions);
        }
        return description.toString();
    }

    public BigDecimal getItemSubtotal() {
        return totalPrice;
    }

    public String getStatusDisplay() {
        return switch (status) {
            case PENDING -> "Pending";
            case CONFIRMED -> "Confirmed";
            case PREPARING -> "Preparing";
            case READY -> "Ready";
            case SERVED -> "Served";
            case COMPLETED -> "Completed";
            case CANCELLED -> "Cancelled";
            case REFUNDED -> "Refunded";
        };
    }

    public boolean hasSpecialInstructions() {
        return specialInstructions != null && !specialInstructions.trim().isEmpty();
    }

    public boolean hasVariation() {
        return variation != null;
    }

    public String getGuestDisplayName() {
        return guestName != null ? guestName : "Guest";
    }

    // Getters and setters
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public MenuItemVariation getVariation() {
        return variation;
    }

    public void setVariation(MenuItemVariation variation) {
        this.variation = variation;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getPreparedAt() {
        return preparedAt;
    }

    public void setPreparedAt(LocalDateTime preparedAt) {
        this.preparedAt = preparedAt;
    }

    public LocalDateTime getServedAt() {
        return servedAt;
    }

    public void setServedAt(LocalDateTime servedAt) {
        this.servedAt = servedAt;
    }
}
