package com.numa.dto.response;

import com.numa.domain.enums.SessionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for session history information
 */
public class SessionHistoryResponse {
    
    private UUID sessionId;
    private String sessionCode;
    private SessionStatus status;
    private String tableNumber;
    private String tableLocation;
    private Integer guestCount;
    private String hostName;
    private String hostPhone;
    private BigDecimal totalAmount;
    private BigDecimal tipAmount;
    private String paymentStatus;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Long durationMinutes;
    private Integer totalOrders;
    private BigDecimal averageOrderValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public SessionHistoryResponse() {}
    
    public SessionHistoryResponse(UUID sessionId, String sessionCode, SessionStatus status, String tableNumber,
                                String tableLocation, Integer guestCount, String hostName, String hostPhone,
                                BigDecimal totalAmount, BigDecimal tipAmount, String paymentStatus,
                                LocalDateTime startedAt, LocalDateTime endedAt, Long durationMinutes,
                                Integer totalOrders, BigDecimal averageOrderValue, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.sessionId = sessionId;
        this.sessionCode = sessionCode;
        this.status = status;
        this.tableNumber = tableNumber;
        this.tableLocation = tableLocation;
        this.guestCount = guestCount;
        this.hostName = hostName;
        this.hostPhone = hostPhone;
        this.totalAmount = totalAmount;
        this.tipAmount = tipAmount;
        this.paymentStatus = paymentStatus;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.durationMinutes = durationMinutes;
        this.totalOrders = totalOrders;
        this.averageOrderValue = averageOrderValue;
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
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getTipAmount() { return tipAmount; }
    public void setTipAmount(BigDecimal tipAmount) { this.tipAmount = tipAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getEndedAt() { return endedAt; }
    public void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }
    public Long getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Long durationMinutes) { this.durationMinutes = durationMinutes; }
    public Integer getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
    public BigDecimal getAverageOrderValue() { return averageOrderValue; }
    public void setAverageOrderValue(BigDecimal averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
