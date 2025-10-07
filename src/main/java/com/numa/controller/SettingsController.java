package com.numa.controller;

import com.numa.dto.request.RestaurantSettingsRequest;
import com.numa.dto.response.RestaurantSettingsResponse;
import com.numa.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for restaurant settings management
 */
@RestController
@RequestMapping("/settings")
@Tag(name = "Settings Management", description = "APIs for managing restaurant settings")
public class SettingsController {
    
    @Autowired
    private SettingsService settingsService;
    
    /**
     * Get restaurant settings
     */
    @Operation(summary = "Get Restaurant Settings", description = "Get current restaurant settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Settings retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/restaurant")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<RestaurantSettingsResponse> getRestaurantSettings() {
        RestaurantSettingsResponse settings = settingsService.getRestaurantSettings();
        return ResponseEntity.ok(settings);
    }
    
    /**
     * Update restaurant settings
     */
    @Operation(summary = "Update Restaurant Settings", description = "Update restaurant settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Settings updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/restaurant")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<RestaurantSettingsResponse> updateRestaurantSettings(
            @Valid @RequestBody RestaurantSettingsRequest request) {
        RestaurantSettingsResponse settings = settingsService.updateRestaurantSettings(request);
        return ResponseEntity.ok(settings);
    }
}
