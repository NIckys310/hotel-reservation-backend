package com.hotel.reservation.dto;

import com.hotel.reservation.model.ServiceType;

import java.math.BigDecimal;

public record ServiceCatalogItemDTO(ServiceType type, String label, BigDecimal price, boolean chargedPerDay) {
}
