package com.numa.dto.response;

import java.util.UUID;

/**
 * Response DTO for restaurant settings
 */
public class RestaurantSettingsResponse {
    
    private UUID id;
    private String name;
    private String description;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String country;
    private String logoUrl;
    private String currencyCode;
    private String languageCode;
    private String timezone;
    private Integer decimalPlaces;
    
    public RestaurantSettingsResponse() {}
    
    public RestaurantSettingsResponse(UUID id, String name, String description, String phone, 
                                    String email, String address, String city, String country, 
                                    String logoUrl, String currencyCode, String languageCode, 
                                    String timezone, Integer decimalPlaces) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.country = country;
        this.logoUrl = logoUrl;
        this.currencyCode = currencyCode;
        this.languageCode = languageCode;
        this.timezone = timezone;
        this.decimalPlaces = decimalPlaces;
    }
    
    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    
    public String getLanguageCode() { return languageCode; }
    public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public Integer getDecimalPlaces() { return decimalPlaces; }
    public void setDecimalPlaces(Integer decimalPlaces) { this.decimalPlaces = decimalPlaces; }
}
