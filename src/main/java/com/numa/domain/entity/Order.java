package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import com.numa.domain.enums.OrderStatus;
import com.numa.domain.enums.OrderType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Order entity representing customer orders in the system.
 * Supports dine-in, takeaway, and delivery orders with comprehensive tracking.
 */
@Entity
@Table(name = "orders", 
       indexes = {
           @Index(name = "idx_orders_restaurant_id", columnList = "restaurant_id"),
           @Index(name = "idx_orders_table_id", columnList = "table_id"),
           @Index(name = "idx_orders_session_id", columnList = "session_id"),
           @Index(name = "idx_orders_status", columnList = "status"),
           @Index(name = "idx_orders_created_at", columnList = "created_at"),
           @Index(name = "idx_orders_restaurant_status_date", columnList = "restaurant_id, status, created_at"),
           @Index(name = "idx_orders_analytics", columnList = "restaurant_id, created_at, total_amount, status")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_restaurant_order_number", 
                           columnNames = {"restaurant_id", "order_number"})
       })
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    @Column(name = "session_id")
    private UUID sessionId;

    @NotBlank(message = "Order number is required")
    @Size(max = 50, message = "Order number must not exceed 50 characters")
    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType = OrderType.DINE_IN;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Size(max = 255, message = "Customer name must not exceed 255 characters")
    @Column(name = "customer_name")
    private String customerName;

    @Size(max = 20, message = "Customer phone must not exceed 20 characters")
    @Column(name = "customer_phone")
    private String customerPhone;

    @Email(message = "Customer email must be valid")
    @Size(max = 255, message = "Customer email must not exceed 255 characters")
    @Column(name = "customer_email")
    private String customerEmail;

    @Size(max = 1000, message = "Special instructions must not exceed 1000 characters")
    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @NotNull(message = "Subtotal is required")
    @DecimalMin(value = "0.0", message = "Subtotal must be non-negative")
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", message = "Tax amount must be non-negative")
    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Service charge must be non-negative")
    @Column(name = "service_charge", nullable = false, precision = 10, scale = 2)
    private BigDecimal serviceCharge = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Delivery fee must be non-negative")
    @Column(name = "delivery_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Discount amount must be non-negative")
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", message = "Total amount must be non-negative")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Size(max = 20, message = "Payment status must not exceed 20 characters")
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "PENDING";

    @Size(max = 20, message = "Payment method must not exceed 20 characters")
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "estimated_ready_time")
    private LocalDateTime estimatedReadyTime;

    @Column(name = "ready_at")
    private LocalDateTime readyAt;

    @Column(name = "served_at")
    private LocalDateTime servedAt;

    // Store delivery address as JSON for flexibility
    @Column(name = "delivery_address", columnDefinition = "jsonb")
    private Map<String, Object> deliveryAddress;

    // Relationships
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Constructors
    public Order() {
        super();
    }

    public Order(Restaurant restaurant, OrderType orderType) {
        this();
        this.restaurant = restaurant;
        this.orderType = orderType;
        this.orderNumber = generateOrderNumber();
    }

    public Order(Restaurant restaurant, RestaurantTable table, UUID sessionId) {
        this(restaurant, OrderType.DINE_IN);
        this.table = table;
        this.sessionId = sessionId;
    }

    // Business methods
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

    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED || status == OrderStatus.SERVED;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }

    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING || status == OrderStatus.CONFIRMED;
    }

    public boolean canBeModified() {
        return status == OrderStatus.PENDING;
    }

    public boolean isDineIn() {
        return orderType == OrderType.DINE_IN;
    }

    public boolean isTakeaway() {
        return orderType == OrderType.TAKEAWAY;
    }

    public boolean isDelivery() {
        return orderType == OrderType.DELIVERY;
    }

    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }

    public void confirm() {
        if (isPending()) {
            this.status = OrderStatus.CONFIRMED;
            calculateEstimatedReadyTime();
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
            this.readyAt = LocalDateTime.now();
        }
    }

    public void markServed() {
        if (isReady()) {
            this.status = OrderStatus.SERVED;
            this.servedAt = LocalDateTime.now();
        }
    }

    public void complete() {
        if (status == OrderStatus.SERVED || status == OrderStatus.READY) {
            this.status = OrderStatus.COMPLETED;
            if (servedAt == null) {
                this.servedAt = LocalDateTime.now();
            }
        }
    }

    public void cancel() {
        if (canBeCancelled()) {
            this.status = OrderStatus.CANCELLED;
        }
    }

    public void recalculateTotals() {
        this.subtotal = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate tax
        if (restaurant.getTaxRate() != null) {
            this.taxAmount = subtotal.multiply(restaurant.getTaxRate());
        }

        // Calculate service charge
        if (restaurant.getServiceChargeRate() != null) {
            this.serviceCharge = subtotal.multiply(restaurant.getServiceChargeRate());
        }

        // Calculate delivery fee for delivery orders
        if (isDelivery() && restaurant.getDeliveryFee() != null) {
            this.deliveryFee = restaurant.getDeliveryFee();
        }

        // Calculate total
        this.totalAmount = subtotal
                .add(taxAmount)
                .add(serviceCharge)
                .add(deliveryFee)
                .subtract(discountAmount);
    }

    public void addItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
        recalculateTotals();
    }

    public void removeItem(OrderItem item) {
        orderItems.remove(item);
        recalculateTotals();
    }

    public int getTotalItemCount() {
        return orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    public String getDisplayOrderNumber() {
        return "#" + orderNumber;
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

    private void calculateEstimatedReadyTime() {
        if (orderItems.isEmpty()) {
            return;
        }

        int maxPrepTime = orderItems.stream()
                .mapToInt(item -> item.getMenuItem().getPreparationTimeMinutes())
                .max()
                .orElse(15);

        this.estimatedReadyTime = LocalDateTime.now().plusMinutes(maxPrepTime);
    }

    private String generateOrderNumber() {
        // Generate order number based on timestamp and random suffix
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return String.format("%d%03d", timestamp % 100000, random);
    }

    // Getters and setters
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getEstimatedReadyTime() {
        return estimatedReadyTime;
    }

    public void setEstimatedReadyTime(LocalDateTime estimatedReadyTime) {
        this.estimatedReadyTime = estimatedReadyTime;
    }

    public LocalDateTime getReadyAt() {
        return readyAt;
    }

    public void setReadyAt(LocalDateTime readyAt) {
        this.readyAt = readyAt;
    }

    public LocalDateTime getServedAt() {
        return servedAt;
    }

    public void setServedAt(LocalDateTime servedAt) {
        this.servedAt = servedAt;
    }

    public Map<String, Object> getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Map<String, Object> deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
