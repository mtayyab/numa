package com.numa.repository;

import com.numa.domain.entity.DiningSession;
import com.numa.domain.enums.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for DiningSession entity operations.
 * Provides data access methods for session management and analytics.
 */
@Repository
public interface DiningSessionRepository extends JpaRepository<DiningSession, UUID> {

    /**
     * Find session by session code
     */
    Optional<DiningSession> findBySessionCode(String sessionCode);

    /**
     * Find sessions by restaurant ID
     */
    Page<DiningSession> findByRestaurantIdOrderByStartedAtDesc(UUID restaurantId, Pageable pageable);

    /**
     * Find sessions by table ID
     */
    List<DiningSession> findByTableIdOrderByStartedAtDesc(UUID tableId);

    /**
     * Find active sessions by restaurant
     */
    @Query("SELECT s FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.status = 'ACTIVE' ORDER BY s.startedAt ASC")
    List<DiningSession> findActiveSessionsByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find current active session for table
     */
    @Query("SELECT s FROM DiningSession s WHERE s.table.id = :tableId " +
           "AND s.status = 'ACTIVE' ORDER BY s.startedAt DESC")
    Optional<DiningSession> findActiveSessionByTable(@Param("tableId") UUID tableId);
    
    /**
     * Find session by table ID and status
     */
    Optional<DiningSession> findByTableIdAndStatus(UUID tableId, SessionStatus status);

    /**
     * Find sessions by status and restaurant
     */
    List<DiningSession> findByRestaurantIdAndStatusOrderByStartedAtDesc(UUID restaurantId, SessionStatus status);

    /**
     * Find sessions awaiting payment
     */
    @Query("SELECT s FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.status = 'AWAITING_PAYMENT' ORDER BY s.startedAt ASC")
    List<DiningSession> findSessionsAwaitingPayment(@Param("restaurantId") UUID restaurantId);

    /**
     * Find sessions with waiter requests
     */
    @Query("SELECT s FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.waiterCalled = true AND s.waiterResponseTime IS NULL " +
           "ORDER BY s.waiterCallTime ASC")
    List<DiningSession> findSessionsWithWaiterRequests(@Param("restaurantId") UUID restaurantId);

    /**
     * Find sessions by date range
     */
    @Query("SELECT s FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.startedAt BETWEEN :startDate AND :endDate " +
           "ORDER BY s.startedAt DESC")
    List<DiningSession> findSessionsByDateRange(@Param("restaurantId") UUID restaurantId,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    /**
     * Find long-running sessions (over specified hours)
     */
    @Query("SELECT s FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.status = 'ACTIVE' " +
           "AND s.startedAt < :cutoffTime " +
           "ORDER BY s.startedAt ASC")
    List<DiningSession> findLongRunningSessions(@Param("restaurantId") UUID restaurantId,
                                              @Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Count active sessions by restaurant
     */
    @Query("SELECT COUNT(s) FROM DiningSession s WHERE s.restaurant.id = :restaurantId AND s.status = 'ACTIVE'")
    long countActiveSessionsByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Count sessions by status and restaurant
     */
    long countByRestaurantIdAndStatus(UUID restaurantId, SessionStatus status);

    /**
     * Count sessions by date range
     */
    @Query("SELECT COUNT(s) FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.startedAt BETWEEN :startDate AND :endDate")
    long countSessionsByDateRange(@Param("restaurantId") UUID restaurantId,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate average session duration
     */
    @Query("SELECT AVG(TIMESTAMPDIFF(MINUTE, s.startedAt, s.endedAt)) " +
           "FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.endedAt IS NOT NULL " +
           "AND s.startedAt BETWEEN :startDate AND :endDate")
    Double calculateAverageSessionDuration(@Param("restaurantId") UUID restaurantId,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Find sessions with multiple guests
     */
    @Query("SELECT s FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.guestCount > 1 " +
           "AND s.startedAt BETWEEN :startDate AND :endDate " +
           "ORDER BY s.guestCount DESC")
    List<DiningSession> findGroupSessions(@Param("restaurantId") UUID restaurantId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    /**
     * Get session statistics by hour
     */
    @Query("SELECT HOUR(s.startedAt) as hour, COUNT(s) as sessionCount, AVG(s.guestCount) as avgGuestCount " +
           "FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.startedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY HOUR(s.startedAt) " +
           "ORDER BY hour")
    List<Object[]> getHourlySessionStats(@Param("restaurantId") UUID restaurantId,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    /**
     * Get daily session statistics
     */
    @Query("SELECT DATE(s.startedAt) as sessionDate, COUNT(s) as sessionCount, " +
           "AVG(s.guestCount) as avgGuestCount, SUM(s.totalAmount) as totalRevenue " +
           "FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.startedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(s.startedAt) " +
           "ORDER BY sessionDate DESC")
    List<Object[]> getDailySessionStats(@Param("restaurantId") UUID restaurantId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    /**
     * Find sessions by table utilization
     */
    @Query("SELECT s.table.id, COUNT(s) as sessionCount, AVG(TIMESTAMPDIFF(MINUTE, s.startedAt, s.endedAt)) as avgDuration " +
           "FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.endedAt IS NOT NULL " +
           "AND s.startedAt BETWEEN :startDate AND :endDate " +
           "GROUP BY s.table.id " +
           "ORDER BY sessionCount DESC")
    List<Object[]> getTableUtilizationStats(@Param("restaurantId") UUID restaurantId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    /**
     * Check if session code exists
     */
    boolean existsBySessionCode(String sessionCode);

    /**
     * Find recent sessions for analytics (last 30 days)
     */
    @Query("SELECT s FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.startedAt >= :thirtyDaysAgo " +
           "ORDER BY s.startedAt DESC")
    List<DiningSession> findRecentSessions(@Param("restaurantId") UUID restaurantId,
                                         @Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    /**
     * Get revenue statistics for analytics
     */
    @Query("SELECT SUM(s.totalAmount) as totalRevenue, AVG(s.totalAmount) as avgOrderValue " +
           "FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.startedAt BETWEEN :startDate AND :endDate")
    List<Object[]> getRevenueStats(@Param("restaurantId") UUID restaurantId,
                                  @Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate);

    /**
     * Get guest statistics for analytics
     */
    @Query("SELECT SUM(s.guestCount) as totalGuests, AVG(s.guestCount) as avgGuestsPerSession " +
           "FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.startedAt BETWEEN :startDate AND :endDate")
    List<Object[]> getGuestStats(@Param("restaurantId") UUID restaurantId,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate);

    /**
     * Get duration statistics for analytics
     */
    @Query("SELECT AVG(TIMESTAMPDIFF(MINUTE, s.startedAt, s.endedAt)) as avgDurationMinutes " +
           "FROM DiningSession s WHERE s.restaurant.id = :restaurantId " +
           "AND s.startedAt BETWEEN :startDate AND :endDate " +
           "AND s.endedAt IS NOT NULL")
    List<Object[]> getDurationStats(@Param("restaurantId") UUID restaurantId,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    /**
     * Count sessions by restaurant and date range
     */
    long countByRestaurantIdAndStartedAtBetween(UUID restaurantId, LocalDateTime startDate, LocalDateTime endDate);
}
