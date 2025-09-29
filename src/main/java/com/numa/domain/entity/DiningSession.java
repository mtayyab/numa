package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import com.numa.domain.enums.SessionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DiningSession entity representing a dining session at a table.
 * Manages group ordering, guest management, and bill splitting.
 */
@Entity
@Table(name = "dining_sessions", indexes = {
    @Index(name = "idx_sessions_restaurant_id", columnList = "restaurant_id"),
    @Index(name = "idx_sessions_table_id", columnList = "table_id"),
    @Index(name = "idx_sessions_code", columnList = "session_code"),
    @Index(name = "idx_sessions_status", columnList = "status"),
    @Index(name = "idx_sessions_restaurant_status_date", columnList = "restaurant_id, status, started_at")
})
public class DiningSession extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable table;

    @NotBlank(message = "Session code is required")
    @Size(max = 10, message = "Session code must not exceed 10 characters")
    @Column(name = "session_code", nullable = false, unique = true)
    private String sessionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status = SessionStatus.ACTIVE;

    @Min(value = 1, message = "Guest count must be at least 1")
    @Max(value = 20, message = "Guest count must not exceed 20")
    @Column(name = "guest_count", nullable = false)
    private Integer guestCount = 1;

    @Size(max = 255, message = "Host name must not exceed 255 characters")
    @Column(name = "host_name")
    private String hostName;

    @Size(max = 20, message = "Host phone must not exceed 20 characters")
    @Column(name = "host_phone")
    private String hostPhone;

    @Size(max = 1000, message = "Special requests must not exceed 1000 characters")
    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;

    @DecimalMin(value = "0.0", message = "Total amount must be non-negative")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Tip amount must be non-negative")
    @Column(name = "tip_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal tipAmount = BigDecimal.ZERO;

    @Size(max = 20, message = "Payment status must not exceed 20 characters")
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "PENDING";

    @Column(name = "waiter_called", nullable = false)
    private Boolean waiterCalled = false;

    @Column(name = "waiter_call_time")
    private LocalDateTime waiterCallTime;

    @Column(name = "waiter_response_time")
    private LocalDateTime waiterResponseTime;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    // Relationships
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SessionGuest> guests = new ArrayList<>();

    @OneToMany(mappedBy = "sessionId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BillSplit> billSplits = new ArrayList<>();

    // Constructors
    public DiningSession() {
        super();
        this.startedAt = LocalDateTime.now();
    }

    public DiningSession(Restaurant restaurant, RestaurantTable table) {
        this();
        this.restaurant = restaurant;
        this.table = table;
        this.sessionCode = generateSessionCode();
    }

    public DiningSession(Restaurant restaurant, RestaurantTable table, String hostName) {
        this(restaurant, table);
        this.hostName = hostName;
    }

    // Business methods
    public boolean isActive() {
        return status == SessionStatus.ACTIVE;
    }

    public boolean isPaused() {
        return status == SessionStatus.PAUSED;
    }

    public boolean isCompleted() {
        return status == SessionStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return status == SessionStatus.CANCELLED;
    }

    public boolean isAwaitingPayment() {
        return status == SessionStatus.AWAITING_PAYMENT;
    }

    public boolean canAcceptOrders() {
        return isActive();
    }

    public boolean canAddGuests() {
        return isActive() && guests.size() < table.getCapacity();
    }

    public void activate() {
        this.status = SessionStatus.ACTIVE;
        this.table.occupy(getId());
    }

    public void pause() {
        if (isActive()) {
            this.status = SessionStatus.PAUSED;
        }
    }

    public void resume() {
        if (isPaused()) {
            this.status = SessionStatus.ACTIVE;
        }
    }

    public void requestPayment() {
        if (isActive() || isPaused()) {
            this.status = SessionStatus.AWAITING_PAYMENT;
            calculateTotalAmount();
        }
    }

    public void complete() {
        this.status = SessionStatus.COMPLETED;
        this.endedAt = LocalDateTime.now();
        this.table.makeAvailable();
    }

    public void cancel() {
        this.status = SessionStatus.CANCELLED;
        this.endedAt = LocalDateTime.now();
        this.table.makeAvailable();
    }

    public void callWaiter() {
        this.waiterCalled = true;
        this.waiterCallTime = LocalDateTime.now();
        this.waiterResponseTime = null;
    }

    public void waiterResponded() {
        if (waiterCalled) {
            this.waiterResponseTime = LocalDateTime.now();
            this.waiterCalled = false;
        }
    }

    public void addGuest(SessionGuest guest) {
        if (canAddGuests()) {
            guests.add(guest);
            guest.setSession(this);
            updateGuestCount();
        }
    }

    public void removeGuest(SessionGuest guest) {
        guests.remove(guest);
        updateGuestCount();
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setSessionId(getId());
        calculateTotalAmount();
    }

    public SessionGuest getHost() {
        return guests.stream()
                .filter(SessionGuest::isHost)
                .findFirst()
                .orElse(null);
    }

    public List<SessionGuest> getNonHostGuests() {
        return guests.stream()
                .filter(guest -> !guest.isHost())
                .toList();
    }

    public List<Order> getActiveOrders() {
        return orders.stream()
                .filter(order -> !order.isCancelled())
                .toList();
    }

    public List<Order> getPendingOrders() {
        return orders.stream()
                .filter(Order::isPending)
                .toList();
    }

    public BigDecimal getTotalOrderAmount() {
        return getActiveOrders().stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long getSessionDurationMinutes() {
        LocalDateTime endTime = endedAt != null ? endedAt : LocalDateTime.now();
        return java.time.Duration.between(startedAt, endTime).toMinutes();
    }

    public boolean hasUnpaidOrders() {
        return getActiveOrders().stream()
                .anyMatch(order -> !"PAID".equals(order.getPaymentStatus()));
    }

    public boolean hasWaiterRequest() {
        return waiterCalled && waiterResponseTime == null;
    }

    public String getDisplayCode() {
        return sessionCode;
    }

    public String getQrCodeUrl() {
        return String.format("/restaurant/%s/session/%s", 
                           restaurant.getSlug(), 
                           sessionCode);
    }

    private void updateGuestCount() {
        this.guestCount = guests.size();
    }

    private void calculateTotalAmount() {
        this.totalAmount = getTotalOrderAmount().add(tipAmount);
    }

    private String generateSessionCode() {
        // Generate a 6-character alphanumeric code
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return code.toString();
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

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostPhone() {
        return hostPhone;
    }

    public void setHostPhone(String hostPhone) {
        this.hostPhone = hostPhone;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(BigDecimal tipAmount) {
        this.tipAmount = tipAmount;
        calculateTotalAmount();
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Boolean getWaiterCalled() {
        return waiterCalled;
    }

    public void setWaiterCalled(Boolean waiterCalled) {
        this.waiterCalled = waiterCalled;
    }

    public LocalDateTime getWaiterCallTime() {
        return waiterCallTime;
    }

    public void setWaiterCallTime(LocalDateTime waiterCallTime) {
        this.waiterCallTime = waiterCallTime;
    }

    public LocalDateTime getWaiterResponseTime() {
        return waiterResponseTime;
    }

    public void setWaiterResponseTime(LocalDateTime waiterResponseTime) {
        this.waiterResponseTime = waiterResponseTime;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public List<SessionGuest> getGuests() {
        return guests;
    }

    public void setGuests(List<SessionGuest> guests) {
        this.guests = guests;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<BillSplit> getBillSplits() {
        return billSplits;
    }

    public void setBillSplits(List<BillSplit> billSplits) {
        this.billSplits = billSplits;
    }
}
