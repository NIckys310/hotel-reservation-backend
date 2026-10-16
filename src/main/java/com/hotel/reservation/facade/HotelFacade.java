package com.hotel.reservation.facade;

import com.hotel.reservation.dto.QuoteDTO;
import com.hotel.reservation.dto.ReservationRequestDTO;
import com.hotel.reservation.dto.RoomAvailabilityDTO;
import com.hotel.reservation.dto.ServiceCatalogItemDTO;
import com.hotel.reservation.exception.BusinessException;
import com.hotel.reservation.model.Guest;
import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.ReservationStatus;
import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomType;
import com.hotel.reservation.model.ServiceType;
import com.hotel.reservation.exception.NotFoundException;
import com.hotel.reservation.repository.ReservationRepository;
import com.hotel.reservation.service.AccessService;
import com.hotel.reservation.service.AdditionalServiceService;
import com.hotel.reservation.service.BillingService;
import com.hotel.reservation.service.RateService;
import com.hotel.reservation.service.RoomService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Facade pattern: the single entry point that hides the five hotel subsystems
 * (rooms, rates, additional services, billing and access) behind simple operations.
 */
@Component
public class HotelFacade {

    private final RoomService roomService;
    private final RateService rateService;
    private final AdditionalServiceService additionalServiceService;
    private final BillingService billingService;
    private final AccessService accessService;
    private final ReservationRepository reservationRepository;

    public HotelFacade(RoomService roomService, RateService rateService,
                       AdditionalServiceService additionalServiceService, BillingService billingService,
                       AccessService accessService, ReservationRepository reservationRepository) {
        this.roomService = roomService;
        this.rateService = rateService;
        this.additionalServiceService = additionalServiceService;
        this.billingService = billingService;
        this.accessService = accessService;
        this.reservationRepository = reservationRepository;
    }

    /** crearReserva(reservaDTO): checks availability, prices the stay and stores the reservation. */
    @Transactional
    public synchronized Reservation createReservation(ReservationRequestDTO request) {
        Room room = roomService.reserveRoom(request.roomNumber(), request.checkIn(), request.checkOut(),
                request.numberOfGuests());
        QuoteDTO quote = rateService.calculateQuote(room.getType(), request.checkIn(), request.checkOut());
        Guest guest = new Guest(request.guestName().trim(), request.documentId().trim(), request.email().trim(),
                request.phone() == null ? "" : request.phone().trim());
        String id = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Reservation reservation = new Reservation(id, guest, room, request.checkIn(), request.checkOut(),
                request.numberOfGuests(), quote.total());
        return reservationRepository.save(reservation);
    }

    /** agregarServicio(reservaId, tipoServicio). */
    @Transactional
    public synchronized Reservation addService(String reservationId, ServiceType serviceType, Integer quantity) {
        Reservation reservation = findReservation(reservationId);
        if (!reservation.isActive()) {
            throw new BusinessException("No se pueden agregar servicios a una reserva finalizada o cancelada");
        }
        additionalServiceService.addService(reservation, serviceType, quantity);
        return reservationRepository.save(reservation);
    }

    /** realizarCheckIn(reservaId): marks the room as occupied and issues the digital key. */
    @Transactional
    public synchronized Reservation checkIn(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BusinessException("La reserva " + reservation.getId() + " ya realizó el check-in");
        }
        roomService.occupyRoom(reservation.getRoom());
        reservation.setDigitalKey(accessService.generateDigitalKey(reservation));
        reservation.setCheckedInAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        return reservationRepository.save(reservation);
    }

    /** realizarCheckOut(reservaId): charges extra services, generates the final invoice and releases the room. */
    @Transactional
    public synchronized Reservation checkOut(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new BusinessException("Debe realizar el check-in antes del check-out");
        }
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new BusinessException("La reserva " + reservation.getId() + " ya realizó el check-out");
        }
        reservation.setInvoice(billingService.generateInvoice(reservation));
        roomService.releaseRoom(reservation.getRoom());
        reservation.setCheckedOutAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        return reservationRepository.save(reservation);
    }

    /** Cancels a reservation that has not checked in yet, releasing its dates for other guests. */
    @Transactional
    public synchronized Reservation cancelReservation(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new BusinessException("Solo se pueden cancelar reservas que aún no han hecho check-in");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(String reservationId) {
        return findReservation(reservationId);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAllByOrderByCreatedAtDesc();
    }

    private Reservation findReservation(String reservationId) {
        String id = reservationId == null ? "" : reservationId.trim().toUpperCase();
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe una reserva con el código " + reservationId));
    }

    /** Price filters apply to the average nightly price of the stay (season surcharge included). */
    public List<RoomAvailabilityDTO> checkAvailability(LocalDate checkIn, LocalDate checkOut, RoomType type,
                                                       BigDecimal minPrice, BigDecimal maxPrice) {
        roomService.validateDates(checkIn, checkOut);
        return roomService.findRooms(type).stream()
                .map(room -> new RoomAvailabilityDTO(room.getNumber(), room.getType(), room.getType().getLabel(),
                        room.getName(), room.getFloor(), room.getType().getCapacity(), room.getDescription(),
                        room.getSizeM2(), room.getBedType(), room.getView(), room.getImageUrl(), room.getAmenities(),
                        roomService.isAvailable(room, checkIn, checkOut),
                        rateService.calculateQuote(room.getType(), checkIn, checkOut)))
                .filter(room -> minPrice == null || room.quote().averageNightlyPrice().compareTo(minPrice) >= 0)
                .filter(room -> maxPrice == null || room.quote().averageNightlyPrice().compareTo(maxPrice) <= 0)
                .toList();
    }

    public QuoteDTO getQuote(RoomType type, LocalDate checkIn, LocalDate checkOut) {
        roomService.validateDates(checkIn, checkOut);
        return rateService.calculateQuote(type, checkIn, checkOut);
    }

    public List<ServiceCatalogItemDTO> getServiceCatalog() {
        return additionalServiceService.getCatalog();
    }

    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }
}
