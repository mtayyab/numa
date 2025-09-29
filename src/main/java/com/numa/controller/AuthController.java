package com.numa.controller;

import com.numa.dto.request.LoginRequest;
import com.numa.dto.request.RefreshTokenRequest;
import com.numa.dto.response.AuthResponse;
import com.numa.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 * Handles user login, token refresh, and authentication-related endpoints.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentication", description = "Authentication and authorization endpoints")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Authenticate user and return JWT tokens
     */
    @Operation(summary = "User Login", description = "Authenticate user credentials and return JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh access token using refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout user (invalidate tokens)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // TODO: Implement token blacklisting if needed
        return ResponseEntity.ok().build();
    }

    /**
     * Get current authenticated user info
     */
    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser() {
        Object userInfo = authService.getCurrentUserInfo();
        return ResponseEntity.ok(userInfo);
    }
}
