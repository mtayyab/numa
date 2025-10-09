package com.numa.repository;

import com.numa.domain.entity.Voucher;
import com.numa.domain.enums.VoucherStatus;
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
 * Repository for Voucher entities.
 */
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID> {

    /**
     * Find vouchers by restaurant ID
     */
    List<Voucher> findByRestaurantIdOrderByCreatedAtDesc(UUID restaurantId);

    /**
     * Find vouchers by restaurant ID with pagination
     */
    Page<Voucher> findByRestaurantIdOrderByCreatedAtDesc(UUID restaurantId, Pageable pageable);

    /**
     * Find vouchers by restaurant ID and status
     */
    List<Voucher> findByRestaurantIdAndStatusOrderByCreatedAtDesc(UUID restaurantId, VoucherStatus status);

    /**
     * Find active vouchers for a restaurant
     */
    @Query("SELECT v FROM Voucher v WHERE v.restaurant.id = :restaurantId " +
           "AND v.status = 'ACTIVE' " +
           "AND (v.expiresAt IS NULL OR v.expiresAt > :now) " +
           "AND (v.usageLimit IS NULL OR v.usedCount < v.usageLimit) " +
           "AND (v.validFrom IS NULL OR v.validFrom <= :now) " +
           "ORDER BY v.createdAt DESC")
    List<Voucher> findActiveVouchersForRestaurant(@Param("restaurantId") UUID restaurantId, 
                                                  @Param("now") LocalDateTime now);

    /**
     * Find voucher by code and restaurant ID
     */
    Optional<Voucher> findByCodeAndRestaurantId(String code, UUID restaurantId);

    /**
     * Find voucher by code (for guest usage)
     */
    @Query("SELECT v FROM Voucher v WHERE v.code = :code " +
           "AND v.status = 'ACTIVE' " +
           "AND (v.expiresAt IS NULL OR v.expiresAt > :now) " +
           "AND (v.usageLimit IS NULL OR v.usedCount < v.usageLimit) " +
           "AND (v.validFrom IS NULL OR v.validFrom <= :now) " +
           "AND v.isPublic = true")
    Optional<Voucher> findActiveVoucherByCode(@Param("code") String code, 
                                             @Param("now") LocalDateTime now);

    /**
     * Find vouchers by created by user
     */
    List<Voucher> findByCreatedByOrderByCreatedAtDesc(UUID createdBy);

    /**
     * Find vouchers by restaurant ID and created by user
     */
    List<Voucher> findByRestaurantIdAndCreatedByOrderByCreatedAtDesc(UUID restaurantId, UUID createdBy);

    /**
     * Find expired vouchers
     */
    @Query("SELECT v FROM Voucher v WHERE v.expiresAt IS NOT NULL AND v.expiresAt < :now")
    List<Voucher> findExpiredVouchers(@Param("now") LocalDateTime now);

    /**
     * Find vouchers that have reached usage limit
     */
    @Query("SELECT v FROM Voucher v WHERE v.usageLimit IS NOT NULL AND v.usedCount >= v.usageLimit")
    List<Voucher> findVouchersAtUsageLimit();

    /**
     * Count active vouchers for a restaurant
     */
    @Query("SELECT COUNT(v) FROM Voucher v WHERE v.restaurant.id = :restaurantId " +
           "AND v.status = 'ACTIVE' " +
           "AND (v.expiresAt IS NULL OR v.expiresAt > :now) " +
           "AND (v.usageLimit IS NULL OR v.usedCount < v.usageLimit)")
    Long countActiveVouchersForRestaurant(@Param("restaurantId") UUID restaurantId, 
                                          @Param("now") LocalDateTime now);

    /**
     * Find vouchers by status
     */
    List<Voucher> findByStatusOrderByCreatedAtDesc(VoucherStatus status);

    /**
     * Find public vouchers
     */
    List<Voucher> findByIsPublicTrueOrderByCreatedAtDesc();

    /**
     * Find vouchers expiring soon (within specified days)
     */
    @Query("SELECT v FROM Voucher v WHERE v.expiresAt IS NOT NULL " +
           "AND v.expiresAt BETWEEN :now AND :expiryThreshold " +
           "AND v.status = 'ACTIVE'")
    List<Voucher> findVouchersExpiringSoon(@Param("now") LocalDateTime now, 
                                          @Param("expiryThreshold") LocalDateTime expiryThreshold);

    /**
     * Find vouchers with low usage (less than specified percentage)
     */
    @Query("SELECT v FROM Voucher v WHERE v.usageLimit IS NOT NULL " +
           "AND v.usedCount < (v.usageLimit * :percentage / 100) " +
           "AND v.status = 'ACTIVE'")
    List<Voucher> findVouchersWithLowUsage(@Param("percentage") Double percentage);

    /**
     * Check if voucher code exists for restaurant
     */
    boolean existsByCodeAndRestaurantId(String code, UUID restaurantId);

    /**
     * Check if voucher code exists globally
     */
    boolean existsByCode(String code);
}
