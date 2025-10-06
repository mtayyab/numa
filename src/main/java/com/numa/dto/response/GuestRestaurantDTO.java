package com.numa.dto.response;

import com.numa.domain.enums.RestaurantStatus;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Lightweight DTO for guest restaurant information.
 * Contains only the fields needed for guest interface to avoid lazy loading issues.
 */
public class GuestRestaurantDTO {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String currencyCode;
    private String languageCode;
    private String timezone;
    private String logoUrl;
    private String bannerUrl;
    private String brandColor;
    private RestaurantStatus status;
    private Boolean deliveryEnabled;
    private Boolean takeawayEnabled;
    private Boolean dineInEnabled;
    private BigDecimal deliveryRadiusKm;
    private BigDecimal deliveryFee;
    private BigDecimal minimumOrderAmount;
    private BigDecimal taxRate;
    private BigDecimal serviceChargeRate;

    public GuestRestaurantDTO() {}

    public GuestRestaurantDTO(UUID id, String name, String slug, String description, String phone,
                             String addressLine1, String addressLine2, String city, String state,
                             String postalCode, String country, String currencyCode, String languageCode,
                             String timezone, String logoUrl, String bannerUrl, String brandColor,
                             RestaurantStatus status, Boolean deliveryEnabled, Boolean takeawayEnabled,
                             Boolean dineInEnabled, BigDecimal deliveryRadiusKm, BigDecimal deliveryFee,
                             BigDecimal minimumOrderAmount, BigDecimal taxRate, BigDecimal serviceChargeRate) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.currencyCode = currencyCode;
        this.languageCode = languageCode;
        this.timezone = timezone;
        this.logoUrl = logoUrl;
        this.bannerUrl = bannerUrl;
        this.brandColor = brandColor;
        this.status = status;
        this.deliveryEnabled = deliveryEnabled;
        this.takeawayEnabled = takeawayEnabled;
        this.dineInEnabled = dineInEnabled;
        this.deliveryRadiusKm = deliveryRadiusKm;
        this.deliveryFee = deliveryFee;
        this.minimumOrderAmount = minimumOrderAmount;
        this.taxRate = taxRate;
        this.serviceChargeRate = serviceChargeRate;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
