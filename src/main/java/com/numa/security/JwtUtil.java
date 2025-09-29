package com.numa.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * JWT utility class for token generation, validation, and parsing.
 * Handles both access tokens and refresh tokens with multi-tenant support.
 */
@Component
public class JwtUtil {

    @Value("${spring.security.jwt.secret-key}")
    private String jwtSecret;

    @Value("${spring.security.jwt.expiration}")
    private int jwtExpirationMs;

    @Value("${spring.security.jwt.refresh-expiration}")
    private int jwtRefreshExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate access token for authenticated user
     */
    public String generateAccessToken(UserDetails userDetails, UUID restaurantId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("restaurantId", restaurantId.toString());
        claims.put("role", role);
        claims.put("type", "access");
        
        return createToken(claims, userDetails.getUsername(), jwtExpirationMs);
    }

    /**
     * Generate refresh token for authenticated user
     */
    public String generateRefreshToken(UserDetails userDetails, UUID restaurantId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("restaurantId", restaurantId.toString());
        claims.put("type", "refresh");
        
        return createToken(claims, userDetails.getUsername(), jwtRefreshExpirationMs);
    }

    /**
     * Generate guest token for table sessions (no authentication required)
     */
    public String generateGuestToken(String sessionId, String guestName, UUID restaurantId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sessionId", sessionId);
        claims.put("guestName", guestName);
        claims.put("restaurantId", restaurantId.toString());
        claims.put("type", "guest");
        
        // Guest tokens are valid for 8 hours
        return createToken(claims, "guest_" + sessionId, 8 * 60 * 60 * 1000);
    }

    /**
     * Create JWT token with claims and expiration
     */
    private String createToken(Map<String, Object> claims, String subject, int expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract username from JWT token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extract restaurant ID from JWT token
     */
    public UUID getRestaurantIdFromToken(String token) {
        String restaurantIdStr = getClaimFromToken(token, claims -> 
            claims.get("restaurantId", String.class));
        return restaurantIdStr != null ? UUID.fromString(restaurantIdStr) : null;
    }

    /**
     * Extract role from JWT token
     */
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    /**
     * Extract token type from JWT token
     */
    public String getTokenTypeFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("type", String.class));
    }

    /**
     * Extract session ID from guest token
     */
    public String getSessionIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("sessionId", String.class));
    }

    /**
     * Extract guest name from guest token
     */
    public String getGuestNameFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("guestName", String.class));
    }

    /**
     * Extract expiration date from JWT token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extract specific claim from JWT token
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from JWT token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if JWT token is expired
     */
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Validate JWT token against user details
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            final String tokenType = getTokenTypeFromToken(token);
            
            return (username.equals(userDetails.getUsername()) 
                    && !isTokenExpired(token)
                    && "access".equals(tokenType));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Validate refresh token
     */
    public Boolean validateRefreshToken(String token, String username) {
        try {
            final String tokenUsername = getUsernameFromToken(token);
            final String tokenType = getTokenTypeFromToken(token);
            
            return (tokenUsername.equals(username) 
                    && !isTokenExpired(token)
                    && "refresh".equals(tokenType));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Validate guest token
     */
    public Boolean validateGuestToken(String token) {
        try {
            final String tokenType = getTokenTypeFromToken(token);
            return !isTokenExpired(token) && "guest".equals(tokenType);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check if token is a guest token
     */
    public Boolean isGuestToken(String token) {
        try {
            final String tokenType = getTokenTypeFromToken(token);
            return "guest".equals(tokenType);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check if token is an access token
     */
    public Boolean isAccessToken(String token) {
        try {
            final String tokenType = getTokenTypeFromToken(token);
            return "access".equals(tokenType);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check if token is a refresh token
     */
    public Boolean isRefreshToken(String token) {
        try {
            final String tokenType = getTokenTypeFromToken(token);
            return "refresh".equals(tokenType);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get remaining time in milliseconds before token expires
     */
    public Long getTokenRemainingTime(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            return expiration.getTime() - System.currentTimeMillis();
        } catch (JwtException | IllegalArgumentException e) {
            return 0L;
        }
    }
}
