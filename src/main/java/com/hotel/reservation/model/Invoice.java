package com.hotel.reservation.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    private String invoiceNumber;
    private String reservationId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fullName", column = @Column(name = "guest_full_name")),
            @AttributeOverride(name = "documentId", column = @Column(name = "guest_document_id")),
            @AttributeOverride(name = "email", column = @Column(name = "guest_email")),
            @AttributeOverride(name = "phone", column = @Column(name = "guest_phone"))
    })
    private Guest guest;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private long nights;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "invoice_lines", joinColumns = @JoinColumn(name = "invoice_number"))
    @OrderColumn(name = "position")
    private List<InvoiceLine> lines = new ArrayList<>();

    private BigDecimal lodgingSubtotal;
    private BigDecimal servicesSubtotal;
    private BigDecimal total;
    private LocalDateTime issuedAt;

    protected Invoice() {
    }

    public Invoice(String invoiceNumber, String reservationId, Guest guest, String roomNumber, RoomType roomType,
                   long nights, List<InvoiceLine> lines, BigDecimal lodgingSubtotal, BigDecimal servicesSubtotal,
                   BigDecimal total, LocalDateTime issuedAt) {
        this.invoiceNumber = invoiceNumber;
        this.reservationId = reservationId;
        this.guest = guest;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.nights = nights;
        this.lines = new ArrayList<>(lines);
        this.lodgingSubtotal = lodgingSubtotal;
        this.servicesSubtotal = servicesSubtotal;
        this.total = total;
        this.issuedAt = issuedAt;
    }

    public String getInvoiceNumber() { return invoiceNumber; }
    public String getReservationId() { return reservationId; }
    public Guest getGuest() { return guest; }
    public String getRoomNumber() { return roomNumber; }
    public RoomType getRoomType() { return roomType; }
    public long getNights() { return nights; }
    public List<InvoiceLine> getLines() { return lines; }
    public BigDecimal getLodgingSubtotal() { return lodgingSubtotal; }
    public BigDecimal getServicesSubtotal() { return servicesSubtotal; }
    public BigDecimal getTotal() { return total; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
}
