package com.numa.service;

import com.numa.domain.entity.User;
import com.numa.domain.entity.Restaurant;
import com.numa.dto.request.LoginRequest;
import com.numa.dto.request.RefreshTokenRequest;
import com.numa.dto.response.AuthResponse;
import com.numa.exception.ValidationException;
import com.numa.exception.ResourceNotFoundException;
import com.numa.repository.UserRepository;
import com.numa.repository.RestaurantRepository;
import com.numa.security.JwtUtil;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service for authentication operations.
 * Handles user login, token generation, and authentication management.
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Authenticate user and generate tokens
     */
    public AuthResponse authenticate(LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;

        // Update last login time
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(userDetails, 
                                                        user.getRestaurant().getId(), 
                                                        user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails, user.getRestaurant().getId());

        // Build response
        AuthResponse response = new AuthResponse(accessToken, refreshToken, 86400L); // 24 hours
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole().name());
        response.setRestaurantId(user.getRestaurant().getId());
        response.setRestaurantName(user.getRestaurant().getName());

        return response;
    }

    /**
     * Refresh access token
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        if (!jwtUtil.isRefreshToken(refreshToken)) {
            throw new ValidationException("Invalid refresh token");
        }

        String username = jwtUtil.getUsernameFromToken(refreshToken);
        User user = userRepository.findByEmailAndStatus(username, "ACTIVE")
                .orElseThrow(() -> new ValidationException("User not found"));

        if (!jwtUtil.validateRefreshToken(refreshToken, username)) {
            throw new ValidationException("Invalid or expired refresh token");
        }

        // Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(user, 
                                                           user.getRestaurant().getId(), 
                                                           user.getRole().name());

        // Build response
        AuthResponse response = new AuthResponse(newAccessToken, refreshToken, 86400L);
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole().name());
        response.setRestaurantId(user.getRestaurant().getId());
        response.setRestaurantName(user.getRestaurant().getName());

        return response;
    }

    /**
     * Get current authenticated user information
     */
    @Transactional(readOnly = true)
    public Object getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("No authenticated user found");
        }


        Object principal = authentication.getPrincipal();
        System.out.println("Principal: " + principal);
        
        if (principal instanceof User) {
            User user = (User) principal;
            
            // Get restaurant ID from JWT token to avoid lazy loading issues
            UUID restaurantId = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .filter(auth -> auth.startsWith("RESTAURANT_"))
            .map(auth -> auth.substring("RESTAURANT_".length()))
            .map(UUID::fromString)
            .findFirst()
            .orElseThrow(() -> new ValidationException("Restaurant ID not found"));
            System.out.println("Restaurant ID: " + restaurantId);
            
            // Fetch restaurant data separately to avoid lazy loading issues
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
            
            // Return user info without sensitive data
            return new UserInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                restaurant.getId(),
                restaurant.getName()
            );
        }
        
        throw new ValidationException("Invalid user principal");
    }

    // Inner class for user info response
    public static class UserInfoResponse {
        private final java.util.UUID userId;
        private final String email;
        private final String firstName;
        private final String lastName;
        private final String role;
        private final java.util.UUID restaurantId;
        private final String restaurantName;

        public UserInfoResponse(java.util.UUID userId, String email, String firstName, String lastName, 
                              String role, java.util.UUID restaurantId, String restaurantName) {
            this.userId = userId;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.restaurantId = restaurantId;
            this.restaurantName = restaurantName;
        }

        // Getters
        public java.util.UUID getUserId() { return userId; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getRole() { return role; }
        public java.util.UUID getRestaurantId() { return restaurantId; }
        public String getRestaurantName() { return restaurantName; }
    }
}
