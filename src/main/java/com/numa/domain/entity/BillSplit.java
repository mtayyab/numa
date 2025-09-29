package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * BillSplit entity representing how the bill is split among session guests.
 * Supports different splitting methods: equal, by percentage, or custom amounts.
 */
@Entity
@Table(name = "bill_splits", indexes = {
    @Index(name = "idx_bill_splits_session_id", columnList = "session_id"),
    @Index(name = "idx_bill_splits_guest_id", columnList = "guest_id")
})
public class BillSplit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private DiningSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private SessionGuest guest;

    @NotBlank(message = "Split type is required")
    @Size(max = 20, message = "Split type must not exceed 20 characters")
    @Column(name = "split_type", nullable = false)
    private String splitType = "EQUAL";

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", message = "Amount must be non-negative")
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @DecimalMin(value = "0.0", message = "Percentage must be non-negative")
    @DecimalMax(value = "100.0", message = "Percentage must not exceed 100")
    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage;

    @Size(max = 20, message = "Payment status must not exceed 20 characters")
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "PENDING";

    @Size(max = 20, message = "Payment method must not exceed 20 characters")
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // Constructors
    public BillSplit() {
        super();
    }

    public BillSplit(DiningSession session, SessionGuest guest, String splitType, BigDecimal amount) {
        this();
        this.session = session;
        this.guest = guest;
        this.splitType = splitType;
        this.amount = amount;
    }

    // Business methods
    public boolean isPending() {
        return "PENDING".equals(paymentStatus);
    }

    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }

    public boolean isPartiallyPaid() {
        return "PARTIALLY_PAID".equals(paymentStatus);
    }

    public boolean isFailed() {
        return "FAILED".equals(paymentStatus);
    }

    public void markAsPaid(String paymentMethod) {
        this.paymentStatus = "PAID";
        this.paymentMethod = paymentMethod;
        this.paidAt = LocalDateTime.now();
    }

    public void markAsPartiallyPaid(String paymentMethod) {
        this.paymentStatus = "PARTIALLY_PAID";
        this.paymentMethod = paymentMethod;
        this.paidAt = LocalDateTime.now();
    }

    public void markAsFailed() {
        this.paymentStatus = "FAILED";
        this.paidAt = null;
    }

    public boolean isEqualSplit() {
        return "EQUAL".equals(splitType);
    }

    public boolean isPercentageSplit() {
        return "PERCENTAGE".equals(splitType);
    }

    public boolean isCustomSplit() {
        return "CUSTOM".equals(splitType);
    }

    public boolean isItemBasedSplit() {
        return "ITEM_BASED".equals(splitType);
    }

    public String getDisplaySplitType() {
        return switch (splitType) {
            case "EQUAL" -> "Equal Split";
            case "PERCENTAGE" -> "Percentage Split";
            case "CUSTOM" -> "Custom Amount";
            case "ITEM_BASED" -> "Item-based Split";
            default -> splitType;
        };
    }

    public String getDisplayAmount() {
        return String.format("%.2f", amount);
    }

    public String getDisplayPercentage() {
        return percentage != null ? String.format("%.1f%%", percentage) : "N/A";
    }

    public String getPaymentStatusDisplay() {
        return switch (paymentStatus) {
            case "PENDING" -> "Pending";
            case "PAID" -> "Paid";
            case "PARTIALLY_PAID" -> "Partially Paid";
            case "FAILED" -> "Failed";
            default -> paymentStatus;
        };
    }

    public BigDecimal getAmountDue() {
        if (isPaid()) {
            return BigDecimal.ZERO;
        }
        return amount;
    }

    // Getters and setters
    public DiningSession getSession() {
        return session;
    }

    public void setSession(DiningSession session) {
        this.session = session;
    }

    public SessionGuest getGuest() {
        return guest;
    }

    public void setGuest(SessionGuest guest) {
        this.guest = guest;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
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

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
