package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * MenuItemVariation entity representing variations of menu items.
 * For example, different sizes (Small, Medium, Large) or customizations.
 */
@Entity
@Table(name = "menu_item_variations", indexes = {
    @Index(name = "idx_variations_item_id", columnList = "menu_item_id")
})
public class MenuItemVariation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @NotBlank(message = "Variation name is required")
    @Size(max = 255, message = "Variation name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price adjustment is required")
    @Column(name = "price_adjustment", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAdjustment = BigDecimal.ZERO;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @Min(value = 0, message = "Sort order must be non-negative")
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Constructors
    public MenuItemVariation() {
        super();
    }

    public MenuItemVariation(MenuItem menuItem, String name, BigDecimal priceAdjustment) {
        this();
        this.menuItem = menuItem;
        this.name = name;
        this.priceAdjustment = priceAdjustment;
    }

    // Business methods
    public BigDecimal getFinalPrice() {
        return menuItem.getPrice().add(priceAdjustment);
    }

    public String getDisplayName() {
        return name;
    }

    public boolean isFree() {
        return priceAdjustment.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isUpcharge() {
        return priceAdjustment.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isDiscount() {
        return priceAdjustment.compareTo(BigDecimal.ZERO) < 0;
    }

    // Getters and setters
    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
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

    public BigDecimal getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(BigDecimal priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
