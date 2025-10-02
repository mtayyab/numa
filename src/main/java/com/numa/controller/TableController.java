package com.numa.controller;

import com.numa.dto.request.TableRequest;
import com.numa.dto.response.TableResponse;
import com.numa.service.TableService;
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
 * REST controller for table management operations.
 * Handles table CRUD operations and QR code generation for restaurant staff.
 */
@RestController
@RequestMapping("/restaurants/{restaurantId}/tables")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Table Management", description = "Endpoints for table management")
public class TableController {

    @Autowired
    private TableService tableService;

    /**
     * Get all tables for a restaurant
     */
    @Operation(summary = "Get Restaurant Tables", description = "Get all tables for a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tables retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<TableResponse>> getTables(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId) {
        List<TableResponse> tables = tableService.getTables(restaurantId);
        return ResponseEntity.ok(tables);
    }

    /**
     * Get table by ID
     */
    @Operation(summary = "Get Table by ID", description = "Get a specific table by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @GetMapping("/{tableId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<TableResponse> getTable(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Table ID") @PathVariable UUID tableId) {
        TableResponse table = tableService.getTable(restaurantId, tableId);
        return ResponseEntity.ok(table);
    }

    /**
     * Create a new table
     */
    @Operation(summary = "Create Table", description = "Create a new table for the restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Table created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @PostMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<TableResponse> createTable(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Valid @RequestBody TableRequest request) {
        TableResponse table = tableService.createTable(restaurantId, request);
        return ResponseEntity.status(201).body(table);
    }

    /**
     * Update a table
     */
    @Operation(summary = "Update Table", description = "Update table information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @PutMapping("/{tableId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<TableResponse> updateTable(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Table ID") @PathVariable UUID tableId,
            @Valid @RequestBody TableRequest request) {
        TableResponse table = tableService.updateTable(restaurantId, tableId, request);
        return ResponseEntity.ok(table);
    }

    /**
     * Delete a table
     */
    @Operation(summary = "Delete Table", description = "Delete a table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Table deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @DeleteMapping("/{tableId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteTable(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Table ID") @PathVariable UUID tableId) {
        tableService.deleteTable(restaurantId, tableId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Generate QR code for a table
     */
    @Operation(summary = "Generate QR Code", description = "Generate QR code for a table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QR code generated successfully"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @PostMapping("/{tableId}/qr-code")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<TableResponse> generateQrCode(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Table ID") @PathVariable UUID tableId) {
        TableResponse table = tableService.generateQrCode(restaurantId, tableId);
        return ResponseEntity.ok(table);
    }

    /**
     * Update table status
     */
    @Operation(summary = "Update Table Status", description = "Update the status of a table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @PatchMapping("/{tableId}/status")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<TableResponse> updateTableStatus(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Table ID") @PathVariable UUID tableId,
            @RequestParam String status) {
        TableResponse table = tableService.updateTableStatus(restaurantId, tableId, status);
        return ResponseEntity.ok(table);
    }
}
