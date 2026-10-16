package com.hotel.reservation.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Invoice(String invoiceNumber, String reservationId, Guest guest, String roomNumber,
                      RoomType roomType, long nights, List<InvoiceLine> lines,
                      BigDecimal lodgingSubtotal, BigDecimal servicesSubtotal, BigDecimal total,
                      LocalDateTime issuedAt) {
}
