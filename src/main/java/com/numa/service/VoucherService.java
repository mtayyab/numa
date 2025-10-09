package com.numa.service;

import com.numa.dto.request.VoucherRequest;
import com.numa.dto.response.VoucherResponse;
import com.numa.domain.entity.Voucher;
import com.numa.domain.entity.Restaurant;
import com.numa.domain.entity.User;
import com.numa.domain.enums.VoucherStatus;
import com.numa.domain.enums.VoucherType;
import com.numa.repository.VoucherRepository;
import com.numa.repository.RestaurantRepository;
import com.numa.repository.UserRepository;
import com.numa.exception.ResourceNotFoundException;
import com.numa.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for voucher management operations.
 */
@Service
@Transactional
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new voucher
     */
    public VoucherResponse createVoucher(VoucherRequest request, UUID createdBy) {
        // Validate restaurant exists
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // Validate user exists
        userRepository.findById(createdBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if voucher code already exists
        if (voucherRepository.existsByCode(request.getCode())) {
            throw new ValidationException("Voucher code already exists");
        }

        // Validate voucher request
        validateVoucherRequest(request);

        // Create voucher entity
        Voucher voucher = new Voucher();
        voucher.setCode(request.getCode().toUpperCase());
        voucher.setDescription(request.getDescription());
        voucher.setType(request.getType());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinimumOrderAmount(request.getMinimumOrderAmount());
        voucher.setMaximumDiscountAmount(request.getMaximumDiscountAmount());
        voucher.setExpiresAt(request.getExpiresAt());
        voucher.setUsageLimit(request.getUsageLimit());
        voucher.setRestaurant(restaurant);
        voucher.setCreatedBy(createdBy);
        voucher.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : true);
        voucher.setValidFrom(request.getValidFrom() != null ? request.getValidFrom() : LocalDateTime.now());

        Voucher savedVoucher = voucherRepository.save(voucher);
        return mapToVoucherResponse(savedVoucher);
    }

    /**
     * Get all vouchers for a restaurant
     */
    @Transactional(readOnly = true)
    public List<VoucherResponse> getVouchersByRestaurant(UUID restaurantId) {
        List<Voucher> vouchers = voucherRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
        return vouchers.stream()
                .map(this::mapToVoucherResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get vouchers for a restaurant with pagination
     */
    @Transactional(readOnly = true)
    public Page<VoucherResponse> getVouchersByRestaurant(UUID restaurantId, Pageable pageable) {
        Page<Voucher> vouchers = voucherRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId, pageable);
        return vouchers.map(this::mapToVoucherResponse);
    }

    /**
     * Get active vouchers for a restaurant
     */
    @Transactional(readOnly = true)
    public List<VoucherResponse> getActiveVouchersByRestaurant(UUID restaurantId) {
        List<Voucher> vouchers = voucherRepository.findActiveVouchersForRestaurant(restaurantId, LocalDateTime.now());
        return vouchers.stream()
                .map(this::mapToVoucherResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get voucher by ID
     */
    @Transactional(readOnly = true)
    public VoucherResponse getVoucherById(UUID voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        return mapToVoucherResponse(voucher);
    }

    /**
     * Get voucher by code (for guest usage)
     */
    @Transactional(readOnly = true)
    public VoucherResponse getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findActiveVoucherByCode(code, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found or not active"));
        return mapToVoucherResponse(voucher);
    }

    /**
     * Update voucher
     */
    public VoucherResponse updateVoucher(UUID voucherId, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        // Check if code is being changed and if new code exists
        if (!voucher.getCode().equals(request.getCode().toUpperCase())) {
            if (voucherRepository.existsByCode(request.getCode())) {
                throw new ValidationException("Voucher code already exists");
            }
        }

        // Validate voucher request
        validateVoucherRequest(request);

        // Update voucher fields
        voucher.setCode(request.getCode().toUpperCase());
        voucher.setDescription(request.getDescription());
        voucher.setType(request.getType());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinimumOrderAmount(request.getMinimumOrderAmount());
        voucher.setMaximumDiscountAmount(request.getMaximumDiscountAmount());
        voucher.setExpiresAt(request.getExpiresAt());
        voucher.setUsageLimit(request.getUsageLimit());
        voucher.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : true);
        voucher.setValidFrom(request.getValidFrom());

        Voucher savedVoucher = voucherRepository.save(voucher);
        return mapToVoucherResponse(savedVoucher);
    }

    /**
     * Delete voucher
     */
    public void deleteVoucher(UUID voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        voucherRepository.delete(voucher);
    }

    /**
     * Toggle voucher status
     */
    public VoucherResponse toggleVoucherStatus(UUID voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        if (voucher.getStatus() == VoucherStatus.ACTIVE) {
            voucher.setStatus(VoucherStatus.INACTIVE);
        } else {
            voucher.setStatus(VoucherStatus.ACTIVE);
        }

        Voucher savedVoucher = voucherRepository.save(voucher);
        return mapToVoucherResponse(savedVoucher);
    }

    /**
     * Validate voucher for order
     */
    @Transactional(readOnly = true)
    public boolean validateVoucherForOrder(String code, BigDecimal orderAmount) {
        try {
            Voucher voucher = voucherRepository.findActiveVoucherByCode(code, LocalDateTime.now())
                    .orElse(null);
            return voucher != null && voucher.isValidForOrder(orderAmount);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calculate discount for voucher
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateVoucherDiscount(String code, BigDecimal orderAmount) {
        Voucher voucher = voucherRepository.findActiveVoucherByCode(code, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found or not active"));

        if (!voucher.isValidForOrder(orderAmount)) {
            throw new ValidationException("Voucher is not valid for this order amount");
        }

        return voucher.calculateDiscount(orderAmount);
    }

    /**
     * Use voucher (increment usage count)
     */
    @Transactional
    public void useVoucher(String code) {
        Voucher voucher = voucherRepository.findActiveVoucherByCode(code, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found or not active"));

        voucher.incrementUsage();
        voucherRepository.save(voucher);
    }

    /**
     * Generate unique voucher code
     */
    public String generateUniqueVoucherCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (voucherRepository.existsByCode(code));
        return code;
    }

    /**
     * Map voucher entity to response DTO
     */
    private VoucherResponse mapToVoucherResponse(Voucher voucher) {
        return new VoucherResponse(
                voucher.getId(),
                voucher.getCode(),
                voucher.getDescription(),
                voucher.getType(),
                voucher.getDiscountValue(),
                voucher.getMinimumOrderAmount(),
                voucher.getMaximumDiscountAmount(),
                voucher.getExpiresAt(),
                voucher.getUsageLimit(),
                voucher.getUsedCount(),
                voucher.getStatus(),
                voucher.getRestaurant().getId(),
                voucher.getRestaurant().getName(),
                voucher.getCreatedBy(),
                null, // createdByName - would need to fetch from user repository
                voucher.getIsPublic(),
                voucher.getValidFrom(),
                voucher.getCreatedAt(),
                voucher.getUpdatedAt()
        );
    }

    /**
     * Validate voucher request
     */
    private void validateVoucherRequest(VoucherRequest request) {
        if (request.getType() == VoucherType.PERCENTAGE) {
            if (request.getDiscountValue().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new ValidationException("Percentage discount cannot exceed 100%");
            }
        }

        if (request.getMinimumOrderAmount() != null && request.getMaximumDiscountAmount() != null) {
            if (request.getMinimumOrderAmount().compareTo(request.getMaximumDiscountAmount()) > 0) {
                throw new ValidationException("Minimum order amount cannot be greater than maximum discount amount");
            }
        }
    }

    /**
     * Generate random voucher code
     */
    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return code.toString();
    }
}
