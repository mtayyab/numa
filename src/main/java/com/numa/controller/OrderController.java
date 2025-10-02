package com.numa.controller;

import com.numa.dto.request.OrderStatusUpdateRequest;
import com.numa.dto.response.OrderResponse;
import com.numa.service.OrderService;
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
 * REST controller for order management operations.
 * Handles order CRUD operations and status updates for restaurant staff.
 */
@RestController
@RequestMapping("/restaurants/{restaurantId}/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Order Management", description = "Endpoints for order management")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Get all orders for a restaurant
     */
    @Operation(summary = "Get Restaurant Orders", description = "Get all orders for a restaurant with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<List<OrderResponse>> getOrders(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Order status filter") @RequestParam(required = false) String status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        List<OrderResponse> orders = orderService.getOrders(restaurantId, status, page, size);
        return ResponseEntity.ok(orders);
    }

    /**
     * Get order by ID
     */
    @Operation(summary = "Get Order by ID", description = "Get a specific order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Order ID") @PathVariable UUID orderId) {
        OrderResponse order = orderService.getOrder(restaurantId, orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Update order status
     */
    @Operation(summary = "Update Order Status", description = "Update the status of an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Order ID") @PathVariable UUID orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse order = orderService.updateOrderStatus(restaurantId, orderId, request.getStatus());
        return ResponseEntity.ok(order);
    }

    /**
     * Cancel order
     */
    @Operation(summary = "Cancel Order", description = "Cancel an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled")
    })
    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasRole('OWNER') or hasRole('MANAGER') or hasRole('STAFF')")
    public ResponseEntity<OrderResponse> cancelOrder(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Order ID") @PathVariable UUID orderId) {
        OrderResponse order = orderService.cancelOrder(restaurantId, orderId);
        return ResponseEntity.ok(order);
    }
}
