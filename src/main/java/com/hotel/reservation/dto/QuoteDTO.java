package com.hotel.reservation.dto;

import com.hotel.reservation.model.RoomType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record QuoteDTO(RoomType roomType, String roomTypeLabel, LocalDate checkIn, LocalDate checkOut,
                       long nights, long highSeasonNights, long lowSeasonNights,
                       BigDecimal basePrice, BigDecimal highSeasonPrice, BigDecimal averageNightlyPrice,
                       BigDecimal total) {
}
