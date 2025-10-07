package com.numa.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for session analytics data.
 * Contains comprehensive analytics information for restaurant sessions.
 */
public class SessionAnalyticsResponse {
    
    // Basic metrics
    private Long totalSessions;
    private Long activeSessions;
    private Long completedSessions;
    private BigDecimal totalRevenue;
    private BigDecimal averageOrderValue;
    private Integer totalGuests;
    private Double averageGuestsPerSession;
    private Integer averageSessionDurationMinutes;
    
    // Time-based analytics
    private List<HourlyStats> peakHours;
    private List<DailyStats> dailyStats;
    
    // Date range
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    public SessionAnalyticsResponse() {}
    
    public SessionAnalyticsResponse(Long totalSessions, Long activeSessions, Long completedSessions,
                                  BigDecimal totalRevenue, BigDecimal averageOrderValue, Integer totalGuests,
                                  Double averageGuestsPerSession, Integer averageSessionDurationMinutes,
                                  List<HourlyStats> peakHours, List<DailyStats> dailyStats,
                                  LocalDateTime startDate, LocalDateTime endDate) {
        this.totalSessions = totalSessions;
        this.activeSessions = activeSessions;
        this.completedSessions = completedSessions;
        this.totalRevenue = totalRevenue;
        this.averageOrderValue = averageOrderValue;
        this.totalGuests = totalGuests;
        this.averageGuestsPerSession = averageGuestsPerSession;
        this.averageSessionDurationMinutes = averageSessionDurationMinutes;
        this.peakHours = peakHours;
        this.dailyStats = dailyStats;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    // Inner classes for nested data
    public static class HourlyStats {
        private Integer hour;
        private Long sessionCount;
        private Double averageGuestCount;
        
        public HourlyStats() {}
        
        public HourlyStats(Integer hour, Long sessionCount, Double averageGuestCount) {
            this.hour = hour;
            this.sessionCount = sessionCount;
            this.averageGuestCount = averageGuestCount;
        }
        
        // Getters and setters
        public Integer getHour() { return hour; }
        public void setHour(Integer hour) { this.hour = hour; }
        public Long getSessionCount() { return sessionCount; }
        public void setSessionCount(Long sessionCount) { this.sessionCount = sessionCount; }
        public Double getAverageGuestCount() { return averageGuestCount; }
        public void setAverageGuestCount(Double averageGuestCount) { this.averageGuestCount = averageGuestCount; }
    }
    
    public static class DailyStats {
        private String date;
        private Long sessions;
        private BigDecimal revenue;
        private Integer guests;
        private Double averageOrderValue;
        
        public DailyStats() {}
        
        public DailyStats(String date, Long sessions, BigDecimal revenue, Integer guests, Double averageOrderValue) {
            this.date = date;
            this.sessions = sessions;
            this.revenue = revenue;
            this.guests = guests;
            this.averageOrderValue = averageOrderValue;
        }
        
        // Getters and setters
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public Long getSessions() { return sessions; }
        public void setSessions(Long sessions) { this.sessions = sessions; }
        public BigDecimal getRevenue() { return revenue; }
        public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }
        public Integer getGuests() { return guests; }
        public void setGuests(Integer guests) { this.guests = guests; }
        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    }
    
    // Getters and setters
    public Long getTotalSessions() { return totalSessions; }
    public void setTotalSessions(Long totalSessions) { this.totalSessions = totalSessions; }
    
    public Long getActiveSessions() { return activeSessions; }
    public void setActiveSessions(Long activeSessions) { this.activeSessions = activeSessions; }
    
    public Long getCompletedSessions() { return completedSessions; }
    public void setCompletedSessions(Long completedSessions) { this.completedSessions = completedSessions; }
    
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public BigDecimal getAverageOrderValue() { return averageOrderValue; }
    public void setAverageOrderValue(BigDecimal averageOrderValue) { this.averageOrderValue = averageOrderValue; }
    
    public Integer getTotalGuests() { return totalGuests; }
    public void setTotalGuests(Integer totalGuests) { this.totalGuests = totalGuests; }
    
    public Double getAverageGuestsPerSession() { return averageGuestsPerSession; }
    public void setAverageGuestsPerSession(Double averageGuestsPerSession) { this.averageGuestsPerSession = averageGuestsPerSession; }
    
    public Integer getAverageSessionDurationMinutes() { return averageSessionDurationMinutes; }
    public void setAverageSessionDurationMinutes(Integer averageSessionDurationMinutes) { this.averageSessionDurationMinutes = averageSessionDurationMinutes; }
    
    public List<HourlyStats> getPeakHours() { return peakHours; }
    public void setPeakHours(List<HourlyStats> peakHours) { this.peakHours = peakHours; }
    
    public List<DailyStats> getDailyStats() { return dailyStats; }
    public void setDailyStats(List<DailyStats> dailyStats) { this.dailyStats = dailyStats; }
    
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
}
