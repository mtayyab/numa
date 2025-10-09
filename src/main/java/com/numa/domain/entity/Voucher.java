package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import com.numa.domain.enums.VoucherStatus;
import com.numa.domain.enums.VoucherType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Voucher entity for restaurant discount vouchers.
 * Allows restaurants to create discount vouchers that guests can use.
 */
@Entity
@Table(name = "vouchers")
public class Voucher extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VoucherType type;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "minimum_order_amount", precision = 10, scale = 2)
    private BigDecimal minimumOrderAmount;

    @Column(name = "maximum_discount_amount", precision = 10, scale = 2)
    private BigDecimal maximumDiscountAmount;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VoucherStatus status = VoucherStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    // Constructors
    public Voucher() {}

    public Voucher(String code, String description, VoucherType type, BigDecimal discountValue,
                   BigDecimal minimumOrderAmount, BigDecimal maximumDiscountAmount,
                   LocalDateTime expiresAt, Integer usageLimit, Restaurant restaurant,
                   UUID createdBy, Boolean isPublic, LocalDateTime validFrom) {
        this.code = code;
        this.description = description;
        this.type = type;
        this.discountValue = discountValue;
        this.minimumOrderAmount = minimumOrderAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.expiresAt = expiresAt;
        this.usageLimit = usageLimit;
        this.restaurant = restaurant;
        this.createdBy = createdBy;
        this.isPublic = isPublic;
        this.validFrom = validFrom;
    }

    // Business logic methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isUsageLimitReached() {
        return usageLimit != null && usedCount >= usageLimit;
    }

    public boolean isActive() {
        return status == VoucherStatus.ACTIVE && !isExpired() && !isUsageLimitReached();
    }

    public boolean isValidForOrder(BigDecimal orderAmount) {
        if (!isActive()) {
            return false;
        }
        
        if (minimumOrderAmount != null && orderAmount.compareTo(minimumOrderAmount) < 0) {
            return false;
        }
        
        return true;
    }

    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!isValidForOrder(orderAmount)) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = BigDecimal.ZERO;
        
        if (type == VoucherType.PERCENTAGE) {
            discount = orderAmount.multiply(discountValue).divide(BigDecimal.valueOf(100));
        } else if (type == VoucherType.FIXED_AMOUNT) {
            discount = discountValue;
        }

        // Apply maximum discount limit if set
        if (maximumDiscountAmount != null && discount.compareTo(maximumDiscountAmount) > 0) {
            discount = maximumDiscountAmount;
        }

        // Ensure discount doesn't exceed order amount
        if (discount.compareTo(orderAmount) > 0) {
            discount = orderAmount;
        }

        return discount;
    }

    public void incrementUsage() {
        this.usedCount++;
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public VoucherType getType() { return type; }
    public void setType(VoucherType type) { this.type = type; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }
    public BigDecimal getMinimumOrderAmount() { return minimumOrderAmount; }
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }
    public BigDecimal getMaximumDiscountAmount() { return maximumDiscountAmount; }
    public void setMaximumDiscountAmount(BigDecimal maximumDiscountAmount) { this.maximumDiscountAmount = maximumDiscountAmount; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public Integer getUsedCount() { return usedCount; }
    public void setUsedCount(Integer usedCount) { this.usedCount = usedCount; }
    public VoucherStatus getStatus() { return status; }
    public void setStatus(VoucherStatus status) { this.status = status; }
    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
}
