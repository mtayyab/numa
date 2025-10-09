package com.numa.controller;

import com.numa.dto.request.VoucherRequest;
import com.numa.dto.response.VoucherResponse;
import com.numa.service.VoucherService;
import com.numa.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Controller for voucher management operations.
 */
@RestController
@RequestMapping("/vouchers")
@Tag(name = "Voucher Management", description = "APIs for managing restaurant vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    /**
     * Create a new voucher
     */
    @Operation(summary = "Create Voucher", description = "Create a new discount voucher for the restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voucher created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid voucher data"),
            @ApiResponse(responseCode = "409", description = "Voucher code already exists")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<VoucherResponse> createVoucher(@Valid @RequestBody VoucherRequest request) {
        UUID createdBy = getCurrentUserId();
        VoucherResponse voucher = voucherService.createVoucher(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(voucher);
    }

    /**
     * Get all vouchers for a restaurant
     */
    @Operation(summary = "Get Restaurant Vouchers", description = "Get all vouchers for a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vouchers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<VoucherResponse>> getVouchersByRestaurant(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId) {
        List<VoucherResponse> vouchers = voucherService.getVouchersByRestaurant(restaurantId);
        return ResponseEntity.ok(vouchers);
    }

    /**
     * Get vouchers for a restaurant with pagination
     */
    @Operation(summary = "Get Restaurant Vouchers (Paginated)", description = "Get vouchers for a restaurant with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vouchers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurant/{restaurantId}/paginated")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Page<VoucherResponse>> getVouchersByRestaurantPaginated(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId,
            Pageable pageable) {
        Page<VoucherResponse> vouchers = voucherService.getVouchersByRestaurant(restaurantId, pageable);
        return ResponseEntity.ok(vouchers);
    }

    /**
     * Get active vouchers for a restaurant
     */
    @Operation(summary = "Get Active Vouchers", description = "Get all active vouchers for a restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active vouchers retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    @GetMapping("/restaurant/{restaurantId}/active")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<VoucherResponse>> getActiveVouchersByRestaurant(
            @Parameter(description = "Restaurant ID") @PathVariable UUID restaurantId) {
        List<VoucherResponse> vouchers = voucherService.getActiveVouchersByRestaurant(restaurantId);
        return ResponseEntity.ok(vouchers);
    }

    /**
     * Get voucher by ID
     */
    @Operation(summary = "Get Voucher", description = "Get voucher details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voucher retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Voucher not found")
    })
    @GetMapping("/{voucherId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<VoucherResponse> getVoucherById(
            @Parameter(description = "Voucher ID") @PathVariable UUID voucherId) {
        VoucherResponse voucher = voucherService.getVoucherById(voucherId);
        return ResponseEntity.ok(voucher);
    }

    /**
     * Get voucher by code (for guest usage)
     */
    @Operation(summary = "Get Voucher by Code", description = "Get voucher details by code (public endpoint)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voucher retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Voucher not found or not active")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<VoucherResponse> getVoucherByCode(
            @Parameter(description = "Voucher code") @PathVariable String code) {
        VoucherResponse voucher = voucherService.getVoucherByCode(code);
        return ResponseEntity.ok(voucher);
    }

    /**
     * Update voucher
     */
    @Operation(summary = "Update Voucher", description = "Update voucher details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voucher updated successfully"),
            @ApiResponse(responseCode = "404", description = "Voucher not found"),
            @ApiResponse(responseCode = "400", description = "Invalid voucher data"),
            @ApiResponse(responseCode = "409", description = "Voucher code already exists")
    })
    @PutMapping("/{voucherId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<VoucherResponse> updateVoucher(
            @Parameter(description = "Voucher ID") @PathVariable UUID voucherId,
            @Valid @RequestBody VoucherRequest request) {
        VoucherResponse voucher = voucherService.updateVoucher(voucherId, request);
        return ResponseEntity.ok(voucher);
    }

    /**
     * Delete voucher
     */
    @Operation(summary = "Delete Voucher", description = "Delete a voucher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Voucher deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Voucher not found")
    })
    @DeleteMapping("/{voucherId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Void> deleteVoucher(
            @Parameter(description = "Voucher ID") @PathVariable UUID voucherId) {
        voucherService.deleteVoucher(voucherId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Toggle voucher status
     */
    @Operation(summary = "Toggle Voucher Status", description = "Toggle voucher active/inactive status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voucher status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Voucher not found")
    })
    @PatchMapping("/{voucherId}/toggle-status")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<VoucherResponse> toggleVoucherStatus(
            @Parameter(description = "Voucher ID") @PathVariable UUID voucherId) {
        VoucherResponse voucher = voucherService.toggleVoucherStatus(voucherId);
        return ResponseEntity.ok(voucher);
    }

    /**
     * Validate voucher for order
     */
    @Operation(summary = "Validate Voucher", description = "Validate if a voucher can be used for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voucher validation result"),
            @ApiResponse(responseCode = "404", description = "Voucher not found")
    })
    @PostMapping("/validate")
    public ResponseEntity<Object> validateVoucher(
            @Parameter(description = "Voucher code") @RequestParam String code,
            @Parameter(description = "Order amount") @RequestParam BigDecimal orderAmount) {
        boolean isValid = voucherService.validateVoucherForOrder(code, orderAmount);
        return ResponseEntity.ok(java.util.Map.of("valid", isValid));
    }

    /**
     * Calculate voucher discount
     */
    @Operation(summary = "Calculate Voucher Discount", description = "Calculate discount amount for a voucher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Voucher not found"),
            @ApiResponse(responseCode = "400", description = "Voucher not valid for order amount")
    })
    @PostMapping("/calculate-discount")
    public ResponseEntity<Object> calculateDiscount(
            @Parameter(description = "Voucher code") @RequestParam String code,
            @Parameter(description = "Order amount") @RequestParam BigDecimal orderAmount) {
        BigDecimal discount = voucherService.calculateVoucherDiscount(code, orderAmount);
        return ResponseEntity.ok(java.util.Map.of("discount", discount));
    }

    /**
     * Generate unique voucher code
     */
    @Operation(summary = "Generate Voucher Code", description = "Generate a unique voucher code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voucher code generated successfully")
    })
    @GetMapping("/generate-code")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Object> generateVoucherCode() {
        String code = voucherService.generateUniqueVoucherCode();
        return ResponseEntity.ok(java.util.Map.of("code", code));
    }

    /**
     * Get current user ID from security context
     */
    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        throw new RuntimeException("User not authenticated");
    }
}
