package com.numa.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JWT Authentication Filter that intercepts requests and validates JWT tokens.
 * Supports both user authentication tokens and guest session tokens.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = getJwtFromRequest(request);
            logger.info("JWT Filter processing request: " + request.getRequestURI() + ", JWT present: " + (jwt != null));
            
            if (StringUtils.hasText(jwt)) {
                logger.info("JWT token: " + jwt.substring(0, Math.min(50, jwt.length())) + "...");
                
                // Check if it's a guest token
                if (jwtUtil.isGuestToken(jwt) && jwtUtil.validateGuestToken(jwt)) {
                    logger.info("Processing guest token");
                    // For guest tokens, we create a simple authentication without user details
                    setGuestAuthentication(request, jwt);
                } else if (jwtUtil.isAccessToken(jwt)) {
                    logger.info("Processing access token");
                    // For user tokens, validate against user details
                    String username = jwtUtil.getUsernameFromToken(jwt);
                    logger.info("Username from token: " + username);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        logger.info("User details loaded: " + userDetails.getUsername());
                        
                        if (jwtUtil.validateToken(jwt, userDetails)) {
                            logger.info("Token validation successful, setting authentication");
                            setUserAuthentication(request, userDetails);
                        } else {
                            logger.warn("Token validation failed");
                        }
                    } else {
                        logger.warn("Username is null or authentication already exists");
                    }
                } else {
                    logger.warn("Token is not an access token");
                }
            } else {
                logger.info("No JWT token found in request");
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Set authentication for regular authenticated users
     */
    private void setUserAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Set authentication for guest users
     */
    private void setGuestAuthentication(HttpServletRequest request, String jwt) {
        String sessionId = jwtUtil.getSessionIdFromToken(jwt);
        String guestName = jwtUtil.getGuestNameFromToken(jwt);
        
        // Create a guest user details object
        GuestUserDetails guestDetails = new GuestUserDetails(sessionId, guestName, 
                                                            jwtUtil.getRestaurantIdFromToken(jwt));
        
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(guestDetails, null, guestDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Extract JWT token from request header
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Skip filter for certain paths
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // Debug logging
        logger.info("JWT Filter checking path: " + path);
        
        // Since Spring Boot has context-path: /api/v1, the actual path will be without the context path
        // So /api/v1/auth/me becomes /auth/me in the filter
        boolean shouldSkip = path.startsWith("/actuator/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs/") ||
               path.startsWith("/guest/") ||
               path.startsWith("/public/") ||
               path.startsWith("/qr/") ||
               path.equals("/auth/login") ||
               path.equals("/auth/register") ||
               path.equals("/auth/refresh") ||
               path.startsWith("/auth/forgot-password") ||
               path.startsWith("/auth/reset-password") ||
               path.startsWith("/auth/verify-email") ||
               path.equals("/restaurants/register") ||
               path.equals("/restaurants/active") ||
               path.startsWith("/restaurants/by-slug/");
        
        logger.info("Should skip JWT filter for path " + path + ": " + shouldSkip);
        
        // Force skip for debugging
        if (path.equals("/auth/login") || path.equals("/restaurants/register")) {
            logger.info("FORCING SKIP for path: " + path);
            return true;
        }
        
        return shouldSkip;
    }
}
