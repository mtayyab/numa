package com.numa.repository;

import com.numa.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for OrderItem entity operations.
 * Provides data access methods for order item management and analytics.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    /**
     * Find order items by order ID
     */
    List<OrderItem> findByOrderId(UUID orderId);

    /**
     * Find order items by order ID ordered by creation time
     */
    List<OrderItem> findByOrderIdOrderByCreatedAtAsc(UUID orderId);

    /**
     * Find order items by menu item ID
     */
    List<OrderItem> findByMenuItemId(UUID menuItemId);

    /**
     * Find order items by status
     */
    List<OrderItem> findByStatus(String status);

    /**
     * Count order items by order ID
     */
    long countByOrderId(UUID orderId);

    /**
     * Count order items by menu item ID
     */
    long countByMenuItemId(UUID menuItemId);

    /**
     * Find order items by restaurant ID
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.restaurant.id = :restaurantId")
    List<OrderItem> findByRestaurantId(@Param("restaurantId") UUID restaurantId);

    /**
     * Find order items by restaurant ID and date range
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.restaurant.id = :restaurantId " +
           "AND oi.createdAt BETWEEN :startDate AND :endDate")
    List<OrderItem> findByRestaurantIdAndDateRange(@Param("restaurantId") UUID restaurantId,
                                                  @Param("startDate") java.time.LocalDateTime startDate,
                                                  @Param("endDate") java.time.LocalDateTime endDate);

    /**
     * Find most popular menu items by restaurant
     */
    @Query("SELECT oi.menuItem.id, oi.menuItem.name, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi JOIN oi.order o WHERE o.restaurant.id = :restaurantId " +
           "GROUP BY oi.menuItem.id, oi.menuItem.name ORDER BY totalQuantity DESC")
    List<Object[]> findMostPopularMenuItemsByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Calculate total revenue by menu item
     */
    @Query("SELECT oi.menuItem.id, oi.menuItem.name, SUM(oi.totalPrice) as totalRevenue " +
           "FROM OrderItem oi JOIN oi.order o WHERE o.restaurant.id = :restaurantId " +
           "GROUP BY oi.menuItem.id, oi.menuItem.name ORDER BY totalRevenue DESC")
    List<Object[]> calculateRevenueByMenuItem(@Param("restaurantId") UUID restaurantId);

    /**
     * Find order items by session ID
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.sessionId = :sessionId")
    List<OrderItem> findBySessionId(@Param("sessionId") UUID sessionId);

    /**
     * Find order items by session ID and status
     */
    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.sessionId = :sessionId AND oi.status = :status")
    List<OrderItem> findBySessionIdAndStatus(@Param("sessionId") UUID sessionId, @Param("status") String status);

    /**
     * Calculate total quantity by menu item for a session
     */
    @Query("SELECT oi.menuItem.id, oi.menuItem.name, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi JOIN oi.order o WHERE o.sessionId = :sessionId " +
           "GROUP BY oi.menuItem.id, oi.menuItem.name ORDER BY totalQuantity DESC")
    List<Object[]> calculateQuantityByMenuItemForSession(@Param("sessionId") UUID sessionId);

    /**
     * Find order items with special instructions
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.specialInstructions IS NOT NULL AND oi.specialInstructions != ''")
    List<OrderItem> findWithSpecialInstructions();

    /**
     * Find order items by status
     */
    List<OrderItem> findByStatus(com.numa.domain.enums.OrderStatus status);

    /**
     * Find order items by prepared time range
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.preparedAt BETWEEN :startTime AND :endTime")
    List<OrderItem> findByPreparedTimeRange(@Param("startTime") java.time.LocalDateTime startTime,
                                           @Param("endTime") java.time.LocalDateTime endTime);

    /**
     * Calculate average preparation time by menu item
     */
    @Query("SELECT oi.menuItem.id, oi.menuItem.name, AVG(TIMESTAMPDIFF(MINUTE, oi.createdAt, oi.preparedAt)) as avgPrepTime " +
           "FROM OrderItem oi WHERE oi.preparedAt IS NOT NULL " +
           "GROUP BY oi.menuItem.id, oi.menuItem.name ORDER BY avgPrepTime ASC")
    List<Object[]> calculateAveragePreparationTimeByMenuItem();
}
