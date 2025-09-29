package com.numa.repository;

import com.numa.domain.entity.Order;
import com.numa.domain.enums.OrderStatus;
import com.numa.domain.enums.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Order entity operations.
 * Provides data access methods for order management and analytics.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Find order by restaurant and order number
     */
    Optional<Order> findByRestaurantIdAndOrderNumber(UUID restaurantId, String orderNumber);

    /**
     * Find orders by restaurant ID
     */
    Page<Order> findByRestaurantIdOrderByCreatedAtDesc(UUID restaurantId, Pageable pageable);

    /**
     * Find orders by restaurant and status
     */
    List<Order> findByRestaurantIdAndStatusOrderByCreatedAtAsc(UUID restaurantId, OrderStatus status);

    /**
     * Find orders by session ID
     */
    List<Order> findBySessionIdOrderByCreatedAtAsc(UUID sessionId);

    /**
     * Find orders by table ID
     */
    List<Order> findByTableIdOrderByCreatedAtDesc(UUID tableId);

    /**
     * Find active orders by restaurant (not completed or cancelled)
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.status NOT IN ('COMPLETED', 'CANCELLED', 'REFUNDED') " +
           "ORDER BY o.createdAt ASC")
    List<Order> findActiveOrdersByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find pending orders by restaurant
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.status = 'PENDING' ORDER BY o.createdAt ASC")
    List<Order> findPendingOrdersByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find orders ready for pickup/serving
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.status = 'READY' ORDER BY o.readyAt ASC")
    List<Order> findReadyOrdersByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find orders being prepared
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.status = 'PREPARING' ORDER BY o.estimatedReadyTime ASC")
    List<Order> findPreparingOrdersByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find orders by date range
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY o.createdAt DESC")
    List<Order> findOrdersByDateRange(@Param("restaurantId") UUID restaurantId,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Find orders by order type
     */
    List<Order> findByRestaurantIdAndOrderTypeOrderByCreatedAtDesc(UUID restaurantId, OrderType orderType);

    /**
     * Find overdue orders (past estimated ready time)
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.status IN ('CONFIRMED', 'PREPARING') " +
           "AND o.estimatedReadyTime < :now " +
           "ORDER BY o.estimatedReadyTime ASC")
    List<Order> findOverdueOrders(@Param("restaurantId") UUID restaurantId, @Param("now") LocalDateTime now);

    /**
     * Count orders by status and restaurant
     */
    long countByRestaurantIdAndStatus(UUID restaurantId, OrderStatus status);

    /**
     * Count orders by restaurant and date range
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate")
    long countOrdersByDateRange(@Param("restaurantId") UUID restaurantId,
                               @Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate total revenue by restaurant and date range
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "AND o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueByDateRange(@Param("restaurantId") UUID restaurantId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate average order value by restaurant and date range
     */
    @Query("SELECT AVG(o.totalAmount) FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "AND o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal calculateAverageOrderValue(@Param("restaurantId") UUID restaurantId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    /**
     * Find orders with unpaid status
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.paymentStatus != 'PAID' " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "ORDER BY o.createdAt ASC")
    List<Order> findUnpaidOrders(@Param("restaurantId") UUID restaurantId);

    /**
     * Find orders by customer phone
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.customerPhone = :phone " +
           "ORDER BY o.createdAt DESC")
    List<Order> findOrdersByCustomerPhone(@Param("restaurantId") UUID restaurantId, @Param("phone") String phone);

    /**
     * Find orders by customer email
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.customerEmail = :email " +
           "ORDER BY o.createdAt DESC")
    List<Order> findOrdersByCustomerEmail(@Param("restaurantId") UUID restaurantId, @Param("email") String email);

    /**
     * Find recent orders for analytics (last 30 days)
     */
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt >= :thirtyDaysAgo " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(@Param("restaurantId") UUID restaurantId, 
                               @Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    /**
     * Get hourly order counts for analytics
     */
    @Query("SELECT HOUR(o.createdAt) as hour, COUNT(o) as orderCount " +
           "FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY HOUR(o.createdAt) " +
           "ORDER BY hour")
    List<Object[]> getHourlyOrderCounts(@Param("restaurantId") UUID restaurantId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * Get daily order counts for analytics
     */
    @Query("SELECT DATE(o.createdAt) as orderDate, COUNT(o) as orderCount, SUM(o.totalAmount) as revenue " +
           "FROM Order o WHERE o.restaurant.id = :restaurantId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY DATE(o.createdAt) " +
           "ORDER BY orderDate DESC")
    List<Object[]> getDailyOrderStats(@Param("restaurantId") UUID restaurantId,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Check if order number exists in restaurant
     */
    boolean existsByRestaurantIdAndOrderNumber(UUID restaurantId, String orderNumber);
}
