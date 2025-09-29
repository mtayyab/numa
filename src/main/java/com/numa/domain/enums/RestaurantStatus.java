package com.numa.domain.enums;

/**
 * Enumeration for restaurant status values.
 * Defines the possible states of a restaurant in the system.
 */
public enum RestaurantStatus {
    /**
     * Restaurant is active and accepting orders
     */
    ACTIVE,
    
    /**
     * Restaurant is temporarily closed but still in the system
     */
    INACTIVE,
    
    /**
     * Restaurant is suspended (admin action)
     */
    SUSPENDED,
    
    /**
     * Restaurant is pending approval for activation
     */
    PENDING_APPROVAL,
    
    /**
     * Restaurant account is permanently deactivated
     */
    DEACTIVATED
}
