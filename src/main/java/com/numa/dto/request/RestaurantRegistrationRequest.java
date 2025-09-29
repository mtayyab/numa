package com.numa.dto.request;

import jakarta.validation.constraints.*;

/**
 * DTO for restaurant registration request.
 * Contains all necessary information for restaurant onboarding.
 */
public class RestaurantRegistrationRequest {

    // Restaurant information
    @NotBlank(message = "Restaurant name is required")
    @Size(max = 255, message = "Restaurant name must not exceed 255 characters")
    private String restaurantName;

    @NotBlank(message = "Restaurant email is required")
    @Email(message = "Restaurant email must be valid")
    private String restaurantEmail;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    // Address information
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    // Localization
    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currencyCode = "USD";

    @NotBlank(message = "Timezone is required")
    private String timezone;

    // Owner information
    @NotBlank(message = "Owner first name is required")
    @Size(max = 100, message = "Owner first name must not exceed 100 characters")
    private String ownerFirstName;

    @NotBlank(message = "Owner last name is required")
    @Size(max = 100, message = "Owner last name must not exceed 100 characters")
    private String ownerLastName;

    @NotBlank(message = "Owner email is required")
    @Email(message = "Owner email must be valid")
    private String ownerEmail;

    @Size(max = 20, message = "Owner phone must not exceed 20 characters")
    private String ownerPhone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;

    // Terms and conditions
    @AssertTrue(message = "You must accept the terms and conditions")
    private Boolean acceptTerms;

    // Constructors
    public RestaurantRegistrationRequest() {}

    // Getters and setters
    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getAcceptTerms() {
        return acceptTerms;
    }

    public void setAcceptTerms(Boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }

    // Validation method
    @AssertTrue(message = "Passwords do not match")
    private boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
