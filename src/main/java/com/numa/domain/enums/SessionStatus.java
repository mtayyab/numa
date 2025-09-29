package com.numa.domain.enums;

/**
 * Enumeration for dining session status values.
 * Tracks the lifecycle of a dining session from start to completion.
 */
public enum SessionStatus {
    /**
     * Session is active and accepting orders
     */
    ACTIVE,
    
    /**
     * Session is paused (no new orders accepted)
     */
    PAUSED,
    
    /**
     * Session is waiting for payment
     */
    AWAITING_PAYMENT,
    
    /**
     * Session has been completed and closed
     */
    COMPLETED,
    
    /**
     * Session was cancelled before completion
     */
    CANCELLED
}
