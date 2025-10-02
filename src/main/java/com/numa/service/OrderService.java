package com.numa.service;

import com.numa.domain.entity.Order;
import com.numa.domain.enums.OrderStatus;
import com.numa.dto.response.OrderResponse;
import com.numa.exception.ResourceNotFoundException;
import com.numa.repository.OrderRepository;
import com.numa.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for order management operations.
 * Handles order retrieval, status updates, and cancellation.
 */
@Service
@Transactional(readOnly = true)
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Get all orders for a restaurant with optional filtering
     */
    public List<OrderResponse> getOrders(UUID restaurantId, String status, int page, int size) {
        // Verify restaurant exists
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Pageable pageable = PageRequest.of(page, size);
        List<Order> orders;

        if (status != null && !status.isEmpty()) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                orders = orderRepository.findByRestaurantIdAndStatusOrderByCreatedAtDesc(restaurantId, orderStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid order status: " + status);
            }
        } else {
            Page<Order> orderPage = orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId, pageable);
            orders = orderPage.getContent();
        }

        return orders.stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get order by ID
     */
    public OrderResponse getOrder(UUID restaurantId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Verify order belongs to restaurant
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Order not found");
        }

        return mapToOrderResponse(order);
    }

    /**
     * Update order status
     */
    @Transactional
    public OrderResponse updateOrderStatus(UUID restaurantId, UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Verify order belongs to restaurant
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Order not found");
        }

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            Order savedOrder = orderRepository.save(order);
            return mapToOrderResponse(savedOrder);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    /**
     * Cancel order
     */
    @Transactional
    public OrderResponse cancelOrder(UUID restaurantId, UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Verify order belongs to restaurant
        if (!order.getRestaurant().getId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Order not found");
        }

        // Check if order can be cancelled
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Order cannot be cancelled in current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    /**
     * Map Order entity to response DTO
     */
    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus().toString());
        response.setOrderType(order.getOrderType().toString());
        response.setTotal(order.getTotalAmount());
        response.setTableNumber(order.getTable() != null ? order.getTable().getTableNumber() : null);
        response.setCustomerName(order.getCustomerName());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setSpecialInstructions(order.getSpecialInstructions());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        // Map order items
        if (order.getOrderItems() != null) {
            response.setItems(order.getOrderItems().stream()
                    .map(item -> {
                        OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
                        itemResponse.setId(item.getId());
                        itemResponse.setMenuItemId(item.getMenuItem().getId());
                        itemResponse.setName(item.getMenuItem().getName());
                        itemResponse.setQuantity(item.getQuantity());
                        itemResponse.setPrice(item.getUnitPrice());
                        itemResponse.setSpecialInstructions(item.getSpecialInstructions());
                        return itemResponse;
                    })
                    .collect(Collectors.toList()));
        }

        return response;
    }
}
