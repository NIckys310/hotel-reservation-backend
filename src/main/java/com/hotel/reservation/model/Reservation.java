package com.hotel.reservation.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

    private final String id;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final int numberOfGuests;
    private final BigDecimal lodgingCost;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final List<AddedService> services = new ArrayList<>();
    private ReservationStatus status = ReservationStatus.CONFIRMED;
    private String digitalKey;
    private LocalDateTime checkedInAt;
    private LocalDateTime checkedOutAt;
    private Invoice invoice;

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

    public long getNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public boolean overlaps(LocalDate from, LocalDate to) {
        return checkInDate.isBefore(to) && from.isBefore(checkOutDate);
    }

    public boolean isActive() {
        return status != ReservationStatus.CHECKED_OUT;
    }

    public BigDecimal getServicesCost() {
        return services.stream().map(AddedService::total).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

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
    public Invoice getInvoice() { return invoice; }
    public void setInvoice(Invoice invoice) { this.invoice = invoice; }
}
