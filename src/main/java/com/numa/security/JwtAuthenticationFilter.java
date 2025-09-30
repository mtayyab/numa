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

/**
 * JWT Authentication Filter that intercepts requests and validates JWT tokens.
 * Supports both user authentication tokens and guest session tokens.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt)) {
                // Check if it's a guest token
                if (jwtUtil.isGuestToken(jwt) && jwtUtil.validateGuestToken(jwt)) {
                    // For guest tokens, we create a simple authentication without user details
                    setGuestAuthentication(request, jwt);
                } else if (jwtUtil.isAccessToken(jwt)) {
                    // For user tokens, validate against user details
                    String username = jwtUtil.getUsernameFromToken(jwt);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        
                        if (jwtUtil.validateToken(jwt, userDetails)) {
                            setUserAuthentication(request, userDetails);
                        }
                    }
                }
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
        
        // Skip JWT filter for public endpoints
        return path.startsWith("/actuator/") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs/") ||
               path.startsWith("/api/v1/guest/") ||
               path.startsWith("/guest/") ||
               path.startsWith("/api/v1/public/") ||
               path.startsWith("/public/") ||
               path.startsWith("/api/v1/qr/") ||
               path.startsWith("/qr/") ||
               path.equals("/api/v1/auth/login") ||
               path.equals("/auth/login") ||
               path.equals("/api/v1/auth/register") ||
               path.equals("/auth/register") ||
               path.equals("/api/v1/auth/refresh") ||
               path.equals("/auth/refresh") ||
               path.startsWith("/api/v1/auth/forgot-password") ||
               path.startsWith("/auth/forgot-password") ||
               path.startsWith("/api/v1/auth/reset-password") ||
               path.startsWith("/auth/reset-password") ||
               path.startsWith("/api/v1/auth/verify-email") ||
               path.startsWith("/auth/verify-email") ||
               path.equals("/api/v1/restaurants/register") ||
               path.equals("/restaurants/register") ||
               path.equals("/api/v1/restaurants/active") ||
               path.equals("/restaurants/active") ||
               path.startsWith("/api/v1/restaurants/by-slug/") ||
               path.startsWith("/restaurants/by-slug/");
    }
}
