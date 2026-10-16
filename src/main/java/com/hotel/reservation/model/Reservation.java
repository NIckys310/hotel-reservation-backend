package com.hotel.reservation.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    private String id;

    @Embedded
    private Guest guest;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "room_number")
    private Room room;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private BigDecimal lodgingCost;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "reservation_services", joinColumns = @JoinColumn(name = "reservation_id"))
    @OrderColumn(name = "position")
    private List<AddedService> services = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.CONFIRMED;

    private String digitalKey;
    private LocalDateTime checkedInAt;
    private LocalDateTime checkedOutAt;
    private LocalDateTime cancelledAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_number")
    private Invoice invoice;

    protected Reservation() {
    }

    public Reservation(String id, Guest guest, Room room, LocalDate checkInDate, LocalDate checkOutDate,
                       int numberOfGuests, BigDecimal lodgingCost) {
        this.id = id;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.lodgingCost = lodgingCost;
    }

    @Transient
    public long getNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public boolean overlaps(LocalDate from, LocalDate to) {
        return checkInDate.isBefore(to) && from.isBefore(checkOutDate);
    }

    @Transient
    public boolean isActive() {
        return status != ReservationStatus.CHECKED_OUT && status != ReservationStatus.CANCELLED;
    }

    @Transient
    public BigDecimal getServicesCost() {
        return services.stream().map(AddedService::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public BigDecimal getCurrentTotal() {
        return lodgingCost.add(getServicesCost());
    }

    public String getId() { return id; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public BigDecimal getLodgingCost() { return lodgingCost; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<AddedService> getServices() { return services; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    public String getDigitalKey() { return digitalKey; }
    public void setDigitalKey(String digitalKey) { this.digitalKey = digitalKey; }
    public LocalDateTime getCheckedInAt() { return checkedInAt; }
    public void setCheckedInAt(LocalDateTime checkedInAt) { this.checkedInAt = checkedInAt; }
    public LocalDateTime getCheckedOutAt() { return checkedOutAt; }
    public void setCheckedOutAt(LocalDateTime checkedOutAt) { this.checkedOutAt = checkedOutAt; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
}
