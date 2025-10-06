package com.numa.controller;

import com.numa.dto.request.GuestJoinSessionRequest;
import com.numa.dto.request.GuestOrderRequest;
import com.numa.dto.response.GuestMenuResponse;
import com.numa.dto.response.GuestSessionResponse;
import com.numa.dto.response.GuestOrderResponse;
import com.numa.dto.response.GuestRestaurantResponse;
import com.numa.dto.response.GuestTableResponse;
import com.numa.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for guest operations.
 * Handles guest menu browsing, session joining, and ordering without authentication.
 */
@RestController
@RequestMapping("/guest")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Guest Operations", description = "Endpoints for guest users (no authentication required)")
public class GuestController {

    @Autowired
    private GuestService guestService;

    /**
     * Get restaurant information by slug
     */
    @Operation(summary = "Get Restaurant by Slug", description = "Get restaurant information by slug for guest access")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant found"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurants/{slug}")
    public ResponseEntity<GuestRestaurantResponse> getRestaurantBySlug(
            @Parameter(description = "Restaurant slug") @PathVariable String slug) {
        GuestRestaurantResponse restaurant = guestService.getRestaurantBySlug(slug);
        return ResponseEntity.ok(restaurant);
    }

    /**
     * Get table information by QR code
     */
    @Operation(summary = "Get Table by QR Code", description = "Get table information by QR code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Table found"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @GetMapping("/tables/{qrCode}")
    public ResponseEntity<GuestTableResponse> getTableByQrCode(
            @Parameter(description = "Table QR code") @PathVariable String qrCode) {
        GuestTableResponse table = guestService.getTableByQrCode(qrCode);
        return ResponseEntity.ok(table);
    }

    /**
     * Check for active session on a table
     */
    @Operation(summary = "Check Active Session", description = "Check if there's an active session on a table")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session status retrieved"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @GetMapping("/tables/{qrCode}/active-session")
    public ResponseEntity<Object> getActiveSessionForTable(
            @Parameter(description = "Table QR code") @PathVariable String qrCode) {
        Object sessionInfo = guestService.getActiveSessionForTable(qrCode);
        return ResponseEntity.ok(sessionInfo);
    }

    /**
     * Get public menu for a restaurant
     */
    @Operation(summary = "Get Public Menu", description = "Get public menu for a restaurant (only active items)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurants/{slug}/menu")
    public ResponseEntity<GuestMenuResponse> getPublicMenu(
            @Parameter(description = "Restaurant slug") @PathVariable String slug) {
        GuestMenuResponse menu = guestService.getPublicMenu(slug);
        return ResponseEntity.ok(menu);
    }

    /**
     * Join a dining session
     */
    @Operation(summary = "Join Dining Session", description = "Join an existing dining session or create a new one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session joined successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Table not found")
    })
    @PostMapping("/sessions/join")
    public ResponseEntity<GuestSessionResponse> joinSession(
            @Valid @RequestBody GuestJoinSessionRequest request) {
        GuestSessionResponse response = guestService.joinSession(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get session information by session code
     */
    @Operation(summary = "Get Session Info", description = "Get information about a dining session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session found"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @GetMapping("/sessions/{sessionCode}")
    public ResponseEntity<GuestSessionResponse> getSession(
            @Parameter(description = "Session code") @PathVariable String sessionCode) {
        GuestSessionResponse session = guestService.getSessionByCode(sessionCode);
        return ResponseEntity.ok(session);
    }

    /**
     * Add item to session cart
     */
    @Operation(summary = "Add Item to Cart", description = "Add a menu item to the session cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Session or item not found")
    })
    @PostMapping("/sessions/{sessionId}/cart")
    public ResponseEntity<GuestSessionResponse> addToCart(
            @Parameter(description = "Session ID") @PathVariable UUID sessionId,
            @Valid @RequestBody GuestOrderRequest request) {
        GuestSessionResponse response = guestService.addToCart(sessionId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove item from session cart
     */
    @Operation(summary = "Remove Item from Cart", description = "Remove a menu item from the session cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Session or item not found")
    })
    @DeleteMapping("/sessions/{sessionId}/cart/{orderId}")
    public ResponseEntity<GuestSessionResponse> removeFromCart(
            @Parameter(description = "Session ID") @PathVariable UUID sessionId,
            @Parameter(description = "Order ID") @PathVariable UUID orderId) {
        GuestSessionResponse response = guestService.removeFromCart(sessionId, orderId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update cart item quantity
     */
    @Operation(summary = "Update Cart Item", description = "Update quantity of an item in the session cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Session or item not found")
    })
    @PutMapping("/sessions/{sessionId}/cart/{orderId}")
    public ResponseEntity<GuestSessionResponse> updateCartItem(
            @Parameter(description = "Session ID") @PathVariable UUID sessionId,
            @Parameter(description = "Order ID") @PathVariable UUID orderId,
            @Valid @RequestBody GuestOrderRequest request) {
        GuestSessionResponse response = guestService.updateCartItem(sessionId, orderId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Submit session order
     */
    @Operation(summary = "Submit Order", description = "Submit all items in the session cart as an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @PostMapping("/sessions/{sessionId}/orders")
    public ResponseEntity<GuestOrderResponse> submitOrder(
            @Parameter(description = "Session ID") @PathVariable UUID sessionId) {
        GuestOrderResponse response = guestService.submitOrder(sessionId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get session orders
     */
    @Operation(summary = "Get Session Orders", description = "Get all orders for a dining session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @GetMapping("/sessions/{sessionId}/orders")
    public ResponseEntity<List<GuestOrderResponse>> getSessionOrders(
            @Parameter(description = "Session ID") @PathVariable UUID sessionId) {
        List<GuestOrderResponse> orders = guestService.getSessionOrders(sessionId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Leave dining session
     */
    @Operation(summary = "Leave Session", description = "Leave a dining session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Left session successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @PostMapping("/sessions/{sessionId}/leave")
    public ResponseEntity<Void> leaveSession(
            @Parameter(description = "Session ID") @PathVariable UUID sessionId,
            @Parameter(description = "Guest token") @RequestParam String guestToken) {
        guestService.leaveSession(sessionId, guestToken);
        return ResponseEntity.ok().build();
    }
}
