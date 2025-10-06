package com.numa.dto.response;

import com.numa.domain.enums.SessionStatus;
import com.numa.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for active session information
 */
public class ActiveSessionResponse {
    
    private UUID sessionId;
    private String sessionCode;
    private SessionStatus status;
    private String tableNumber;
    private String tableLocation;
    private Integer guestCount;
    private String hostName;
    private String hostPhone;
    private String specialRequests;
    private BigDecimal totalAmount;
    private BigDecimal tipAmount;
    private String paymentStatus;
    private Boolean waiterCalled;
    private LocalDateTime waiterCallTime;
    private LocalDateTime waiterResponseTime;
    private LocalDateTime startedAt;
    private LocalDateTime lastActivityAt;
    private List<SessionGuestInfo> guests;
    private List<OrderSummary> orders;
    private List<OrderSummary> cartItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ActiveSessionResponse() {}
    
    public ActiveSessionResponse(UUID sessionId, String sessionCode, SessionStatus status, String tableNumber,
                               String tableLocation, Integer guestCount, String hostName, String hostPhone,
                               String specialRequests, BigDecimal totalAmount, BigDecimal tipAmount,
                               String paymentStatus, Boolean waiterCalled, LocalDateTime waiterCallTime,
                               LocalDateTime waiterResponseTime, LocalDateTime startedAt, LocalDateTime lastActivityAt,
                               List<SessionGuestInfo> guests, List<OrderSummary> orders, List<OrderSummary> cartItems,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.sessionId = sessionId;
        this.sessionCode = sessionCode;
        this.status = status;
        this.tableNumber = tableNumber;
        this.tableLocation = tableLocation;
        this.guestCount = guestCount;
        this.hostName = hostName;
        this.hostPhone = hostPhone;
        this.specialRequests = specialRequests;
        this.totalAmount = totalAmount;
        this.tipAmount = tipAmount;
        this.paymentStatus = paymentStatus;
        this.waiterCalled = waiterCalled;
        this.waiterCallTime = waiterCallTime;
        this.waiterResponseTime = waiterResponseTime;
        this.startedAt = startedAt;
        this.lastActivityAt = lastActivityAt;
        this.guests = guests;
        this.orders = orders;
        this.cartItems = cartItems;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }
    public String getSessionCode() { return sessionCode; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }
    public String getTableLocation() { return tableLocation; }
    public void setTableLocation(String tableLocation) { this.tableLocation = tableLocation; }
    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }
    public String getHostPhone() { return hostPhone; }
    public void setHostPhone(String hostPhone) { this.hostPhone = hostPhone; }
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getTipAmount() { return tipAmount; }
    public void setTipAmount(BigDecimal tipAmount) { this.tipAmount = tipAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public Boolean getWaiterCalled() { return waiterCalled; }
    public void setWaiterCalled(Boolean waiterCalled) { this.waiterCalled = waiterCalled; }
    public LocalDateTime getWaiterCallTime() { return waiterCallTime; }
    public void setWaiterCallTime(LocalDateTime waiterCallTime) { this.waiterCallTime = waiterCallTime; }
    public LocalDateTime getWaiterResponseTime() { return waiterResponseTime; }
    public void setWaiterResponseTime(LocalDateTime waiterResponseTime) { this.waiterResponseTime = waiterResponseTime; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getLastActivityAt() { return lastActivityAt; }
    public void setLastActivityAt(LocalDateTime lastActivityAt) { this.lastActivityAt = lastActivityAt; }
    public List<SessionGuestInfo> getGuests() { return guests; }
    public void setGuests(List<SessionGuestInfo> guests) { this.guests = guests; }
    public List<OrderSummary> getOrders() { return orders; }
    public void setOrders(List<OrderSummary> orders) { this.orders = orders; }
    public List<OrderSummary> getCartItems() { return cartItems; }
    public void setCartItems(List<OrderSummary> cartItems) { this.cartItems = cartItems; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    /**
     * Inner class for session guest information
     */
    public static class SessionGuestInfo {
        private UUID guestId;
        private String guestName;
        private String guestPhone;
        private Boolean isHost;
        private LocalDateTime joinedAt;
        private LocalDateTime lastActivityAt;
        
        public SessionGuestInfo() {}
        
        public SessionGuestInfo(UUID guestId, String guestName, String guestPhone, Boolean isHost,
                              LocalDateTime joinedAt, LocalDateTime lastActivityAt) {
            this.guestId = guestId;
            this.guestName = guestName;
            this.guestPhone = guestPhone;
            this.isHost = isHost;
            this.joinedAt = joinedAt;
            this.lastActivityAt = lastActivityAt;
        }
        
        // Getters and setters
        public UUID getGuestId() { return guestId; }
        public void setGuestId(UUID guestId) { this.guestId = guestId; }
        public String getGuestName() { return guestName; }
        public void setGuestName(String guestName) { this.guestName = guestName; }
        public String getGuestPhone() { return guestPhone; }
        public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }
        public Boolean getIsHost() { return isHost; }
        public void setIsHost(Boolean isHost) { this.isHost = isHost; }
        public LocalDateTime getJoinedAt() { return joinedAt; }
        public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
        public LocalDateTime getLastActivityAt() { return lastActivityAt; }
        public void setLastActivityAt(LocalDateTime lastActivityAt) { this.lastActivityAt = lastActivityAt; }
    }
    
    /**
     * Inner class for order summary
     */
    public static class OrderSummary {
        private UUID orderId;
        private String customerName;
        private OrderStatus status;
        private BigDecimal totalAmount;
        private LocalDateTime createdAt;
        private List<OrderItemSummary> items;
        
        public OrderSummary() {}
        
        public OrderSummary(UUID orderId, String customerName, OrderStatus status, BigDecimal totalAmount,
                          LocalDateTime createdAt, List<OrderItemSummary> items) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.status = status;
            this.totalAmount = totalAmount;
            this.createdAt = createdAt;
            this.items = items;
        }
        
        // Getters and setters
        public UUID getOrderId() { return orderId; }
        public void setOrderId(UUID orderId) { this.orderId = orderId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public OrderStatus getStatus() { return status; }
        public void setStatus(OrderStatus status) { this.status = status; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public List<OrderItemSummary> getItems() { return items; }
        public void setItems(List<OrderItemSummary> items) { this.items = items; }
    }
    
    /**
     * Inner class for order item summary
     */
    public static class OrderItemSummary {
        private UUID itemId;
        private String itemName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private String specialInstructions;
        
        public OrderItemSummary() {}
        
        public OrderItemSummary(UUID itemId, String itemName, Integer quantity, BigDecimal unitPrice,
                              BigDecimal totalPrice, String specialInstructions) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = totalPrice;
            this.specialInstructions = specialInstructions;
        }
        
        // Getters and setters
        public UUID getItemId() { return itemId; }
        public void setItemId(UUID itemId) { this.itemId = itemId; }
        public String getItemName() { return itemName; }
        public void setItemName(String itemName) { this.itemName = itemName; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getTotalPrice() { return totalPrice; }
        public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
        public String getSpecialInstructions() { return specialInstructions; }
        public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
    }
}
