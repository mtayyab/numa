package com.numa.service;

import com.numa.domain.entity.Restaurant;
import com.numa.domain.entity.User;
import com.numa.domain.enums.RestaurantStatus;
import com.numa.domain.enums.UserRole;
import com.numa.dto.request.RestaurantRegistrationRequest;
import com.numa.dto.request.RestaurantUpdateRequest;
import com.numa.dto.response.RestaurantResponse;
import com.numa.exception.ResourceNotFoundException;
import com.numa.exception.ValidationException;
import com.numa.mapper.RestaurantMapper;
import com.numa.repository.RestaurantRepository;
import com.numa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for restaurant management operations.
 * Handles restaurant onboarding, configuration, and management.
 */
@Service
@Transactional
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    /**
     * Register a new restaurant with owner account
     */
    public RestaurantResponse registerRestaurant(RestaurantRegistrationRequest request) {
        // Validate request
        validateRegistrationRequest(request);

        // Create restaurant entity
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getRestaurantName());
        restaurant.setSlug(generateSlug(request.getRestaurantName()));
        restaurant.setEmail(request.getRestaurantEmail());
        restaurant.setPhone(request.getPhone());
        restaurant.setAddressLine1(request.getAddressLine1());
        restaurant.setAddressLine2(request.getAddressLine2());
        restaurant.setCity(request.getCity());
        restaurant.setState(request.getState());
        restaurant.setPostalCode(request.getPostalCode());
        restaurant.setCountry(request.getCountry());
        restaurant.setCurrencyCode(request.getCurrencyCode());
        restaurant.setTimezone(request.getTimezone());
        restaurant.setStatus(RestaurantStatus.PENDING_APPROVAL);

        // Save restaurant
        restaurant = restaurantRepository.save(restaurant);

        // Create owner user account
        User owner = new User();
        owner.setRestaurant(restaurant);
        owner.setEmail(request.getOwnerEmail());
        owner.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        owner.setFirstName(request.getOwnerFirstName());
        owner.setLastName(request.getOwnerLastName());
        owner.setPhone(request.getOwnerPhone());
        owner.setRole(UserRole.OWNER);
        owner.setStatus("ACTIVE");

        userRepository.save(owner);

        // Send welcome email
        emailService.sendRestaurantWelcomeEmail(restaurant, owner);

        return restaurantMapper.toResponse(restaurant);
    }

    /**
     * Get restaurant by ID
     */
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurant(UUID restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        return restaurantMapper.toResponse(restaurant);
    }

    /**
     * Get restaurant by slug
     */
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantBySlug(String slug) {
        Restaurant restaurant = restaurantRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with slug: " + slug));
        return restaurantMapper.toResponse(restaurant);
    }

    /**
     * Update restaurant information
     */
    public RestaurantResponse updateRestaurant(UUID restaurantId, RestaurantUpdateRequest request) {
        Restaurant restaurant = findRestaurantById(restaurantId);

        // Update basic information
        if (request.getName() != null) {
            restaurant.setName(request.getName());
        }
        if (request.getDescription() != null) {
            restaurant.setDescription(request.getDescription());
        }
        if (request.getPhone() != null) {
            restaurant.setPhone(request.getPhone());
        }

        // Update address
        if (request.getAddressLine1() != null) {
            restaurant.setAddressLine1(request.getAddressLine1());
        }
        if (request.getAddressLine2() != null) {
            restaurant.setAddressLine2(request.getAddressLine2());
        }
        if (request.getCity() != null) {
            restaurant.setCity(request.getCity());
        }
        if (request.getState() != null) {
            restaurant.setState(request.getState());
        }
        if (request.getPostalCode() != null) {
            restaurant.setPostalCode(request.getPostalCode());
        }

        // Update branding
        if (request.getLogoUrl() != null) {
            restaurant.setLogoUrl(request.getLogoUrl());
        }
        if (request.getBannerUrl() != null) {
            restaurant.setBannerUrl(request.getBannerUrl());
        }
        if (request.getBrandColor() != null) {
            restaurant.setBrandColor(request.getBrandColor());
        }

        // Update operational settings
        if (request.getDeliveryEnabled() != null) {
            restaurant.setDeliveryEnabled(request.getDeliveryEnabled());
        }
        if (request.getTakeawayEnabled() != null) {
            restaurant.setTakeawayEnabled(request.getTakeawayEnabled());
        }
        if (request.getDineInEnabled() != null) {
            restaurant.setDineInEnabled(request.getDineInEnabled());
        }

        // Update financial settings
        if (request.getDeliveryFee() != null) {
            restaurant.setDeliveryFee(request.getDeliveryFee());
        }
        if (request.getMinimumOrderAmount() != null) {
            restaurant.setMinimumOrderAmount(request.getMinimumOrderAmount());
        }
        if (request.getTaxRate() != null) {
            restaurant.setTaxRate(request.getTaxRate());
        }
        if (request.getServiceChargeRate() != null) {
            restaurant.setServiceChargeRate(request.getServiceChargeRate());
        }

        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(restaurant);
    }

    /**
     * Activate restaurant
     */
    public RestaurantResponse activateRestaurant(UUID restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurant.setStatus(RestaurantStatus.ACTIVE);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(restaurant);
    }

    /**
     * Deactivate restaurant
     */
    public RestaurantResponse deactivateRestaurant(UUID restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurant.setStatus(RestaurantStatus.INACTIVE);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(restaurant);
    }

    /**
     * Get all restaurants with pagination
     */
    @Transactional(readOnly = true)
    public Page<RestaurantResponse> getAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable)
                .map(restaurantMapper::toResponse);
    }

    /**
     * Search restaurants
     */
    @Transactional(readOnly = true)
    public Page<RestaurantResponse> searchRestaurants(String searchTerm, Pageable pageable) {
        return restaurantRepository.searchRestaurants(searchTerm, pageable)
                .map(restaurantMapper::toResponse);
    }

    /**
     * Get restaurants by status
     */
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getRestaurantsByStatus(RestaurantStatus status) {
        return restaurantRepository.findByStatus(status)
                .stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    /**
     * Get active restaurants
     */
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getActiveRestaurants() {
        return restaurantRepository.findActiveRestaurants()
                .stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    /**
     * Check if restaurant can accept orders
     */
    @Transactional(readOnly = true)
    public boolean canAcceptOrders(UUID restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        return restaurant.canAcceptOrders();
    }

    /**
     * Get restaurant statistics
     */
    @Transactional(readOnly = true)
    public RestaurantStatsResponse getRestaurantStats(UUID restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        
        // Calculate various statistics
        long totalTables = restaurant.getTables().size();
        long totalMenuItems = restaurant.getMenuCategories().stream()
                .mapToLong(category -> category.getMenuItems().size())
                .sum();
        long totalOrders = restaurant.getOrders().size();
        
        return new RestaurantStatsResponse(totalTables, totalMenuItems, totalOrders);
    }

    // Helper methods
    private Restaurant findRestaurantById(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
    }

    private void validateRegistrationRequest(RestaurantRegistrationRequest request) {
        // Check if email already exists
        if (restaurantRepository.existsByEmailIgnoreCase(request.getRestaurantEmail())) {
            throw new ValidationException("Restaurant email already exists");
        }

        // Check if owner email already exists
        if (userRepository.existsByEmailIgnoreCase(request.getOwnerEmail())) {
            throw new ValidationException("Owner email already exists");
        }

        // Validate slug uniqueness
        String slug = generateSlug(request.getRestaurantName());
        if (restaurantRepository.existsBySlugIgnoreCase(slug)) {
            throw new ValidationException("Restaurant name already exists");
        }
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    // Inner class for statistics response
    public static class RestaurantStatsResponse {
        private final long totalTables;
        private final long totalMenuItems;
        private final long totalOrders;

        public RestaurantStatsResponse(long totalTables, long totalMenuItems, long totalOrders) {
            this.totalTables = totalTables;
            this.totalMenuItems = totalMenuItems;
            this.totalOrders = totalOrders;
        }

        // Getters
        public long getTotalTables() { return totalTables; }
        public long getTotalMenuItems() { return totalMenuItems; }
        public long getTotalOrders() { return totalOrders; }
    }
}
