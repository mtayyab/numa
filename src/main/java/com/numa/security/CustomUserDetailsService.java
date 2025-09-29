package com.numa.security;

import com.numa.domain.entity.User;
import com.numa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom UserDetailsService implementation for loading user-specific data.
 * Integrates with Spring Security for authentication and authorization.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndStatus(email, "ACTIVE")
                .orElseThrow(() -> new UsernameNotFoundException(
                    "User not found or inactive with email: " + email));

        return user;
    }

    /**
     * Load user by ID (useful for token validation)
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException(
                    "User not found with id: " + userId));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new UsernameNotFoundException("User account is not active: " + userId);
        }

        return user;
    }
}
