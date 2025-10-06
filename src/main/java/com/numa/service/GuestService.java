package com.numa.service;

import com.numa.domain.entity.Restaurant;
import com.numa.domain.entity.RestaurantTable;
import com.numa.domain.entity.MenuCategory;
import com.numa.domain.entity.MenuItem;
import com.numa.domain.entity.DiningSession;
import com.numa.domain.entity.SessionGuest;
import com.numa.domain.entity.Order;
import com.numa.domain.enums.OrderStatus;
import com.numa.domain.enums.SessionStatus;
import com.numa.dto.request.GuestJoinSessionRequest;
import com.numa.dto.request.GuestOrderRequest;
import com.numa.dto.response.GuestMenuResponse;
import com.numa.dto.response.GuestSessionResponse;
import com.numa.dto.response.GuestOrderResponse;
import com.numa.dto.response.GuestRestaurantResponse;
import com.numa.dto.response.GuestTableResponse;
import com.numa.dto.response.GuestRestaurantDTO;
import com.numa.dto.response.GuestMenuCategoryDTO;
import com.numa.dto.response.GuestMenuItemDTO;
import com.numa.repository.RestaurantRepository;
import com.numa.repository.RestaurantTableRepository;
import com.numa.repository.MenuCategoryRepository;
import com.numa.repository.DiningSessionRepository;
import com.numa.repository.SessionGuestRepository;
import com.numa.repository.OrderRepository;
import com.numa.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for guest operations (no authentication required)
 */
@Service
@Transactional
public class GuestService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private RestaurantTableRepository tableRepository;
    
    @Autowired
    private MenuCategoryRepository menuCategoryRepository;
    
    @Autowired
    private DiningSessionRepository sessionRepository;
    
    @Autowired
    private SessionGuestRepository sessionGuestRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Get restaurant by slug
     */
    public GuestRestaurantResponse getRestaurantBySlug(String slug) {
        Restaurant restaurant = restaurantRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with slug: " + slug));
        return new GuestRestaurantResponse(restaurant);
    }

    /**
     * Get table by QR code
     */
    public GuestTableResponse getTableByQrCode(String qrCode) {
        RestaurantTable table = tableRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with QR code: " + qrCode));
        return new GuestTableResponse(table);
    }

    /**
     * Get public menu for restaurant
     */
    public GuestMenuResponse getPublicMenu(String slug) {
        Restaurant restaurant = restaurantRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with slug: " + slug));
        List<MenuCategory> categories = menuCategoryRepository.findByRestaurantIdAndIsActiveTrueOrderBySortOrderAsc(restaurant.getId());
        
        // Convert to DTOs to avoid lazy loading issues
        GuestRestaurantDTO restaurantDTO = convertToRestaurantDTO(restaurant);
        List<GuestMenuCategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToCategoryDTO)
                .collect(Collectors.toList());
        
        return new GuestMenuResponse(restaurantDTO, categoryDTOs);
    }

    /**
     * Join or create dining session
     */
    public GuestSessionResponse joinSession(GuestJoinSessionRequest request) {
        // Find or create dining session
        GuestTableResponse tableResponse = getTableByQrCode(request.getTableQrCode());
        RestaurantTable table = tableRepository.findById(tableResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));
        DiningSession session = sessionRepository.findByTableIdAndStatus(table.getId(), SessionStatus.ACTIVE)
                .orElseGet(() -> createNewSession(table));
        
        // Create guest session
        SessionGuest guest = new SessionGuest(session, request.getGuestName());
        
        sessionGuestRepository.save(guest);
        
        // Get session data
        List<SessionGuest> guests = sessionGuestRepository.findBySessionIdOrderByJoinedAtAsc(session.getId());
        List<Order> cartItems = orderRepository.findBySessionIdAndStatus(session.getId(), OrderStatus.PENDING);
        List<Order> orders = orderRepository.findBySessionIdAndStatusNot(session.getId(), OrderStatus.PENDING);
        
        return new GuestSessionResponse(
                session.getId(),
                session.getSessionCode(),
                guest.getJoinToken(),
                guest.getGuestName(),
                session,
                guests,
                cartItems,
                orders
        );
    }

    /**
     * Get session information
     */
    public GuestSessionResponse getSession(UUID sessionId) {
        DiningSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));
        
        List<SessionGuest> guests = sessionGuestRepository.findBySessionIdOrderByJoinedAtAsc(sessionId);
        List<Order> cartItems = orderRepository.findBySessionIdAndStatus(sessionId, OrderStatus.PENDING);
        List<Order> orders = orderRepository.findBySessionIdAndStatusNot(sessionId, OrderStatus.PENDING);
        
        return new GuestSessionResponse(
                session.getId(),
                session.getSessionCode(),
                null, // No guest token for general session info
                null,
                session,
                guests,
                cartItems,
                orders
        );
    }

    /**
     * Add item to cart
     */
    public GuestSessionResponse addToCart(UUID sessionId, GuestOrderRequest request) {
        DiningSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + sessionId));
        
        // Create order item
        Order order = new Order(session.getRestaurant(), session.getTable(), session.getId());
        order.setCustomerName("Guest");
        order.setSpecialInstructions(request.getSpecialInstructions());
        
        // Calculate price (simplified - in real implementation, get from menu item)
        order.setTotalAmount(BigDecimal.valueOf(10.00).multiply(BigDecimal.valueOf(request.getQuantity())));
        
        orderRepository.save(order);
        
        return getSession(sessionId);
    }

    /**
     * Remove item from cart
     */
    public GuestSessionResponse removeFromCart(UUID sessionId, UUID orderId) {
        Order order = orderRepository.findByIdAndSessionId(orderId, sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot remove submitted order");
        }
        
        orderRepository.delete(order);
        return getSession(sessionId);
    }

    /**
     * Update cart item
     */
    public GuestSessionResponse updateCartItem(UUID sessionId, UUID orderId, GuestOrderRequest request) {
        Order order = orderRepository.findByIdAndSessionId(orderId, sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot update submitted order");
        }
        
        order.setSpecialInstructions(request.getSpecialInstructions());
        order.setTotalAmount(BigDecimal.valueOf(10.00).multiply(BigDecimal.valueOf(request.getQuantity())));
        
        orderRepository.save(order);
        return getSession(sessionId);
    }

    /**
     * Submit order
     */
    public GuestOrderResponse submitOrder(UUID sessionId) {
        List<Order> cartItems = orderRepository.findBySessionIdAndStatus(sessionId, OrderStatus.PENDING);
        
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("No items in cart to submit");
        }
        
        // Update all cart items to confirmed
        cartItems.forEach(order -> {
            order.setStatus(OrderStatus.CONFIRMED);
        });
        
        orderRepository.saveAll(cartItems);
        
        // Return the first order as response (in real implementation, might create a single order)
        Order firstOrder = cartItems.get(0);
        return new GuestOrderResponse(
                firstOrder.getId(),
                sessionId,
                "Guest", // In real implementation, get from session guest
                firstOrder.getStatus(),
                firstOrder.getTotalAmount(),
                firstOrder.getCreatedAt(),
                null, // No estimated ready time in current entity
                List.of() // In real implementation, populate order items
        );
    }

    /**
     * Get session orders
     */
    public List<GuestOrderResponse> getSessionOrders(UUID sessionId) {
        List<Order> orders = orderRepository.findBySessionIdAndStatusNot(sessionId, OrderStatus.PENDING);
        
        return orders.stream()
                .map(order -> new GuestOrderResponse(
                        order.getId(),
                        sessionId,
                        "Guest", // In real implementation, get from session guest
                        order.getStatus(),
                        order.getTotalAmount(),
                        order.getCreatedAt(),
                        null, // No estimated ready time in current entity
                        List.of() // In real implementation, populate order items
                ))
                .collect(Collectors.toList());
    }

    /**
     * Leave session
     */
    public void leaveSession(UUID sessionId, String guestToken) {
        SessionGuest guest = sessionGuestRepository.findBySessionIdAndJoinToken(sessionId, guestToken)
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found in session"));
        
        guest.setLastActivityAt(LocalDateTime.now());
        sessionGuestRepository.save(guest);
    }

    /**
     * Create new dining session
     */
    private DiningSession createNewSession(RestaurantTable table) {
        DiningSession session = new DiningSession(table.getRestaurant(), table);
        return sessionRepository.save(session);
    }

    /**
     * Convert Restaurant entity to GuestRestaurantDTO
     */
    private GuestRestaurantDTO convertToRestaurantDTO(Restaurant restaurant) {
        return new GuestRestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getSlug(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getAddressLine1(),
                restaurant.getAddressLine2(),
                restaurant.getCity(),
                restaurant.getState(),
                restaurant.getPostalCode(),
                restaurant.getCountry(),
                restaurant.getCurrencyCode(),
                restaurant.getLanguageCode(),
                restaurant.getTimezone(),
                restaurant.getLogoUrl(),
                restaurant.getBannerUrl(),
                restaurant.getBrandColor(),
                restaurant.getStatus(),
                restaurant.getDeliveryEnabled(),
                restaurant.getTakeawayEnabled(),
                restaurant.getDineInEnabled(),
                restaurant.getDeliveryRadiusKm(),
                restaurant.getDeliveryFee(),
                restaurant.getMinimumOrderAmount(),
                restaurant.getTaxRate(),
                restaurant.getServiceChargeRate()
        );
    }

    /**
     * Convert MenuCategory entity to GuestMenuCategoryDTO
     */
    private GuestMenuCategoryDTO convertToCategoryDTO(MenuCategory category) {
        // Filter out inactive menu items and convert to DTOs
        List<GuestMenuItemDTO> menuItemDTOs = category.getMenuItems().stream()
                .filter(item -> item.getIsActive() && item.getIsAvailable())
                .map(this::convertToMenuItemDTO)
                .collect(Collectors.toList());

        return new GuestMenuCategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl(),
                category.getSortOrder(),
                category.getIsActive(),
                category.getAvailableFrom(),
                category.getAvailableUntil(),
                menuItemDTOs
        );
    }

    /**
     * Convert MenuItem entity to GuestMenuItemDTO
     */
    private GuestMenuItemDTO convertToMenuItemDTO(MenuItem item) {
        List<String> allergens = item.getAllergens() != null ? 
                List.of(item.getAllergens().split(",")) : List.of();
        
        return new GuestMenuItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getImageUrl(),
                item.getSortOrder(),
                item.getIsActive(),
                item.getIsAvailable(),
                item.getIsVegetarian(),
                item.getIsVegan(),
                item.getIsGlutenFree(),
                item.getIsSpicy(),
                item.getSpiceLevel(),
                allergens
        );
    }
}
