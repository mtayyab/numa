package com.numa.domain.enums;

/**
 * Enumeration for user roles in the system.
 * Defines different permission levels for restaurant staff.
 */
public enum UserRole {
    /**
     * Restaurant owner with full administrative access
     */
    OWNER,
    
    /**
     * Restaurant manager with administrative access
     */
    MANAGER,
    
    /**
     * Restaurant staff with limited access to order management
     */
    STAFF,
    
    /**
     * Kitchen staff with access to order preparation features
     */
    KITCHEN_STAFF,
    
    /**
     * Waiter with access to table and order management
     */
    WAITER
}
