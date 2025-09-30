package com.numa.controller;

import com.numa.domain.entity.MenuCategory;
import com.numa.domain.entity.MenuItem;
import com.numa.dto.request.MenuCategoryRequest;
import com.numa.dto.request.MenuItemRequest;
import com.numa.dto.response.MenuCategoryResponse;
import com.numa.dto.response.MenuItemResponse;
import com.numa.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for menu management operations.
 * Handles menu categories and items CRUD operations.
 */
@RestController
@RequestMapping("/restaurants/{restaurantId}/menu")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Menu Management", description = "Endpoints for menu management")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * Get all menu categories for a restaurant
     */
    @Operation(summary = "Get Menu Categories", description = "Get all menu categories for a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/categories")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<MenuCategoryResponse>> getCategories(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId) {
        List<MenuCategoryResponse> categories = menuService.getCategories(restaurantId);
        return ResponseEntity.ok(categories);
    }

    /**
     * Create a new menu category
     */
    @Operation(summary = "Create Menu Category", description = "Create a new menu category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PostMapping("/categories")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<MenuCategoryResponse> createCategory(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Valid @RequestBody MenuCategoryRequest request) {
        MenuCategoryResponse category = menuService.createCategory(restaurantId, request);
        return ResponseEntity.status(201).body(category);
    }

    /**
     * Update a menu category
     */
    @Operation(summary = "Update Menu Category", description = "Update an existing menu category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<MenuCategoryResponse> updateCategory(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Category ID") @PathVariable UUID categoryId,
            @Valid @RequestBody MenuCategoryRequest request) {
        MenuCategoryResponse category = menuService.updateCategory(restaurantId, categoryId, request);
        return ResponseEntity.ok(category);
    }

    /**
     * Delete a menu category
     */
    @Operation(summary = "Delete Menu Category", description = "Delete a menu category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Category ID") @PathVariable UUID categoryId) {
        menuService.deleteCategory(restaurantId, categoryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all menu items for a restaurant
     */
    @Operation(summary = "Get Menu Items", description = "Get all menu items for a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/items")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<MenuItemResponse>> getItems(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId) {
        List<MenuItemResponse> items = menuService.getItems(restaurantId);
        return ResponseEntity.ok(items);
    }

    /**
     * Get menu items for a specific category
     */
    @Operation(summary = "Get Menu Items by Category", description = "Get menu items for a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/categories/{categoryId}/items")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<MenuItemResponse>> getItemsByCategory(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Category ID") @PathVariable UUID categoryId) {
        List<MenuItemResponse> items = menuService.getItemsByCategory(restaurantId, categoryId);
        return ResponseEntity.ok(items);
    }

    /**
     * Create a new menu item
     */
    @Operation(summary = "Create Menu Item", description = "Create a new menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PostMapping("/categories/{categoryId}/items")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<MenuItemResponse> createItem(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Category ID") @PathVariable UUID categoryId,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse item = menuService.createItem(restaurantId, categoryId, request);
        return ResponseEntity.status(201).body(item);
    }

    /**
     * Update a menu item
     */
    @Operation(summary = "Update Menu Item", description = "Update an existing menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PutMapping("/items/{itemId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<MenuItemResponse> updateItem(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Item ID") @PathVariable UUID itemId,
            @Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse item = menuService.updateItem(restaurantId, itemId, request);
        return ResponseEntity.ok(item);
    }

    /**
     * Delete a menu item
     */
    @Operation(summary = "Delete Menu Item", description = "Delete a menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteItem(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Item ID") @PathVariable UUID itemId) {
        menuService.deleteItem(restaurantId, itemId);
        return ResponseEntity.noContent().build();
    }
}
