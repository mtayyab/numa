package com.numa.dto.request;

import com.numa.domain.enums.VoucherType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Request DTO for creating and updating vouchers.
 */
public class VoucherRequest {

    @NotBlank(message = "Voucher code is required")
    @Size(min = 3, max = 20, message = "Voucher code must be between 3 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Voucher code must contain only uppercase letters and numbers")
    private String code;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Voucher type is required")
    private VoucherType type;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Discount value cannot exceed 999999.99")
    private BigDecimal discountValue;

    @DecimalMin(value = "0.00", message = "Minimum order amount cannot be negative")
    @DecimalMax(value = "999999.99", message = "Minimum order amount cannot exceed 999999.99")
    private BigDecimal minimumOrderAmount;

    @DecimalMin(value = "0.01", message = "Maximum discount amount must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Maximum discount amount cannot exceed 999999.99")
    private BigDecimal maximumDiscountAmount;

    private LocalDateTime expiresAt;

    @Min(value = 1, message = "Usage limit must be at least 1")
    @Max(value = 100000, message = "Usage limit cannot exceed 100000")
    private Integer usageLimit;

    @NotNull(message = "Restaurant ID is required")
    private UUID restaurantId;

    private Boolean isPublic = true;

    private LocalDateTime validFrom;

    // Validation for percentage vouchers
    public boolean isValidPercentageVoucher() {
        if (type == VoucherType.PERCENTAGE) {
            return discountValue.compareTo(BigDecimal.valueOf(100)) <= 0;
        }
        return true;
    }

    // Validation for fixed amount vouchers
    public boolean isValidFixedAmountVoucher() {
        if (type == VoucherType.FIXED_AMOUNT) {
            return discountValue.compareTo(BigDecimal.ZERO) > 0;
        }
        return true;
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
    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
}
