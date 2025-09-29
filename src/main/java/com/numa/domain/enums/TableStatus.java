package com.numa.domain.enums;

/**
 * Enumeration for table status values.
 * Tracks the current state of restaurant tables.
 */
public enum TableStatus {
    /**
     * Table is available for new customers
     */
    AVAILABLE,
    
    /**
     * Table is currently occupied by customers
     */
    OCCUPIED,
    
    /**
     * Table is reserved for a specific time
     */
    RESERVED,
    
    /**
     * Table needs cleaning before next use
     */
    NEEDS_CLEANING,
    
    /**
     * Table is temporarily out of service
     */
    OUT_OF_SERVICE
}
