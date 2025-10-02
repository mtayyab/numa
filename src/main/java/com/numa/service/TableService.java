package com.numa.service;

import com.numa.domain.entity.Restaurant;
import com.numa.domain.entity.RestaurantTable;
import com.numa.domain.enums.TableStatus;
import com.numa.dto.request.TableRequest;
import com.numa.dto.response.TableResponse;
import com.numa.exception.ResourceNotFoundException;
import com.numa.repository.RestaurantRepository;
import com.numa.repository.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for table management operations.
 * Handles table CRUD operations, QR code generation, and status updates.
 */
@Service
@Transactional(readOnly = true)
public class TableService {

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Get all tables for a restaurant
     */
    public List<TableResponse> getTables(UUID restaurantId) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        List<RestaurantTable> tables = tableRepository.findByRestaurantIdOrderByTableNumberAsc(restaurantId);
        return tables.stream()
                .map(this::mapToTableResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get table by ID
     */
    public TableResponse getTable(UUID restaurantId, UUID tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        // Verify table belongs to restaurant
        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Table not found");
        }

        return mapToTableResponse(table);
    }

    /**
     * Create a new table
     */
    @Transactional
    public TableResponse createTable(UUID restaurantId, TableRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // Check if table number already exists for this restaurant
        if (tableRepository.existsByRestaurantIdAndTableNumber(restaurantId, request.getTableNumber())) {
            throw new IllegalArgumentException("Table number already exists for this restaurant");
        }

        RestaurantTable table = new RestaurantTable();
        table.setRestaurant(restaurant);
        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setLocationDescription(request.getLocation());
        table.setStatus(TableStatus.AVAILABLE);

        RestaurantTable savedTable = tableRepository.save(table);
        return mapToTableResponse(savedTable);
    }

    /**
     * Update a table
     */
    @Transactional
    public TableResponse updateTable(UUID restaurantId, UUID tableId, TableRequest request) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        // Verify table belongs to restaurant
        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Table not found");
        }

        // Check if new table number conflicts with existing tables (excluding current table)
        if (!table.getTableNumber().equals(request.getTableNumber()) &&
            tableRepository.existsByRestaurantIdAndTableNumberAndIdNot(restaurantId, request.getTableNumber(), tableId)) {
            throw new IllegalArgumentException("Table number already exists for this restaurant");
        }

        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setLocationDescription(request.getLocation());

        RestaurantTable savedTable = tableRepository.save(table);
        return mapToTableResponse(savedTable);
    }

    /**
     * Delete a table
     */
    @Transactional
    public void deleteTable(UUID restaurantId, UUID tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        // Verify table belongs to restaurant
        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Table not found");
        }

        tableRepository.delete(table);
    }

    /**
     * Generate QR code for a table
     */
    @Transactional
    public TableResponse generateQrCode(UUID restaurantId, UUID tableId) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        // Verify table belongs to restaurant
        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Table not found");
        }

        // Generate QR code (simple UUID for now, can be enhanced with more complex logic)
        String qrCode = "TBL-" + tableId.toString().substring(0, 8).toUpperCase();
        table.setQrCode(qrCode);

        RestaurantTable savedTable = tableRepository.save(table);
        return mapToTableResponse(savedTable);
    }

    /**
     * Update table status
     */
    @Transactional
    public TableResponse updateTableStatus(UUID restaurantId, UUID tableId, String status) {
        RestaurantTable table = tableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        // Verify table belongs to restaurant
        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Table not found");
        }

        try {
            TableStatus newStatus = TableStatus.valueOf(status.toUpperCase());
            table.setStatus(newStatus);
            RestaurantTable savedTable = tableRepository.save(table);
            return mapToTableResponse(savedTable);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid table status: " + status);
        }
    }

    /**
     * Map RestaurantTable entity to response DTO
     */
    private TableResponse mapToTableResponse(RestaurantTable table) {
        TableResponse response = new TableResponse();
        response.setId(table.getId());
        response.setTableNumber(table.getTableNumber());
        response.setCapacity(table.getCapacity());
        response.setLocation(table.getLocationDescription());
        response.setDescription(null); // RestaurantTable doesn't have description field
        response.setStatus(table.getStatus().toString());
        response.setQrCode(table.getQrCode());
        response.setCreatedAt(table.getCreatedAt());
        response.setUpdatedAt(table.getUpdatedAt());
        return response;
    }
}
