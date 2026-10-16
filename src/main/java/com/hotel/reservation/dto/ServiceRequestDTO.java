package com.hotel.reservation.dto;

import com.hotel.reservation.model.ServiceType;
import jakarta.validation.constraints.NotNull;

/** quantity is optional: defaults to 1 for flat services and to the number of nights for breakfast. */
public record ServiceRequestDTO(
        @NotNull(message = "El tipo de servicio es obligatorio") ServiceType serviceType,
        Integer quantity) {
}
