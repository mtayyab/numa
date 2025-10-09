package com.numa.domain.enums;

/**
 * Enum for voucher status.
 */
public enum VoucherStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    EXPIRED("Expired"),
    USED_UP("Used Up");

    private final String displayName;

    VoucherStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
