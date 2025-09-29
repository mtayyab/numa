package com.numa.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO for restaurant update request.
 * Contains fields that can be updated after restaurant creation.
 */
public class RestaurantUpdateRequest {

    @Size(max = 255, message = "Restaurant name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    // Address information
    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    // Branding
    @Size(max = 500, message = "Logo URL must not exceed 500 characters")
    private String logoUrl;

    @Size(max = 500, message = "Banner URL must not exceed 500 characters")
    private String bannerUrl;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Brand color must be a valid hex color")
    private String brandColor;

    // Operational settings
    private Boolean deliveryEnabled;
    private Boolean takeawayEnabled;
    private Boolean dineInEnabled;

    @DecimalMin(value = "0.0", message = "Delivery radius must be positive")
    private BigDecimal deliveryRadiusKm;

    @DecimalMin(value = "0.0", message = "Delivery fee must be positive")
    private BigDecimal deliveryFee;

    @DecimalMin(value = "0.0", message = "Minimum order amount must be positive")
    private BigDecimal minimumOrderAmount;

    @DecimalMin(value = "0.0", message = "Tax rate must be positive")
    @DecimalMax(value = "1.0", message = "Tax rate must not exceed 100%")
    private BigDecimal taxRate;

    @DecimalMin(value = "0.0", message = "Service charge rate must be positive")
    @DecimalMax(value = "1.0", message = "Service charge rate must not exceed 100%")
    private BigDecimal serviceChargeRate;

    // Constructors
    public RestaurantUpdateRequest() {}

    // Getters and setters
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
}
