package com.numa.repository;

import com.numa.domain.entity.User;
import com.numa.domain.enums.UserRole;
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
 * Repository interface for User entity operations.
 * Provides data access methods for user management and authentication.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find active user by email
     */
    Optional<User> findByEmailAndStatus(String email, String status);

    /**
     * Find users by restaurant ID
     */
    List<User> findByRestaurantId(UUID restaurantId);

    /**
     * Find active users by restaurant ID
     */
    @Query("SELECT u FROM User u WHERE u.restaurant.id = :restaurantId AND u.status = 'ACTIVE'")
    List<User> findActiveUsersByRestaurantId(@Param("restaurantId") UUID restaurantId);

    /**
     * Find users by restaurant ID with pagination
     */
    Page<User> findByRestaurantId(UUID restaurantId, Pageable pageable);

    /**
     * Find users by role and restaurant ID
     */
    @Query("SELECT u FROM User u WHERE u.restaurant.id = :restaurantId AND u.role = :role")
    List<User> findByRestaurantIdAndRole(@Param("restaurantId") UUID restaurantId, @Param("role") UserRole role);

    /**
     * Find restaurant owners
     */
    @Query("SELECT u FROM User u WHERE u.role = 'OWNER' AND u.status = 'ACTIVE'")
    List<User> findRestaurantOwners();

    /**
     * Find restaurant managers and owners
     */
    @Query("SELECT u FROM User u WHERE u.restaurant.id = :restaurantId AND " +
           "u.role IN ('OWNER', 'MANAGER') AND u.status = 'ACTIVE'")
    List<User> findRestaurantManagers(@Param("restaurantId") UUID restaurantId);

    /**
     * Check if email exists (case-insensitive)
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    /**
     * Find user by email verification token
     */
    Optional<User> findByEmailVerificationToken(String token);

    /**
     * Find user by password reset token
     */
    Optional<User> findByPasswordResetToken(String token);

    /**
     * Find users with expired password reset tokens
     */
    @Query("SELECT u FROM User u WHERE u.passwordResetToken IS NOT NULL AND " +
           "u.passwordResetExpiresAt < :now")
    List<User> findUsersWithExpiredPasswordResetTokens(@Param("now") LocalDateTime now);

    /**
     * Find unverified users older than specified date
     */
    @Query("SELECT u FROM User u WHERE u.emailVerified = false AND u.createdAt < :cutoffDate")
    List<User> findUnverifiedUsersOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Search users by name or email within a restaurant
     */
    @Query("SELECT u FROM User u WHERE u.restaurant.id = :restaurantId AND " +
           "(LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> searchUsersByRestaurant(@Param("restaurantId") UUID restaurantId, 
                                     @Param("searchTerm") String searchTerm, 
                                     Pageable pageable);

    /**
     * Count active users by restaurant
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.restaurant.id = :restaurantId AND u.status = 'ACTIVE'")
    long countActiveUsersByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find users by status
     */
    List<User> findByStatus(String status);

    /**
     * Find users who haven't logged in recently
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt IS NULL OR u.lastLoginAt < :cutoffDate")
    List<User> findInactiveUsers(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Find users with role in specific restaurant
     */
    @Query("SELECT u FROM User u WHERE u.restaurant.id = :restaurantId AND u.role = :role AND u.status = 'ACTIVE'")
    List<User> findActiveUsersByRestaurantAndRole(@Param("restaurantId") UUID restaurantId, @Param("role") UserRole role);
}
