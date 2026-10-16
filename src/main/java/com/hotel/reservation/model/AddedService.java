package com.hotel.reservation.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Embeddable
public class AddedService {

    @Enumerated(EnumType.STRING)
    private ServiceType type;
    private String label;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private LocalDateTime addedAt;

    protected AddedService() {
    }

    public AddedService(ServiceType type, String label, int quantity, BigDecimal unitPrice, BigDecimal total,
                        LocalDateTime addedAt) {
        this.type = type;
        this.label = label;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
        this.addedAt = addedAt;
    }

    public ServiceType getType() { return type; }
    public String getLabel() { return label; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotal() { return total; }
    public LocalDateTime getAddedAt() { return addedAt; }
}
