package com.hotel.reservation.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class InvoiceLine {

    private String concept;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;

    protected InvoiceLine() {
    }

    public InvoiceLine(String concept, int quantity, BigDecimal unitPrice, BigDecimal total) {
        this.concept = concept;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = total;
    }

    public String getConcept() { return concept; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getTotal() { return total; }
}
