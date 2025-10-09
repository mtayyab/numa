package com.numa.dto.response;

import com.numa.domain.enums.VoucherStatus;
import com.numa.domain.enums.VoucherType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for voucher operations.
 */
public class VoucherResponse {

    private UUID id;
    private String code;
    private String description;
    private VoucherType type;
    private BigDecimal discountValue;
    private BigDecimal minimumOrderAmount;
    private BigDecimal maximumDiscountAmount;
    private LocalDateTime expiresAt;
    private Integer usageLimit;
    private Integer usedCount;
    private VoucherStatus status;
    private UUID restaurantId;
    private String restaurantName;
    private UUID createdBy;
    private String createdByName;
    private Boolean isPublic;
    private LocalDateTime validFrom;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Computed fields
    private Boolean isExpired;
    private Boolean isUsageLimitReached;
    private Boolean isActive;
    private Integer remainingUses;

    // Constructors
    public VoucherResponse() {}

    public VoucherResponse(UUID id, String code, String description, VoucherType type,
                          BigDecimal discountValue, BigDecimal minimumOrderAmount,
                          BigDecimal maximumDiscountAmount, LocalDateTime expiresAt,
                          Integer usageLimit, Integer usedCount, VoucherStatus status,
                          UUID restaurantId, String restaurantName, UUID createdBy,
                          String createdByName, Boolean isPublic, LocalDateTime validFrom,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.type = type;
        this.discountValue = discountValue;
        this.minimumOrderAmount = minimumOrderAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.expiresAt = expiresAt;
        this.usageLimit = usageLimit;
        this.usedCount = usedCount;
        this.status = status;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.isPublic = isPublic;
        this.validFrom = validFrom;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        
        // Compute derived fields
        this.isExpired = expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
        this.isUsageLimitReached = usageLimit != null && usedCount >= usageLimit;
        this.isActive = status == VoucherStatus.ACTIVE && !this.isExpired && !this.isUsageLimitReached;
        this.remainingUses = usageLimit != null ? Math.max(0, usageLimit - usedCount) : null;
    }

    // Helper methods
    public String getStatusDisplayName() {
        if (isExpired) return "Expired";
        if (isUsageLimitReached) return "Used Up";
        return status.getDisplayName();
    }

    public String getTypeDisplayName() {
        return type.getDisplayName();
    }

    public String getDiscountDisplayText() {
        if (type == VoucherType.PERCENTAGE) {
            return discountValue + "% off";
        } else {
            return "$" + discountValue + " off";
        }
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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
    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Boolean getIsExpired() { return isExpired; }
    public void setIsExpired(Boolean isExpired) { this.isExpired = isExpired; }
    public Boolean getIsUsageLimitReached() { return isUsageLimitReached; }
    public void setIsUsageLimitReached(Boolean isUsageLimitReached) { this.isUsageLimitReached = isUsageLimitReached; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getRemainingUses() { return remainingUses; }
    public void setRemainingUses(Integer remainingUses) { this.remainingUses = remainingUses; }
}
