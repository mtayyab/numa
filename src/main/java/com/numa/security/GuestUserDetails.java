package com.numa.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * UserDetails implementation for guest users in dining sessions.
 * Provides minimal authentication for guests without requiring registration.
 */
public class GuestUserDetails implements UserDetails {

    private final String sessionId;
    private final String guestName;
    private final UUID restaurantId;
    private final String username;

    public GuestUserDetails(String sessionId, String guestName, UUID restaurantId) {
        this.sessionId = sessionId;
        this.guestName = guestName;
        this.restaurantId = restaurantId;
        this.username = "guest_" + sessionId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_GUEST"),
            new SimpleGrantedAuthority("SESSION_" + sessionId),
            new SimpleGrantedAuthority("RESTAURANT_" + restaurantId)
        );
    }

    @Override
    public String getPassword() {
        return null; // Guests don't have passwords
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Guest-specific getters
    public String getSessionId() {
        return sessionId;
    }

    public String getGuestName() {
        return guestName;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public boolean isGuest() {
        return true;
    }
}
