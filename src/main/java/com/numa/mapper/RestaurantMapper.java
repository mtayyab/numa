package com.numa.mapper;

import com.numa.domain.entity.Restaurant;
import com.numa.dto.response.RestaurantResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for Restaurant entity and DTOs.
 * Handles conversion between entity and response objects.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    /**
     * Convert Restaurant entity to RestaurantResponse DTO
     */
    RestaurantResponse toResponse(Restaurant restaurant);
}
