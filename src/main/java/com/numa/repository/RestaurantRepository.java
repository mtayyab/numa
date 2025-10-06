package com.numa.repository;

import com.numa.domain.entity.Restaurant;
import com.numa.domain.enums.RestaurantStatus;
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
 * Repository interface for Restaurant entity operations.
 * Provides data access methods for restaurant management.
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    /**
     * Find restaurant by slug
     */
    Optional<Restaurant> findBySlug(String slug);

    /**
     * Find restaurant by email
     */
    Optional<Restaurant> findByEmail(String email);

    /**
     * Find restaurants by status
     */
    List<Restaurant> findByStatus(RestaurantStatus status);

    /**
     * Find active restaurants
     */
    @Query("SELECT r FROM Restaurant r WHERE r.status = 'ACTIVE'")
    List<Restaurant> findActiveRestaurants();

    /**
     * Find restaurants by status with pagination
     */
    Page<Restaurant> findByStatus(RestaurantStatus status, Pageable pageable);

    /**
     * Check if slug exists (case-insensitive)
     */
    @Query("SELECT COUNT(r) > 0 FROM Restaurant r WHERE LOWER(r.slug) = LOWER(:slug)")
    boolean existsBySlugIgnoreCase(@Param("slug") String slug);

    /**
     * Check if email exists (case-insensitive)
     */
    @Query("SELECT COUNT(r) > 0 FROM Restaurant r WHERE LOWER(r.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    /**
     * Find restaurants with expiring subscriptions
     */
    @Query("SELECT r FROM Restaurant r WHERE r.subscriptionExpiresAt BETWEEN :startDate AND :endDate")
    List<Restaurant> findRestaurantsWithExpiringSubscriptions(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Find restaurants by subscription plan
     */
    List<Restaurant> findBySubscriptionPlan(String subscriptionPlan);

    /**
     * Search restaurants by name or description
     */
    @Query("SELECT r FROM Restaurant r WHERE " +
           "LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Restaurant> searchRestaurants(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find restaurants by city
     */
    List<Restaurant> findByCityIgnoreCase(String city);

    /**
     * Find restaurants that support delivery
     */
    @Query("SELECT r FROM Restaurant r WHERE r.deliveryEnabled = true AND r.status = 'ACTIVE'")
    List<Restaurant> findRestaurantsWithDelivery();

    /**
     * Find restaurants created within date range
     */
    @Query("SELECT r FROM Restaurant r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    List<Restaurant> findRestaurantsCreatedBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Count restaurants by status
     */
    long countByStatus(RestaurantStatus status);

    /**
     * Find restaurants with recent activity (orders in last 30 days)
     */
    @Query("SELECT DISTINCT r FROM Restaurant r " +
           "JOIN r.orders o WHERE o.createdAt >= :thirtyDaysAgo")
    List<Restaurant> findRestaurantsWithRecentActivity(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);

    /**
     * Get restaurant basic info by ID (only id, name, slug to avoid lazy loading)
     */
    @Query("SELECT r.id, r.name, r.slug FROM Restaurant r WHERE r.id = :id")
    Object[] findBasicInfoById(@Param("id") UUID id);
}
