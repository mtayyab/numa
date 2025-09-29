package com.numa.repository;

import com.numa.domain.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for MenuCategory entity operations.
 * Provides data access methods for menu category management.
 */
@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, UUID> {

    /**
     * Find categories by restaurant ID ordered by sort order
     */
    List<MenuCategory> findByRestaurantIdOrderBySortOrderAsc(UUID restaurantId);

    /**
     * Find active categories by restaurant ID
     */
    @Query("SELECT c FROM MenuCategory c WHERE c.restaurant.id = :restaurantId AND c.isActive = true ORDER BY c.sortOrder ASC")
    List<MenuCategory> findActiveByRestaurantId(@Param("restaurantId") UUID restaurantId);

    /**
     * Find category by restaurant and name
     */
    Optional<MenuCategory> findByRestaurantIdAndName(UUID restaurantId, String name);

    /**
     * Check if category name exists in restaurant
     */
    boolean existsByRestaurantIdAndNameIgnoreCase(UUID restaurantId, String name);

    /**
     * Find categories with available items
     */
    @Query("SELECT DISTINCT c FROM MenuCategory c " +
           "JOIN c.menuItems i WHERE c.restaurant.id = :restaurantId " +
           "AND c.isActive = true AND i.isActive = true AND i.isAvailable = true " +
           "ORDER BY c.sortOrder ASC")
    List<MenuCategory> findCategoriesWithAvailableItems(@Param("restaurantId") UUID restaurantId);

    /**
     * Count active categories by restaurant
     */
    @Query("SELECT COUNT(c) FROM MenuCategory c WHERE c.restaurant.id = :restaurantId AND c.isActive = true")
    long countActiveByRestaurant(@Param("restaurantId") UUID restaurantId);

    /**
     * Find categories by active status
     */
    List<MenuCategory> findByRestaurantIdAndIsActiveOrderBySortOrderAsc(UUID restaurantId, Boolean isActive);

    /**
     * Get next sort order for restaurant
     */
    @Query("SELECT COALESCE(MAX(c.sortOrder), 0) + 1 FROM MenuCategory c WHERE c.restaurant.id = :restaurantId")
    Integer getNextSortOrder(@Param("restaurantId") UUID restaurantId);

    /**
     * Search categories by name
     */
    @Query("SELECT c FROM MenuCategory c WHERE c.restaurant.id = :restaurantId " +
           "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY c.sortOrder ASC")
    List<MenuCategory> searchByName(@Param("restaurantId") UUID restaurantId, @Param("searchTerm") String searchTerm);

    /**
     * Find categories with items count
     */
    @Query("SELECT c, COUNT(i) as itemCount FROM MenuCategory c " +
           "LEFT JOIN c.menuItems i ON i.isActive = true " +
           "WHERE c.restaurant.id = :restaurantId " +
           "GROUP BY c ORDER BY c.sortOrder ASC")
    List<Object[]> findCategoriesWithItemCount(@Param("restaurantId") UUID restaurantId);

    /**
     * Update sort orders when reordering
     */
    @Query("UPDATE MenuCategory c SET c.sortOrder = :newSortOrder WHERE c.id = :categoryId")
    void updateSortOrder(@Param("categoryId") UUID categoryId, @Param("newSortOrder") Integer newSortOrder);
}
