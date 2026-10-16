package com.hotel.reservation.model;

import java.math.BigDecimal;

public enum Season {
    HIGH("Alta", new BigDecimal("1.5")),
    LOW("Baja", BigDecimal.ONE);

    private final String label;
    private final BigDecimal multiplier;

    Season(String label, BigDecimal multiplier) {
        this.label = label;
        this.multiplier = multiplier;
    }

    public String getLabel() { return label; }
    public BigDecimal getMultiplier() { return multiplier; }
}
