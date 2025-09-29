package com.numa.repository;

import com.numa.domain.entity.SessionGuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for SessionGuest entity operations.
 * Provides data access methods for guest session management.
 */
@Repository
public interface SessionGuestRepository extends JpaRepository<SessionGuest, UUID> {

    /**
     * Find guests by session ID
     */
    List<SessionGuest> findBySessionIdOrderByJoinedAtAsc(UUID sessionId);
    
    
    /**
     * Find guest by session ID and join token
     */
    Optional<SessionGuest> findBySessionIdAndJoinToken(UUID sessionId, String joinToken);
    
    /**
     * Find guest by join token
     */
    Optional<SessionGuest> findByJoinToken(String joinToken);
    
    
    /**
     * Count active guests by session - simplified version
     */
    long countBySessionId(UUID sessionId);
    
    /**
     * Find guests who joined within time range
     */
    @Query("SELECT sg FROM SessionGuest sg WHERE sg.session.id = :sessionId " +
           "AND sg.joinedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY sg.joinedAt ASC")
    List<SessionGuest> findGuestsByJoinTimeRange(@Param("sessionId") UUID sessionId,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find guests who left within time range - simplified version
     */
    @Query("SELECT sg FROM SessionGuest sg WHERE sg.session.id = :sessionId " +
           "AND sg.joinedAt BETWEEN :startTime AND :endTime " +
           "ORDER BY sg.joinedAt ASC")
    List<SessionGuest> findGuestsByLeaveTimeRange(@Param("sessionId") UUID sessionId,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);
    
    /**
     * Find guests by restaurant and date range
     */
    @Query("SELECT sg FROM SessionGuest sg WHERE sg.session.restaurant.id = :restaurantId " +
           "AND sg.joinedAt BETWEEN :startDate AND :endDate " +
           "ORDER BY sg.joinedAt DESC")
    List<SessionGuest> findGuestsByRestaurantAndDateRange(@Param("restaurantId") UUID restaurantId,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count total guests by restaurant and date range
     */
    @Query("SELECT COUNT(sg) FROM SessionGuest sg WHERE sg.session.restaurant.id = :restaurantId " +
           "AND sg.joinedAt BETWEEN :startDate AND :endDate")
    long countGuestsByRestaurantAndDateRange(@Param("restaurantId") UUID restaurantId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find guests by name pattern
     */
    @Query("SELECT sg FROM SessionGuest sg WHERE sg.session.id = :sessionId " +
           "AND LOWER(sg.guestName) LIKE LOWER(CONCAT('%', :namePattern, '%')) " +
           "ORDER BY sg.joinedAt ASC")
    List<SessionGuest> findGuestsByNamePattern(@Param("sessionId") UUID sessionId,
                                             @Param("namePattern") String namePattern);
    
    /**
     * Check if join token exists
     */
    boolean existsByJoinToken(String joinToken);
    
    /**
     * Find guests who are still active (haven't left) - simplified version
     */
    @Query("SELECT sg FROM SessionGuest sg WHERE sg.session.id = :sessionId " +
           "ORDER BY sg.joinedAt ASC")
    List<SessionGuest> findActiveGuestsInSession(@Param("sessionId") UUID sessionId);
    
    /**
     * Find guests who have left the session - simplified version
     */
    @Query("SELECT sg FROM SessionGuest sg WHERE sg.session.id = :sessionId " +
           "ORDER BY sg.joinedAt DESC")
    List<SessionGuest> findGuestsWhoLeftSession(@Param("sessionId") UUID sessionId);
}
