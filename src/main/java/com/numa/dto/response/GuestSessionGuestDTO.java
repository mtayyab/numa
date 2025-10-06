package com.numa.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Lightweight DTO for guest session guest information.
 * Contains only the fields needed for guest interface to avoid lazy loading issues.
 */
public class GuestSessionGuestDTO {
    
    private UUID id;
    private String guestName;
    private String guestPhone;
    private Boolean isHost;
    private String joinToken;
    private LocalDateTime joinedAt;
    private LocalDateTime lastActivityAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public GuestSessionGuestDTO() {}
    
    public GuestSessionGuestDTO(UUID id, String guestName, String guestPhone, Boolean isHost,
                               String joinToken, LocalDateTime joinedAt, LocalDateTime lastActivityAt,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.guestName = guestName;
        this.guestPhone = guestPhone;
        this.isHost = isHost;
        this.joinToken = joinToken;
        this.joinedAt = joinedAt;
        this.lastActivityAt = lastActivityAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public String getGuestPhone() { return guestPhone; }
    public void setGuestPhone(String guestPhone) { this.guestPhone = guestPhone; }
    public Boolean getIsHost() { return isHost; }
    public void setIsHost(Boolean isHost) { this.isHost = isHost; }
    public String getJoinToken() { return joinToken; }
    public void setJoinToken(String joinToken) { this.joinToken = joinToken; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    public LocalDateTime getLastActivityAt() { return lastActivityAt; }
    public void setLastActivityAt(LocalDateTime lastActivityAt) { this.lastActivityAt = lastActivityAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
