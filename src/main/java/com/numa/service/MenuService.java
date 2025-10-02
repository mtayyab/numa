package com.numa.service;

import com.numa.domain.entity.MenuCategory;
import com.numa.domain.entity.MenuItem;
import com.numa.domain.entity.Restaurant;
import com.numa.dto.request.MenuCategoryRequest;
import com.numa.dto.request.MenuItemRequest;
import com.numa.dto.response.MenuCategoryResponse;
import com.numa.dto.response.MenuItemResponse;
import com.numa.exception.ResourceNotFoundException;
import com.numa.repository.MenuCategoryRepository;
import com.numa.repository.MenuItemRepository;
import com.numa.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for menu management operations.
 * Handles menu categories and items CRUD operations.
 */
@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Get all menu categories for a restaurant
     */
    @Transactional(readOnly = true)
    public List<MenuCategoryResponse> getCategories(UUID restaurantId) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        List<MenuCategory> categories = menuCategoryRepository
                .findByRestaurantIdOrderBySortOrderAsc(restaurantId);

        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create a new menu category
     */
    public MenuCategoryResponse createCategory(UUID restaurantId, MenuCategoryRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        MenuCategory category = new MenuCategory();
        category.setRestaurant(restaurant);
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 
                menuCategoryRepository.getNextSortOrder(restaurantId));
        category.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        category.setAvailableFrom(request.getAvailableFrom());
        category.setAvailableUntil(request.getAvailableUntil());

        MenuCategory savedCategory = menuCategoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    /**
     * Update a menu category
     */
    public MenuCategoryResponse updateCategory(UUID restaurantId, UUID categoryId, MenuCategoryRequest request) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu category not found"));

        // Verify category belongs to restaurant
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Menu category not found");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setSortOrder(request.getSortOrder());
        category.setIsActive(request.getIsActive());
        category.setAvailableFrom(request.getAvailableFrom());
        category.setAvailableUntil(request.getAvailableUntil());

        MenuCategory savedCategory = menuCategoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    /**
     * Delete a menu category
     */
    public void deleteCategory(UUID restaurantId, UUID categoryId) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu category not found"));

        // Verify category belongs to restaurant
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Menu category not found");
        }

        menuCategoryRepository.delete(category);
    }

    /**
     * Get all menu items for a restaurant
     */
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getItems(UUID restaurantId) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        List<MenuItem> items = menuItemRepository.findByRestaurantIdOrderBySortOrderAsc(restaurantId);

        return items.stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get menu items for a specific category
     */
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getItemsByCategory(UUID restaurantId, UUID categoryId) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu category not found"));

        // Verify category belongs to restaurant
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Menu category not found");
        }

        List<MenuItem> items = menuItemRepository.findByCategoryIdOrderBySortOrderAsc(categoryId);

        return items.stream()
                .map(this::mapToItemResponse)
                .collect(Collectors.toList());
    }

    /**
     * Create a new menu item
     */
    public MenuItemResponse createItem(UUID restaurantId, UUID categoryId, MenuItemRequest request) {
        MenuCategory category = menuCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu category not found"));

        // Verify category belongs to restaurant
        if (!category.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Menu category not found");
        }

        MenuItem item = new MenuItem();
        item.setRestaurant(category.getRestaurant()); // Set the restaurant from the category
        item.setCategory(category);
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        item.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 
                menuItemRepository.getNextSortOrder(categoryId));
        item.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        item.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);
        item.setAllergens(request.getAllergens());

        MenuItem savedItem = menuItemRepository.save(item);
        return mapToItemResponse(savedItem);
    }

    /**
     * Update a menu item
     */
    public MenuItemResponse updateItem(UUID restaurantId, UUID itemId, MenuItemRequest request) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        // Verify item belongs to restaurant
        if (!item.getCategory().getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Menu item not found");
        }

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        // Only update sortOrder if provided in request, otherwise keep existing value
        if (request.getSortOrder() != null) {
            item.setSortOrder(request.getSortOrder());
        }
        // Only update isActive if provided in request, otherwise keep existing value
        if (request.getIsActive() != null) {
            item.setIsActive(request.getIsActive());
        }
        // Only update isAvailable if provided in request, otherwise keep existing value
        if (request.getIsAvailable() != null) {
            item.setIsAvailable(request.getIsAvailable());
        }
        item.setAllergens(request.getAllergens());

        MenuItem savedItem = menuItemRepository.save(item);
        return mapToItemResponse(savedItem);
    }

    /**
     * Delete a menu item
     */
    public void deleteItem(UUID restaurantId, UUID itemId) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        // Verify item belongs to restaurant
        if (!item.getCategory().getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Menu item not found");
        }

        menuItemRepository.delete(item);
    }

    /**
     * Map MenuCategory entity to response DTO
     */
    private MenuCategoryResponse mapToCategoryResponse(MenuCategory category) {
        MenuCategoryResponse response = new MenuCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setImageUrl(category.getImageUrl());
        response.setSortOrder(category.getSortOrder());
        response.setIsActive(category.getIsActive());
        response.setAvailableFrom(category.getAvailableFrom());
        response.setAvailableUntil(category.getAvailableUntil());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }

    /**
     * Map MenuItem entity to response DTO
     */
    private MenuItemResponse mapToItemResponse(MenuItem item) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(item.getId());
        response.setCategoryId(item.getCategory().getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setPrice(item.getPrice());
        response.setImageUrl(item.getImageUrl());
        response.setSortOrder(item.getSortOrder());
        response.setIsActive(item.getIsActive());
        response.setIsAvailable(item.getIsAvailable());
        response.setAllergens(item.getAllergens());
        response.setCreatedAt(item.getCreatedAt());
        response.setUpdatedAt(item.getUpdatedAt());
        return response;
    }
}
