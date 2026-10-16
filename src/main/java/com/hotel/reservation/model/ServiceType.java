package com.hotel.reservation.model;

import java.math.BigDecimal;

/** Extra services. Breakfast is charged per day of stay; the rest are flat fees. */
public enum ServiceType {
    SPA("Spa", new BigDecimal("50.00"), false),
    BREAKFAST("Desayuno", new BigDecimal("15.00"), true),
    TRANSFER("Traslado", new BigDecimal("30.00"), false);

    private final String label;
    private final BigDecimal price;
    private final boolean chargedPerDay;

    ServiceType(String label, BigDecimal price, boolean chargedPerDay) {
        this.label = label;
        this.price = price;
        this.chargedPerDay = chargedPerDay;
    }

    public String getLabel() { return label; }
    public BigDecimal getPrice() { return price; }
    public boolean isChargedPerDay() { return chargedPerDay; }
}
