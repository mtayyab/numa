package com.numa.controller;

import com.numa.dto.request.RestaurantRegistrationRequest;
import com.numa.dto.request.RestaurantUpdateRequest;
import com.numa.dto.response.RestaurantResponse;
import com.numa.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for restaurant management operations.
 * Handles restaurant CRUD operations and related functionality.
 */
@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    /**
     * Register a new restaurant (public endpoint)
     */
    @PostMapping("/register")
    public ResponseEntity<RestaurantResponse> registerRestaurant(
            @Valid @RequestBody RestaurantRegistrationRequest request) {
        RestaurantResponse response = restaurantService.registerRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get restaurant by ID
     */
    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable UUID restaurantId) {
        RestaurantResponse response = restaurantService.getRestaurant(restaurantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get restaurant by slug (public for menu access)
     */
    @GetMapping("/by-slug/{slug}")
    public ResponseEntity<RestaurantResponse> getRestaurantBySlug(@PathVariable String slug) {
        RestaurantResponse response = restaurantService.getRestaurantBySlug(slug);
        return ResponseEntity.ok(response);
    }

    /**
     * Update restaurant information
     */
    @PutMapping("/{restaurantId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody RestaurantUpdateRequest request) {
        RestaurantResponse response = restaurantService.updateRestaurant(restaurantId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Activate restaurant
     */
    @PostMapping("/{restaurantId}/activate")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<RestaurantResponse> activateRestaurant(@PathVariable UUID restaurantId) {
        RestaurantResponse response = restaurantService.activateRestaurant(restaurantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Deactivate restaurant
     */
    @PostMapping("/{restaurantId}/deactivate")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<RestaurantResponse> deactivateRestaurant(@PathVariable UUID restaurantId) {
        RestaurantResponse response = restaurantService.deactivateRestaurant(restaurantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all restaurants (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RestaurantResponse>> getAllRestaurants(Pageable pageable) {
        Page<RestaurantResponse> response = restaurantService.getAllRestaurants(pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Search restaurants
     */
    @GetMapping("/search")
    public ResponseEntity<Page<RestaurantResponse>> searchRestaurants(
            @RequestParam String q,
            Pageable pageable) {
        Page<RestaurantResponse> response = restaurantService.searchRestaurants(q, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get active restaurants
     */
    @GetMapping("/active")
    public ResponseEntity<List<RestaurantResponse>> getActiveRestaurants() {
        List<RestaurantResponse> response = restaurantService.getActiveRestaurants();
        return ResponseEntity.ok(response);
    }

    /**
     * Get restaurant statistics
     */
    @GetMapping("/{restaurantId}/stats")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<RestaurantService.RestaurantStatsResponse> getRestaurantStats(
            @PathVariable UUID restaurantId) {
        RestaurantService.RestaurantStatsResponse response = restaurantService.getRestaurantStats(restaurantId);
        return ResponseEntity.ok(response);
    }

    /**
     * Check if restaurant can accept orders
     */
    @GetMapping("/{restaurantId}/can-accept-orders")
    public ResponseEntity<Boolean> canAcceptOrders(@PathVariable UUID restaurantId) {
        boolean canAccept = restaurantService.canAcceptOrders(restaurantId);
        return ResponseEntity.ok(canAccept);
    }
}
