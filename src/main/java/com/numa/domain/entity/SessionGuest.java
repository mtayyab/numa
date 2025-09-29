package com.numa.domain.entity;

import com.numa.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SessionGuest entity representing individual guests in a dining session.
 * Manages guest information and their participation in group orders.
 */
@Entity
@Table(name = "session_guests", indexes = {
    @Index(name = "idx_session_guests_session_id", columnList = "session_id"),
    @Index(name = "idx_session_guests_token", columnList = "join_token")
})
public class SessionGuest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private DiningSession session;

    @NotBlank(message = "Guest name is required")
    @Size(max = 255, message = "Guest name must not exceed 255 characters")
    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @Size(max = 20, message = "Guest phone must not exceed 20 characters")
    @Column(name = "guest_phone")
    private String guestPhone;

    @Column(name = "is_host", nullable = false)
    private Boolean isHost = false;

    @NotBlank(message = "Join token is required")
    @Size(max = 255, message = "Join token must not exceed 255 characters")
    @Column(name = "join_token", nullable = false, unique = true)
    private String joinToken;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "last_activity_at", nullable = false)
    private LocalDateTime lastActivityAt;

    // Constructors
    public SessionGuest() {
        super();
        this.joinedAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }

    public SessionGuest(DiningSession session, String guestName) {
        this();
        this.session = session;
        this.guestName = guestName;
        this.joinToken = generateJoinToken();
    }

    public SessionGuest(DiningSession session, String guestName, boolean isHost) {
        this(session, guestName);
        this.isHost = isHost;
    }

    // Business methods
    public void updateActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }

    public boolean isRecentlyActive() {
        return lastActivityAt.isAfter(LocalDateTime.now().minusMinutes(30));
    }

    public long getSessionDurationMinutes() {
        return java.time.Duration.between(joinedAt, LocalDateTime.now()).toMinutes();
    }

    public String getDisplayName() {
        return guestName + (isHost ? " (Host)" : "");
    }

    public String getInitials() {
        String[] parts = guestName.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        } else {
            return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
        }
    }

    public boolean canManageSession() {
        return isHost;
    }

    public boolean canInviteGuests() {
        return isHost;
    }

    public boolean canRequestBill() {
        return isHost;
    }

    private String generateJoinToken() {
        return UUID.randomUUID().toString();
    }

    // Getters and setters
    public DiningSession getSession() {
        return session;
    }

    public void setSession(DiningSession session) {
        this.session = session;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getGuestPhone() {
        return guestPhone;
    }

    public void setGuestPhone(String guestPhone) {
        this.guestPhone = guestPhone;
    }

    public Boolean getIsHost() {
        return isHost;
    }

    public void setIsHost(Boolean isHost) {
        this.isHost = isHost;
    }

    public String getJoinToken() {
        return joinToken;
    }

    public void setJoinToken(String joinToken) {
        this.joinToken = joinToken;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(Boolean host) {
        isHost = host;
    }
}
