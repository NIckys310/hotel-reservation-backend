package com.hotel.reservation.controller;

import com.hotel.reservation.dto.QuoteDTO;
import com.hotel.reservation.dto.ReservationRequestDTO;
import com.hotel.reservation.dto.RoomAvailabilityDTO;
import com.hotel.reservation.dto.ServiceCatalogItemDTO;
import com.hotel.reservation.dto.ServiceRequestDTO;
import com.hotel.reservation.facade.HotelFacade;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomType;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** REST layer. It only talks to the HotelFacade, never to the subsystems directly. */
@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelFacade hotelFacade;

    public HotelController(HotelFacade hotelFacade) {
        this.hotelFacade = hotelFacade;
    }

    @PostMapping("/reservar")
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation createReservation(@Valid @RequestBody ReservationRequestDTO request) {
        return hotelFacade.createReservation(request);
    }

    @GetMapping("/disponibilidad")
    public List<RoomAvailabilityDTO> checkAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,
            @RequestParam(required = false) RoomType type,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return hotelFacade.checkAvailability(checkIn, checkOut, type, minPrice, maxPrice);
    }

    @PostMapping("/servicios/{reservationId}")
    public Reservation addService(@PathVariable String reservationId,
                                  @Valid @RequestBody ServiceRequestDTO request) {
        return hotelFacade.addService(reservationId, request.serviceType(), request.quantity());
    }

    @PutMapping("/checkin/{reservationId}")
    public Reservation checkIn(@PathVariable String reservationId) {
        return hotelFacade.checkIn(reservationId);
    }

    @PutMapping("/checkout/{reservationId}")
    public Reservation checkOut(@PathVariable String reservationId) {
        return hotelFacade.checkOut(reservationId);
    }

    @GetMapping("/reserva/{reservationId}")
    public Reservation getReservation(@PathVariable String reservationId) {
        return hotelFacade.getReservation(reservationId);
    }

    // ---- Supporting endpoints used by the frontend ----

    @GetMapping("/tarifa")
    public QuoteDTO getQuote(@RequestParam RoomType type,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return hotelFacade.getQuote(type, checkIn, checkOut);
    }

    @GetMapping("/servicios")
    public List<ServiceCatalogItemDTO> getServiceCatalog() {
        return hotelFacade.getServiceCatalog();
    }

    @GetMapping("/habitaciones")
    public List<Room> getRooms() {
        return hotelFacade.getAllRooms();
    }

    @GetMapping("/reservas")
    public List<Reservation> getReservations() {
        return hotelFacade.getAllReservations();
    }
}
