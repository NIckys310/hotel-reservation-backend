package com.hotel.reservation.model;

import java.math.BigDecimal;

public enum RoomType {
    SINGLE("Sencilla", new BigDecimal("100.00"), 1),
    DOUBLE("Doble", new BigDecimal("150.00"), 2),
    SUITE("Suite", new BigDecimal("300.00"), 4);

    private final String label;
    private final BigDecimal basePrice;
    private final int capacity;

    RoomType(String label, BigDecimal basePrice, int capacity) {
        this.label = label;
        this.basePrice = basePrice;
        this.capacity = capacity;
    }

    public String getLabel() { return label; }
    public BigDecimal getBasePrice() { return basePrice; }
    public int getCapacity() { return capacity; }
}
