package com.numa.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Authentication Entry Point that handles authentication failures.
 * Returns structured JSON error responses for unauthorized requests.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now().toString());
        errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", "Authentication required to access this resource");
        errorDetails.put("path", request.getRequestURI());

        // Add more specific error details based on the request
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            errorDetails.put("details", "No authentication token provided");
        } else if (!authHeader.startsWith("Bearer ")) {
            errorDetails.put("details", "Invalid authentication token format");
        } else {
            errorDetails.put("details", "Invalid or expired authentication token");
        }

        objectMapper.writeValue(response.getOutputStream(), errorDetails);
    }
}
