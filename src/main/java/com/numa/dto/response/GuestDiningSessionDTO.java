package com.numa.dto.response;

import com.numa.domain.enums.SessionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Lightweight DTO for guest dining session information.
 * Contains only the fields needed for guest interface to avoid lazy loading issues.
 */
public class GuestDiningSessionDTO {
    
    private UUID id;
    private String sessionCode;
    private SessionStatus status;
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
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public GuestDiningSessionDTO() {}
    
    public GuestDiningSessionDTO(UUID id, String sessionCode, SessionStatus status, Integer guestCount,
                                String hostName, String hostPhone, String specialRequests,
                                BigDecimal totalAmount, BigDecimal tipAmount, String paymentStatus,
                                Boolean waiterCalled, LocalDateTime waiterCallTime, LocalDateTime waiterResponseTime,
                                LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.sessionCode = sessionCode;
        this.status = status;
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
        this.endedAt = endedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getSessionCode() { return sessionCode; }
    public void setSessionCode(String sessionCode) { this.sessionCode = sessionCode; }
    public SessionStatus getStatus() { return status; }
    public void setStatus(SessionStatus status) { this.status = status; }
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
    public LocalDateTime getEndedAt() { return endedAt; }
    public void setEndedAt(LocalDateTime endedAt) { this.endedAt = endedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
