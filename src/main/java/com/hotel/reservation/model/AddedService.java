package com.hotel.reservation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddedService(ServiceType type, String label, int quantity, BigDecimal unitPrice,
                           BigDecimal total, LocalDateTime addedAt) {
}
