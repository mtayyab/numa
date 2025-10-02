package com.numa.repository;

import com.numa.domain.entity.RestaurantTable;
import com.numa.domain.enums.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for RestaurantTable entity operations.
 * Provides data access methods for table management.
 */
@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, UUID> {

    /**
     * Find tables by restaurant ID
     */
    List<RestaurantTable> findByRestaurantIdOrderByTableNumberAsc(UUID restaurantId);

    /**
     * Find table by QR code
     */
    Optional<RestaurantTable> findByQrCode(String qrCode);

    /**
     * Find table by restaurant and table number
     */
    Optional<RestaurantTable> findByRestaurantIdAndTableNumber(UUID restaurantId, String tableNumber);

    /**
     * Find tables by status and restaurant
     */
    List<RestaurantTable> findByRestaurantIdAndStatus(UUID restaurantId, TableStatus status);

    /**
     * Find available tables by restaurant
     */
    @Query("SELECT t FROM RestaurantTable t WHERE t.restaurant.id = :restaurantId AND t.status = 'AVAILABLE' ORDER BY t.tableNumber")
    List<RestaurantTable> findAvailableTablesByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find occupied tables by restaurant
     */
    @Query("SELECT t FROM RestaurantTable t WHERE t.restaurant.id = :restaurantId AND t.status = 'OCCUPIED' ORDER BY t.tableNumber")
    List<RestaurantTable> findOccupiedTablesByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find tables that need cleaning
     */
    @Query("SELECT t FROM RestaurantTable t WHERE t.restaurant.id = :restaurantId AND t.status = 'NEEDS_CLEANING' ORDER BY t.tableNumber")
    List<RestaurantTable> findTablesNeedingCleaning(@Param("restaurantId") UUID restaurantId);

    /**
     * Find table by current session ID
     */
    Optional<RestaurantTable> findByCurrentSessionId(UUID sessionId);

    /**
     * Count tables by restaurant and status
     */
    long countByRestaurantIdAndStatus(UUID restaurantId, TableStatus status);

    /**
     * Count total tables by restaurant
     */
    long countByRestaurantId(UUID restaurantId);

    /**
     * Find tables with capacity greater than or equal to specified value
     */
    @Query("SELECT t FROM RestaurantTable t WHERE t.restaurant.id = :restaurantId AND t.capacity >= :minCapacity AND t.status = 'AVAILABLE' ORDER BY t.capacity, t.tableNumber")
    List<RestaurantTable> findAvailableTablesWithMinCapacity(@Param("restaurantId") UUID restaurantId, @Param("minCapacity") Integer minCapacity);

    /**
     * Check if table number exists in restaurant
     */
    boolean existsByRestaurantIdAndTableNumber(UUID restaurantId, String tableNumber);

    /**
     * Check if table number exists in restaurant excluding specific table
     */
    boolean existsByRestaurantIdAndTableNumberAndIdNot(UUID restaurantId, String tableNumber, UUID tableId);

    /**
     * Check if QR code exists
     */
    boolean existsByQrCode(String qrCode);

    /**
     * Get total seating capacity for restaurant
     */
    @Query("SELECT SUM(t.capacity) FROM RestaurantTable t WHERE t.restaurant.id = :restaurantId")
    Integer getTotalCapacityByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Get available seating capacity for restaurant
     */
    @Query("SELECT SUM(t.capacity) FROM RestaurantTable t WHERE t.restaurant.id = :restaurantId AND t.status = 'AVAILABLE'")
    Integer getAvailableCapacityByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find tables by location description
     */
    @Query("SELECT t FROM RestaurantTable t WHERE t.restaurant.id = :restaurantId AND LOWER(t.locationDescription) LIKE LOWER(CONCAT('%', :location, '%')) ORDER BY t.tableNumber")
    List<RestaurantTable> findTablesByLocation(@Param("restaurantId") UUID restaurantId, @Param("location") String location);
}
