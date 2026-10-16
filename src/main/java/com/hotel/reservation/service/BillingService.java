package com.hotel.reservation.service;

import com.hotel.reservation.model.AddedService;
import com.hotel.reservation.model.Invoice;
import com.hotel.reservation.model.InvoiceLine;
import com.hotel.reservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/** Subsystem 4: detailed invoice with a breakdown of lodging and extra services. */
@Service
public class BillingService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final AtomicInteger sequence = new AtomicInteger(1000);
    private final RateService rateService;

    public BillingService(RateService rateService) {
        this.rateService = rateService;
    }

    public Invoice generateInvoice(Reservation reservation) {
        List<InvoiceLine> lines = new ArrayList<>();
        for (LocalDate night = reservation.getCheckInDate(); night.isBefore(reservation.getCheckOutDate());
             night = night.plusDays(1)) {
            BigDecimal rate = rateService.getNightlyRate(reservation.getRoom().getType(), night);
            String concept = "Noche " + night.format(DATE_FORMAT) + " - Habitación "
                    + reservation.getRoom().getType().getLabel() + " " + reservation.getRoom().getNumber()
                    + " (temporada " + rateService.getSeason(night).getLabel().toLowerCase() + ")";
            lines.add(new InvoiceLine(concept, 1, rate, rate));
        }
        for (AddedService service : reservation.getServices()) {
            String concept = "Servicio adicional: " + service.label();
            lines.add(new InvoiceLine(concept, service.quantity(), service.unitPrice(), service.total()));
        }
        BigDecimal servicesSubtotal = reservation.getServicesCost();
        return new Invoice("FAC-" + sequence.incrementAndGet(), reservation.getId(), reservation.getGuest(),
                reservation.getRoom().getNumber(), reservation.getRoom().getType(), reservation.getNights(), lines,
                reservation.getLodgingCost(), servicesSubtotal, reservation.getLodgingCost().add(servicesSubtotal),
                LocalDateTime.now());
    }
}
