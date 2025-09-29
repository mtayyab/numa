package com.numa.dto.response;

import com.numa.domain.entity.DiningSession;
import com.numa.domain.entity.SessionGuest;
import com.numa.domain.entity.Order;

import java.util.List;
import java.util.UUID;

/**
 * Response DTO for guest session data
 */
public class GuestSessionResponse {
    
    private UUID sessionId;
    private String sessionToken;
    private String guestToken;
    private String guestName;
    private DiningSession session;
    private List<SessionGuest> guests;
    private List<Order> cartItems;
    private List<Order> orders;
    
    // Constructors
    public GuestSessionResponse() {}
    
    public GuestSessionResponse(UUID sessionId, String sessionToken, String guestToken, String guestName, 
                               DiningSession session, List<SessionGuest> guests, 
                               List<Order> cartItems, List<Order> orders) {
        this.sessionId = sessionId;
        this.sessionToken = sessionToken;
        this.guestToken = guestToken;
        this.guestName = guestName;
        this.session = session;
        this.guests = guests;
        this.cartItems = cartItems;
        this.orders = orders;
    }
    
    // Getters and Setters
    public UUID getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getSessionToken() {
        return sessionToken;
    }
    
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
    
    public String getGuestToken() {
        return guestToken;
    }
    
    public void setGuestToken(String guestToken) {
        this.guestToken = guestToken;
    }
    
    public String getGuestName() {
        return guestName;
    }
    
    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }
    
    public DiningSession getSession() {
        return session;
    }
    
    public void setSession(DiningSession session) {
        this.session = session;
    }
    
    public List<SessionGuest> getGuests() {
        return guests;
    }
    
    public void setGuests(List<SessionGuest> guests) {
        this.guests = guests;
    }
    
    public List<Order> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<Order> cartItems) {
        this.cartItems = cartItems;
    }
    
    public List<Order> getOrders() {
        return orders;
    }
    
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
