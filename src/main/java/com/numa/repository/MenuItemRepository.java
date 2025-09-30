package com.numa.repository;

import com.numa.domain.entity.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for MenuItem entity operations.
 * Provides data access methods for menu item management.
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {

    /**
     * Find items by restaurant ID
     */
    List<MenuItem> findByRestaurantIdOrderByCategorySortOrderAscSortOrderAsc(UUID restaurantId);

    /**
     * Find items by category ID
     */
    List<MenuItem> findByCategoryIdOrderBySortOrderAsc(UUID categoryId);

    /**
     * Find items by restaurant ID ordered by sort order
     */
    @Query("SELECT i FROM MenuItem i WHERE i.category.restaurant.id = :restaurantId ORDER BY i.sortOrder ASC")
    List<MenuItem> findByRestaurantIdOrderBySortOrderAsc(@Param("restaurantId") UUID restaurantId);

    /**
     * Get next sort order for category
     */
    @Query("SELECT COALESCE(MAX(i.sortOrder), 0) + 1 FROM MenuItem i WHERE i.category.id = :categoryId")
    Integer getNextSortOrder(@Param("categoryId") UUID categoryId);

    /**
     * Find active and available items by category
     */
    @Query("SELECT i FROM MenuItem i WHERE i.category.id = :categoryId " +
           "AND i.isActive = true AND i.isAvailable = true ORDER BY i.sortOrder ASC")
    List<MenuItem> findAvailableItemsByCategory(@Param("categoryId") UUID categoryId);

    /**
     * Find items by restaurant and active status
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND i.isActive = :isActive ORDER BY i.category.sortOrder ASC, i.sortOrder ASC")
    List<MenuItem> findByRestaurantIdAndIsActive(@Param("restaurantId") UUID restaurantId, @Param("isActive") Boolean isActive);

    /**
     * Search items by name or description
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND i.isActive = true AND i.isAvailable = true " +
           "ORDER BY i.category.sortOrder ASC, i.sortOrder ASC")
    List<MenuItem> searchAvailableItems(@Param("restaurantId") UUID restaurantId, @Param("searchTerm") String searchTerm);

    /**
     * Find items by dietary restrictions
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND i.isActive = true AND i.isAvailable = true " +
           "AND (:isVegetarian = false OR i.isVegetarian = true) " +
           "AND (:isVegan = false OR i.isVegan = true) " +
           "AND (:isGlutenFree = false OR i.isGlutenFree = true) " +
           "ORDER BY i.category.sortOrder ASC, i.sortOrder ASC")
    List<MenuItem> findItemsByDietaryRestrictions(
            @Param("restaurantId") UUID restaurantId,
            @Param("isVegetarian") boolean isVegetarian,
            @Param("isVegan") boolean isVegan,
            @Param("isGlutenFree") boolean isGlutenFree);

    /**
     * Find items by price range
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND i.price BETWEEN :minPrice AND :maxPrice " +
           "AND i.isActive = true AND i.isAvailable = true " +
           "ORDER BY i.price ASC")
    List<MenuItem> findItemsByPriceRange(
            @Param("restaurantId") UUID restaurantId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Find low stock items
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND i.stockQuantity IS NOT NULL " +
           "AND i.stockQuantity <= i.lowStockThreshold " +
           "AND i.isActive = true " +
           "ORDER BY i.stockQuantity ASC")
    List<MenuItem> findLowStockItems(@Param("restaurantId") UUID restaurantId);

    /**
     * Find out of stock items
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND i.stockQuantity IS NOT NULL AND i.stockQuantity = 0 " +
           "AND i.isActive = true " +
           "ORDER BY i.name ASC")
    List<MenuItem> findOutOfStockItems(@Param("restaurantId") UUID restaurantId);

    /**
     * Find popular items (most ordered)
     */
    @Query("SELECT i, COUNT(oi) as orderCount FROM MenuItem i " +
           "JOIN OrderItem oi ON oi.menuItem = i " +
           "JOIN Order o ON oi.order = o " +
           "WHERE i.restaurant.id = :restaurantId " +
           "AND o.createdAt >= :fromDate " +
           "AND o.status NOT IN ('CANCELLED', 'REFUNDED') " +
           "GROUP BY i " +
           "ORDER BY orderCount DESC")
    Page<Object[]> findPopularItems(@Param("restaurantId") UUID restaurantId, 
                                   @Param("fromDate") java.time.LocalDateTime fromDate, 
                                   Pageable pageable);

    /**
     * Find items by tags
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND i.tags IS NOT NULL " +
           "AND LOWER(i.tags) LIKE LOWER(CONCAT('%', :tag, '%')) " +
           "AND i.isActive = true AND i.isAvailable = true " +
           "ORDER BY i.category.sortOrder ASC, i.sortOrder ASC")
    List<MenuItem> findItemsByTag(@Param("restaurantId") UUID restaurantId, @Param("tag") String tag);

    /**
     * Find spicy items by spice level
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND i.isSpicy = true AND i.spiceLevel >= :minSpiceLevel " +
           "AND i.isActive = true AND i.isAvailable = true " +
           "ORDER BY i.spiceLevel DESC, i.name ASC")
    List<MenuItem> findSpicyItems(@Param("restaurantId") UUID restaurantId, @Param("minSpiceLevel") Integer minSpiceLevel);

    /**
     * Count items by category
     */
    @Query("SELECT COUNT(i) FROM MenuItem i WHERE i.category.id = :categoryId AND i.isActive = true")
    long countActiveItemsByCategory(@Param("categoryId") UUID categoryId);

    /**
     * Count available items by category
     */
    @Query("SELECT COUNT(i) FROM MenuItem i WHERE i.category.id = :categoryId " +
           "AND i.isActive = true AND i.isAvailable = true")
    long countAvailableItemsByCategory(@Param("categoryId") UUID categoryId);

    /**
     * Get next sort order for category
     */
    @Query("SELECT COALESCE(MAX(i.sortOrder), 0) + 1 FROM MenuItem i WHERE i.category.id = :categoryId")
    Integer getNextSortOrderForCategory(@Param("categoryId") UUID categoryId);

    /**
     * Check if item name exists in category
     */
    boolean existsByCategoryIdAndNameIgnoreCase(UUID categoryId, String name);

    /**
     * Find items with allergens
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND i.allergens IS NOT NULL AND i.allergens != '' " +
           "AND i.isActive = true AND i.isAvailable = true " +
           "ORDER BY i.name ASC")
    List<MenuItem> findItemsWithAllergens(@Param("restaurantId") UUID restaurantId);

    /**
     * Find items without specific allergen
     */
    @Query("SELECT i FROM MenuItem i WHERE i.restaurant.id = :restaurantId " +
           "AND (i.allergens IS NULL OR LOWER(i.allergens) NOT LIKE LOWER(CONCAT('%', :allergen, '%'))) " +
           "AND i.isActive = true AND i.isAvailable = true " +
           "ORDER BY i.category.sortOrder ASC, i.sortOrder ASC")
    List<MenuItem> findItemsWithoutAllergen(@Param("restaurantId") UUID restaurantId, @Param("allergen") String allergen);
}
