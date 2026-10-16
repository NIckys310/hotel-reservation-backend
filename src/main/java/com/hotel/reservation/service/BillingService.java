package com.hotel.reservation.service;

import com.hotel.reservation.model.AddedService;
import com.hotel.reservation.model.Invoice;
import com.hotel.reservation.model.InvoiceLine;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Subsystem 4: detailed invoice with a breakdown of lodging and extra services. */
@Service
public class BillingService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final RateService rateService;
    private final ReservationRepository reservationRepository;

    public BillingService(RateService rateService, ReservationRepository reservationRepository) {
        this.rateService = rateService;
        this.reservationRepository = reservationRepository;
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
            String concept = "Servicio adicional: " + service.getLabel();
            lines.add(new InvoiceLine(concept, service.getQuantity(), service.getUnitPrice(), service.getTotal()));
        }
        BigDecimal servicesSubtotal = reservation.getServicesCost();
        return new Invoice("FAC-" + (1001 + reservationRepository.countByInvoiceIsNotNull()), reservation.getId(), reservation.getGuest(),
                reservation.getRoom().getNumber(), reservation.getRoom().getType(), reservation.getNights(), lines,
                reservation.getLodgingCost(), servicesSubtotal, reservation.getLodgingCost().add(servicesSubtotal),
                LocalDateTime.now());
    }
}
