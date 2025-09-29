package com.numa.domain.enums;

/**
 * Enumeration for order types.
 * Defines how the order will be fulfilled.
 */
public enum OrderType {
    /**
     * Customer is dining in at the restaurant
     */
    DINE_IN,
    
    /**
     * Customer is picking up the order
     */
    TAKEAWAY,
    
    /**
     * Order will be delivered to customer
     */
    DELIVERY,
    
    /**
     * Pre-order for future pickup/delivery
     */
    PRE_ORDER
}
