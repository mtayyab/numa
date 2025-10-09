package com.numa.domain.enums;

/**
 * Enum for voucher discount types.
 */
public enum VoucherType {
    PERCENTAGE("Percentage Discount"),
    FIXED_AMOUNT("Fixed Amount Discount");

    private final String displayName;

    VoucherType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
