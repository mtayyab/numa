package com.numa.controller;

import com.numa.dto.response.ActiveSessionResponse;
import com.numa.dto.response.SessionHistoryResponse;
import com.numa.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for session management operations
 */
@RestController
@RequestMapping("/sessions")
@Tag(name = "Session Management", description = "APIs for managing dining sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    /**
     * Get active sessions for a restaurant
     */
    @Operation(summary = "Get Active Sessions", description = "Get all active sessions for a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active sessions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurant/{restaurantId}/active")
    public ResponseEntity<List<ActiveSessionResponse>> getActiveSessions(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId) {
        List<ActiveSessionResponse> sessions = sessionService.getActiveSessions(restaurantId);
        return ResponseEntity.ok(sessions);
    }

    /**
     * Get session history for a restaurant
     */
    @Operation(summary = "Get Session History", description = "Get session history for a restaurant with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session history retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurant/{restaurantId}/history")
    public ResponseEntity<Page<SessionHistoryResponse>> getSessionHistory(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<SessionHistoryResponse> history = sessionService.getSessionHistory(restaurantId, pageable);
        return ResponseEntity.ok(history);
    }

    /**
     * End a session
     */
    @Operation(summary = "End Session", description = "End an active dining session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session ended successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "400", description = "Session already ended")
    })
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<ActiveSessionResponse> endSession(
            @Parameter(description = "Session ID") @PathVariable UUID sessionId) {
        ActiveSessionResponse session = sessionService.endSession(sessionId);
        return ResponseEntity.ok(session);
    }

    /**
     * Get session details with orders
     */
    @Operation(summary = "Get Session Details", description = "Get detailed session information including orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @GetMapping("/{sessionId}/details")
    public ResponseEntity<ActiveSessionResponse> getSessionDetails(
            @Parameter(description = "Session ID") @PathVariable UUID sessionId) {
        ActiveSessionResponse session = sessionService.getSessionDetails(sessionId);
        return ResponseEntity.ok(session);
    }
}
