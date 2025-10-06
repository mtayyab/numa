package com.numa.dto.response;

import java.util.UUID;

/**
 * Basic restaurant response DTO containing only essential fields.
 * Used to avoid lazy loading issues when only basic restaurant info is needed.
 */
public class BasicRestaurantResponse {
    
    private UUID id;
    private String name;
    private String slug;
    
    // Constructors
    public BasicRestaurantResponse() {}
    
    public BasicRestaurantResponse(UUID id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
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
}
