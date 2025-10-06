package com.numa.dto.response;

import java.util.List;
import java.util.UUID;

/**
 * Response DTO for guest session data.
 * Uses lightweight DTOs to avoid lazy loading issues.
 */
public class GuestSessionResponse {
    
    private UUID sessionId;
    private String sessionToken;
    private String guestToken;
    private String guestName;
    private GuestDiningSessionDTO session;
    private List<GuestSessionGuestDTO> guests;
    private List<GuestOrderResponse> cartItems;
    private List<GuestOrderResponse> orders;
    
    // Constructors
    public GuestSessionResponse() {}
    
    public GuestSessionResponse(UUID sessionId, String sessionToken, String guestToken, String guestName, 
                               GuestDiningSessionDTO session, List<GuestSessionGuestDTO> guests, 
                               List<GuestOrderResponse> cartItems, List<GuestOrderResponse> orders) {
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
    
    public GuestDiningSessionDTO getSession() {
        return session;
    }
    
    public void setSession(GuestDiningSessionDTO session) {
        this.session = session;
    }
    
    public List<GuestSessionGuestDTO> getGuests() {
        return guests;
    }
    
    public void setGuests(List<GuestSessionGuestDTO> guests) {
        this.guests = guests;
    }
    
    public List<GuestOrderResponse> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<GuestOrderResponse> cartItems) {
        this.cartItems = cartItems;
    }
    
    public List<GuestOrderResponse> getOrders() {
        return orders;
    }
    
    public void setOrders(List<GuestOrderResponse> orders) {
        this.orders = orders;
    }
}
