package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * MenuItem entity representing individual items in a restaurant's menu.
 * Contains detailed information about food items including pricing, dietary info, and availability.
 */
@Entity
@Table(name = "menu_items", indexes = {
    @Index(name = "idx_items_restaurant_id", columnList = "restaurant_id"),
    @Index(name = "idx_items_category_id", columnList = "category_id"),
    @Index(name = "idx_items_sort_order", columnList = "category_id, sort_order"),
    @Index(name = "idx_items_availability", columnList = "is_active, is_available")
})
public class MenuItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MenuCategory category;

    @NotBlank(message = "Item name is required")
    @Size(max = 255, message = "Item name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    @Column(name = "image_url")
    private String imageUrl;

    @Min(value = 1, message = "Preparation time must be at least 1 minute")
    @Max(value = 120, message = "Preparation time must not exceed 120 minutes")
    @Column(name = "preparation_time_minutes")
    private Integer preparationTimeMinutes = 15;

    @Min(value = 0, message = "Calories must be non-negative")
    @Column(name = "calories")
    private Integer calories;

    @Column(name = "is_vegetarian", nullable = false)
    private Boolean isVegetarian = false;

    @Column(name = "is_vegan", nullable = false)
    private Boolean isVegan = false;

    @Column(name = "is_gluten_free", nullable = false)
    private Boolean isGlutenFree = false;

    @Column(name = "is_spicy", nullable = false)
    private Boolean isSpicy = false;

    @Min(value = 0, message = "Spice level must be non-negative")
    @Max(value = 5, message = "Spice level must not exceed 5")
    @Column(name = "spice_level")
    private Integer spiceLevel = 0;

    @Size(max = 500, message = "Allergens must not exceed 500 characters")
    @Column(name = "allergens")
    private String allergens;

    @Size(max = 1000, message = "Ingredients must not exceed 1000 characters")
    @Column(name = "ingredients", columnDefinition = "TEXT")
    private String ingredients;

    @Size(max = 500, message = "Tags must not exceed 500 characters")
    @Column(name = "tags")
    private String tags;

    @Min(value = 0, message = "Sort order must be non-negative")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "available_from")
    private LocalTime availableFrom;

    @Column(name = "available_until")
    private LocalTime availableUntil;

    @Min(value = 0, message = "Stock quantity must be non-negative")
    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Min(value = 0, message = "Low stock threshold must be non-negative")
    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold = 5;

    // Relationships
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC, name ASC")
    private List<MenuItemVariation> variations = new ArrayList<>();

    // Constructors
    public MenuItem() {
        super();
    }

    public MenuItem(Restaurant restaurant, MenuCategory category, String name, BigDecimal price) {
        this();
        this.restaurant = restaurant;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    // Business methods
    public boolean isAvailableNow() {
        if (!isActive || !isAvailable) {
            return false;
        }

        // Check stock availability
        if (stockQuantity != null && stockQuantity <= 0) {
            return false;
        }

        // Check time-based availability
        if (availableFrom == null && availableUntil == null) {
            return true; // Available all day
        }

        LocalTime now = LocalTime.now();
        
        if (availableFrom != null && availableUntil != null) {
            if (availableFrom.isBefore(availableUntil)) {
                // Same day availability
                return !now.isBefore(availableFrom) && !now.isAfter(availableUntil);
            } else {
                // Crosses midnight
                return !now.isBefore(availableFrom) || !now.isAfter(availableUntil);
            }
        } else if (availableFrom != null) {
            return !now.isBefore(availableFrom);
        } else if (availableUntil != null) {
            return !now.isAfter(availableUntil);
        }

        return true;
    }

    public boolean isLowStock() {
        return stockQuantity != null && stockQuantity <= lowStockThreshold;
    }

    public boolean hasVariations() {
        return !variations.isEmpty();
    }

    public List<MenuItemVariation> getActiveVariations() {
        return variations.stream()
                .filter(MenuItemVariation::isActive)
                .toList();
    }

    public MenuItemVariation getDefaultVariation() {
        return variations.stream()
                .filter(MenuItemVariation::isDefault)
                .findFirst()
                .orElse(null);
    }

    public BigDecimal getBasePrice() {
        return price;
    }

    public BigDecimal getPriceWithVariation(MenuItemVariation variation) {
        if (variation == null) {
            return price;
        }
        return price.add(variation.getPriceAdjustment());
    }

    public String getDisplayName() {
        return name;
    }

    public String getDietaryInfo() {
        List<String> info = new ArrayList<>();
        if (isVegetarian) info.add("Vegetarian");
        if (isVegan) info.add("Vegan");
        if (isGlutenFree) info.add("Gluten-Free");
        if (isSpicy) info.add("Spicy");
        return String.join(", ", info);
    }

    // Getters and setters
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public void setCategory(MenuCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPreparationTimeMinutes() {
        return preparationTimeMinutes;
    }

    public void setPreparationTimeMinutes(Integer preparationTimeMinutes) {
        this.preparationTimeMinutes = preparationTimeMinutes;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Boolean getIsVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(Boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    public Boolean getIsVegan() {
        return isVegan;
    }

    public void setIsVegan(Boolean isVegan) {
        this.isVegan = isVegan;
    }

    public Boolean getIsGlutenFree() {
        return isGlutenFree;
    }

    public void setIsGlutenFree(Boolean isGlutenFree) {
        this.isGlutenFree = isGlutenFree;
    }

    public Boolean getIsSpicy() {
        return isSpicy;
    }

    public void setIsSpicy(Boolean isSpicy) {
        this.isSpicy = isSpicy;
    }

    public Integer getSpiceLevel() {
        return spiceLevel;
    }

    public void setSpiceLevel(Integer spiceLevel) {
        this.spiceLevel = spiceLevel;
    }

    public String getAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public LocalTime getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(LocalTime availableFrom) {
        this.availableFrom = availableFrom;
    }

    public LocalTime getAvailableUntil() {
        return availableUntil;
    }

    public void setAvailableUntil(LocalTime availableUntil) {
        this.availableUntil = availableUntil;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(Integer lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public List<MenuItemVariation> getVariations() {
        return variations;
    }

    public void setVariations(List<MenuItemVariation> variations) {
        this.variations = variations;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(Boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public boolean isVegan() {
        return isVegan;
    }

    public void setVegan(Boolean vegan) {
        isVegan = vegan;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public void setGlutenFree(Boolean glutenFree) {
        isGlutenFree = glutenFree;
    }

    public boolean isSpicy() {
        return isSpicy;
    }

    public void setSpicy(Boolean spicy) {
        isSpicy = spicy;
    }
}
