package com.numa.service;

import com.numa.dto.response.ActiveSessionResponse;
import com.numa.dto.response.SessionHistoryResponse;
import com.numa.dto.response.SessionAnalyticsResponse;
import com.numa.domain.entity.DiningSession;
import com.numa.domain.entity.SessionGuest;
import com.numa.domain.entity.Order;
import com.numa.domain.entity.OrderItem;
import com.numa.domain.entity.RestaurantTable;
import com.numa.domain.enums.SessionStatus;
import com.numa.domain.enums.OrderStatus;
import com.numa.repository.DiningSessionRepository;
import com.numa.repository.SessionGuestRepository;
import com.numa.repository.OrderRepository;
import com.numa.repository.OrderItemRepository;
import com.numa.repository.RestaurantTableRepository;
import com.numa.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for session management operations
 */
@Service
@Transactional
public class SessionService {

    @Autowired
    private DiningSessionRepository sessionRepository;
    
    @Autowired
    private SessionGuestRepository sessionGuestRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private RestaurantTableRepository tableRepository;

    /**
     * Get active sessions for a restaurant
     */
    @Transactional(readOnly = true)
    public List<ActiveSessionResponse> getActiveSessions(UUID restaurantId) {
        List<DiningSession> sessions = sessionRepository.findActiveSessionsByRestaurant(restaurantId);
        return sessions.stream()
                .map(this::convertToActiveSessionResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get session history for a restaurant
     */
    @Transactional(readOnly = true)
    public Page<SessionHistoryResponse> getSessionHistory(UUID restaurantId, Pageable pageable) {
        Page<DiningSession> sessions = sessionRepository.findByRestaurantIdOrderByStartedAtDesc(restaurantId, pageable);
        return sessions.map(this::convertToSessionHistoryResponse);
    }

    /**
     * End a session
     */
    public ActiveSessionResponse endSession(UUID sessionId) {
        DiningSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));
        
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new IllegalStateException("Session is not active and cannot be ended");
        }
        
        // End the session
        session.complete();
        sessionRepository.save(session);
        
        // Make table available
        RestaurantTable table = session.getTable();
        table.makeAvailable();
        tableRepository.save(table);
        
        return convertToActiveSessionResponse(session);
    }

    /**
     * Get session details with orders
     */
    @Transactional(readOnly = true)
    public ActiveSessionResponse getSessionDetails(UUID sessionId) {
        DiningSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));
        
        return convertToActiveSessionResponse(session);
    }

    /**
     * Convert DiningSession to ActiveSessionResponse
     */
    private ActiveSessionResponse convertToActiveSessionResponse(DiningSession session) {
        // Get session guests
        List<SessionGuest> guests = sessionGuestRepository.findBySessionIdOrderByJoinedAtAsc(session.getId());
        List<ActiveSessionResponse.SessionGuestInfo> guestInfos = guests.stream()
                .map(this::convertToSessionGuestInfo)
                .collect(Collectors.toList());
        
        // Get orders
        List<Order> orders = orderRepository.findBySessionIdAndStatusNot(session.getId(), OrderStatus.PENDING);
        List<ActiveSessionResponse.OrderSummary> orderSummaries = orders.stream()
                .map(this::convertToOrderSummary)
                .collect(Collectors.toList());
        
        // Get cart items (pending orders)
        List<Order> cartItems = orderRepository.findBySessionIdAndStatus(session.getId(), OrderStatus.PENDING);
        List<ActiveSessionResponse.OrderSummary> cartItemSummaries = cartItems.stream()
                .map(this::convertToOrderSummary)
                .collect(Collectors.toList());
        
        // Calculate last activity time
        LocalDateTime lastActivityAt = guests.stream()
                .map(SessionGuest::getLastActivityAt)
                .max(LocalDateTime::compareTo)
                .orElse(session.getStartedAt());
        
        return new ActiveSessionResponse(
                session.getId(),
                session.getSessionCode(),
                session.getStatus(),
                session.getTable().getTableNumber(),
                session.getTable().getLocationDescription(),
                session.getGuestCount(),
                session.getHostName(),
                session.getHostPhone(),
                session.getSpecialRequests(),
                session.getTotalAmount(),
                session.getTipAmount(),
                session.getPaymentStatus(),
                session.getWaiterCalled(),
                session.getWaiterCallTime(),
                session.getWaiterResponseTime(),
                session.getStartedAt(),
                lastActivityAt,
                guestInfos,
                orderSummaries,
                cartItemSummaries,
                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }

    /**
     * Convert SessionGuest to SessionGuestInfo
     */
    private ActiveSessionResponse.SessionGuestInfo convertToSessionGuestInfo(SessionGuest guest) {
        return new ActiveSessionResponse.SessionGuestInfo(
                guest.getId(),
                guest.getGuestName(),
                guest.getGuestPhone(),
                guest.getIsHost(),
                guest.getJoinedAt(),
                guest.getLastActivityAt()
        );
    }

    /**
     * Convert Order to OrderSummary
     */
    private ActiveSessionResponse.OrderSummary convertToOrderSummary(Order order) {
        List<OrderItem> items = orderItemRepository.findByOrderIdOrderByCreatedAtAsc(order.getId());
        List<ActiveSessionResponse.OrderItemSummary> itemSummaries = items.stream()
                .map(this::convertToOrderItemSummary)
                .collect(Collectors.toList());
        
        return new ActiveSessionResponse.OrderSummary(
                order.getId(),
                order.getCustomerName(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                itemSummaries
        );
    }

    /**
     * Convert OrderItem to OrderItemSummary
     */
    private ActiveSessionResponse.OrderItemSummary convertToOrderItemSummary(OrderItem item) {
        return new ActiveSessionResponse.OrderItemSummary(
                item.getId(),
                item.getMenuItem().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getSpecialInstructions()
        );
    }

    /**
     * Convert DiningSession to SessionHistoryResponse
     */
    private SessionHistoryResponse convertToSessionHistoryResponse(DiningSession session) {
        // Calculate duration
        Long durationMinutes = null;
        if (session.getEndedAt() != null) {
            durationMinutes = Duration.between(session.getStartedAt(), session.getEndedAt()).toMinutes();
        }
        
        // Get order statistics
        List<Order> allOrders = orderRepository.findBySessionIdAndStatusNot(session.getId(), OrderStatus.PENDING);
        Integer totalOrders = allOrders.size();
        BigDecimal averageOrderValue = totalOrders > 0 ? 
                session.getTotalAmount().divide(BigDecimal.valueOf(totalOrders), 2, java.math.RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;
        
        return new SessionHistoryResponse(
                session.getId(),
                session.getSessionCode(),
                session.getStatus(),
                session.getTable().getTableNumber(),
                session.getTable().getLocationDescription(),
                session.getGuestCount(),
                session.getHostName(),
                session.getHostPhone(),
                session.getTotalAmount(),
                session.getTipAmount(),
                session.getPaymentStatus(),
                session.getStartedAt(),
                session.getEndedAt(),
                durationMinutes,
                totalOrders,
                averageOrderValue,
                session.getCreatedAt(),
                session.getUpdatedAt()
        );
    }

    /**
     * Get session analytics for a restaurant
     */
    @Transactional(readOnly = true)
    public SessionAnalyticsResponse getSessionAnalytics(UUID restaurantId, String timeRange) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = calculateStartDate(timeRange, endDate);
        
        // Get basic metrics
        Long totalSessions = sessionRepository.countByRestaurantIdAndStartedAtBetween(restaurantId, startDate, endDate);
        Long activeSessions = sessionRepository.countByRestaurantIdAndStatus(restaurantId, SessionStatus.ACTIVE);
        Long completedSessions = sessionRepository.countByRestaurantIdAndStatus(restaurantId, SessionStatus.COMPLETED);
        
        // Get revenue and guest metrics
        List<Object[]> revenueStats = sessionRepository.getRevenueStats(restaurantId, startDate, endDate);
        BigDecimal totalRevenue = revenueStats.isEmpty() ? BigDecimal.ZERO : (BigDecimal) revenueStats.get(0)[0];
        BigDecimal averageOrderValue = revenueStats.isEmpty() ? BigDecimal.ZERO : (BigDecimal) revenueStats.get(0)[1];
        
        List<Object[]> guestStats = sessionRepository.getGuestStats(restaurantId, startDate, endDate);
        Integer totalGuests = guestStats.isEmpty() ? 0 : ((Number) guestStats.get(0)[0]).intValue();
        Double averageGuestsPerSession = guestStats.isEmpty() ? 0.0 : ((Number) guestStats.get(0)[1]).doubleValue();
        
        // Get average session duration
        List<Object[]> durationStats = sessionRepository.getDurationStats(restaurantId, startDate, endDate);
        Integer averageSessionDurationMinutes = durationStats.isEmpty() ? 0 : ((Number) durationStats.get(0)[0]).intValue();
        
        // Get hourly stats
        List<Object[]> hourlyData = sessionRepository.getHourlySessionStats(restaurantId, startDate, endDate);
        List<SessionAnalyticsResponse.HourlyStats> peakHours = hourlyData.stream()
                .map(row -> new SessionAnalyticsResponse.HourlyStats(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).longValue(),
                        ((Number) row[2]).doubleValue()
                ))
                .collect(Collectors.toList());
        
        // Get daily stats
        List<Object[]> dailyData = sessionRepository.getDailySessionStats(restaurantId, startDate, endDate);
        List<SessionAnalyticsResponse.DailyStats> dailyStats = dailyData.stream()
                .map(row -> {
                    String date = ((java.sql.Date) row[0]).toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                    Long sessions = ((Number) row[1]).longValue();
                    BigDecimal revenue = (BigDecimal) row[3];
                    Integer guests = ((Number) row[2]).intValue();
                    Double avgOrderValue = sessions > 0 ? revenue.divide(BigDecimal.valueOf(sessions), 2, java.math.RoundingMode.HALF_UP).doubleValue() : 0.0;
                    
                    return new SessionAnalyticsResponse.DailyStats(date, sessions, revenue, guests, avgOrderValue);
                })
                .collect(Collectors.toList());
        
        return new SessionAnalyticsResponse(
                totalSessions, activeSessions, completedSessions,
                totalRevenue, averageOrderValue, totalGuests,
                averageGuestsPerSession, averageSessionDurationMinutes,
                peakHours, dailyStats, startDate, endDate
        );
    }
    
    /**
     * Calculate start date based on time range
     */
    private LocalDateTime calculateStartDate(String timeRange, LocalDateTime endDate) {
        return switch (timeRange) {
            case "7d" -> endDate.minusDays(7);
            case "30d" -> endDate.minusDays(30);
            case "90d" -> endDate.minusDays(90);
            default -> endDate.minusDays(30); // Default to 30 days
        };
    }
}
