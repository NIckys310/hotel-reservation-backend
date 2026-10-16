package com.hotel.reservation.model;

import java.math.BigDecimal;

public record InvoiceLine(String concept, int quantity, BigDecimal unitPrice, BigDecimal total) {
}
