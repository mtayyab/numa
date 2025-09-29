package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import com.numa.domain.enums.RestaurantStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Restaurant entity representing a restaurant in the multi-tenant system.
 * Each restaurant has its own menu, tables, orders, and configuration.
 */
@Entity
@Table(name = "restaurants", indexes = {
    @Index(name = "idx_restaurants_slug", columnList = "slug"),
    @Index(name = "idx_restaurants_status", columnList = "status")
})
public class Restaurant extends BaseEntity {

    @NotBlank(message = "Restaurant name is required")
    @Size(max = 255, message = "Restaurant name must not exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Restaurant slug is required")
    @Size(max = 100, message = "Restaurant slug must not exceed 100 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lowercase letters, numbers, and hyphens")
    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(name = "phone")
    private String phone;

    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    @Column(name = "address_line1")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    @Column(name = "address_line2")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    @Column(name = "city")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    @Column(name = "state")
    private String state;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    @Column(name = "postal_code")
    private String postalCode;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    @Column(name = "country")
    private String country;

    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    @Column(name = "currency_code", nullable = false)
    private String currencyCode = "USD";

    @NotBlank(message = "Language code is required")
    @Size(max = 5, message = "Language code must not exceed 5 characters")
    @Column(name = "language_code", nullable = false)
    private String languageCode = "en";

    @NotBlank(message = "Timezone is required")
    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    @Column(name = "timezone", nullable = false)
    private String timezone = "UTC";

    @Size(max = 500, message = "Logo URL must not exceed 500 characters")
    @Column(name = "logo_url")
    private String logoUrl;

    @Size(max = 500, message = "Banner URL must not exceed 500 characters")
    @Column(name = "banner_url")
    private String bannerUrl;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Brand color must be a valid hex color")
    @Column(name = "brand_color")
    private String brandColor = "#FF6B35";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RestaurantStatus status = RestaurantStatus.ACTIVE;

    @Size(max = 20, message = "Subscription plan must not exceed 20 characters")
    @Column(name = "subscription_plan", nullable = false)
    private String subscriptionPlan = "BASIC";

    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;

    // Store restaurant-specific settings as JSON
    @Column(name = "settings", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> settings;

    @Column(name = "delivery_enabled", nullable = false)
    private Boolean deliveryEnabled = false;

    @Column(name = "takeaway_enabled", nullable = false)
    private Boolean takeawayEnabled = true;

    @Column(name = "dine_in_enabled", nullable = false)
    private Boolean dineInEnabled = true;

    @DecimalMin(value = "0.0", message = "Delivery radius must be positive")
    @Column(name = "delivery_radius_km", precision = 5, scale = 2)
    private BigDecimal deliveryRadiusKm;

    @DecimalMin(value = "0.0", message = "Delivery fee must be positive")
    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @DecimalMin(value = "0.0", message = "Minimum order amount must be positive")
    @Column(name = "minimum_order_amount", precision = 10, scale = 2)
    private BigDecimal minimumOrderAmount;

    @DecimalMin(value = "0.0", message = "Tax rate must be positive")
    @DecimalMax(value = "1.0", message = "Tax rate must not exceed 100%")
    @Column(name = "tax_rate", precision = 5, scale = 4)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Service charge rate must be positive")
    @DecimalMax(value = "1.0", message = "Service charge rate must not exceed 100%")
    @Column(name = "service_charge_rate", precision = 5, scale = 4)
    private BigDecimal serviceChargeRate = BigDecimal.ZERO;

    // Relationships
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RestaurantTable> tables = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MenuCategory> menuCategories = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    // Constructors
    public Restaurant() {
        super();
    }

    public Restaurant(String name, String slug, String email) {
        this();
        this.name = name;
        this.slug = slug;
        this.email = email;
    }

    // Business methods
    public boolean isActive() {
        return status == RestaurantStatus.ACTIVE;
    }

    public boolean canAcceptOrders() {
        return isActive() && (dineInEnabled || takeawayEnabled || deliveryEnabled);
    }

    public boolean supportsDelivery() {
        return deliveryEnabled && deliveryRadiusKm != null && deliveryRadiusKm.compareTo(BigDecimal.ZERO) > 0;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getBrandColor() {
        return brandColor;
    }

    public void setBrandColor(String brandColor) {
        this.brandColor = brandColor;
    }

    public RestaurantStatus getStatus() {
        return status;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }

    public String getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(String subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public LocalDateTime getSubscriptionExpiresAt() {
        return subscriptionExpiresAt;
    }

    public void setSubscriptionExpiresAt(LocalDateTime subscriptionExpiresAt) {
        this.subscriptionExpiresAt = subscriptionExpiresAt;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Boolean getDeliveryEnabled() {
        return deliveryEnabled;
    }

    public void setDeliveryEnabled(Boolean deliveryEnabled) {
        this.deliveryEnabled = deliveryEnabled;
    }

    public Boolean getTakeawayEnabled() {
        return takeawayEnabled;
    }

    public void setTakeawayEnabled(Boolean takeawayEnabled) {
        this.takeawayEnabled = takeawayEnabled;
    }

    public Boolean getDineInEnabled() {
        return dineInEnabled;
    }

    public void setDineInEnabled(Boolean dineInEnabled) {
        this.dineInEnabled = dineInEnabled;
    }

    public BigDecimal getDeliveryRadiusKm() {
        return deliveryRadiusKm;
    }

    public void setDeliveryRadiusKm(BigDecimal deliveryRadiusKm) {
        this.deliveryRadiusKm = deliveryRadiusKm;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getServiceChargeRate() {
        return serviceChargeRate;
    }

    public void setServiceChargeRate(BigDecimal serviceChargeRate) {
        this.serviceChargeRate = serviceChargeRate;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<RestaurantTable> getTables() {
        return tables;
    }

    public void setTables(List<RestaurantTable> tables) {
        this.tables = tables;
    }

    public List<MenuCategory> getMenuCategories() {
        return menuCategories;
    }

    public void setMenuCategories(List<MenuCategory> menuCategories) {
        this.menuCategories = menuCategories;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
