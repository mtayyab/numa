package com.numa.domain.enums;

/**
 * Enumeration for order status values.
 * Tracks the lifecycle of an order from creation to completion.
 */
public enum OrderStatus {
    /**
     * Order has been created but not yet confirmed
     */
    PENDING,
    
    /**
     * Order has been confirmed and sent to kitchen
     */
    CONFIRMED,
    
    /**
     * Order is being prepared in the kitchen
     */
    PREPARING,
    
    /**
     * Order is ready for pickup/serving
     */
    READY,
    
    /**
     * Order has been served to the customer
     */
    SERVED,
    
    /**
     * Order has been completed successfully
     */
    COMPLETED,
    
    /**
     * Order has been cancelled
     */
    CANCELLED,
    
    /**
     * Order has been refunded
     */
    REFUNDED
}
