package com.hotel.reservation.service;

import com.hotel.reservation.exception.BusinessException;
import com.hotel.reservation.exception.NotFoundException;
import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomStatus;
import com.hotel.reservation.model.RoomType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Subsystem 1: room inventory, availability by dates and type, reserving and releasing rooms. */
@Service
public class RoomService {

    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private final ReservationRepository reservationRepository;

    public RoomService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        loadPredefinedRooms();
    }

    private void loadPredefinedRooms() {
        addRoom("101", RoomType.SINGLE, 1, "Habitación sencilla con vista al jardín");
        addRoom("102", RoomType.SINGLE, 1, "Habitación sencilla con escritorio de trabajo");
        addRoom("103", RoomType.SINGLE, 1, "Habitación sencilla con acceso a la terraza");
        addRoom("104", RoomType.DOUBLE, 1, "Habitación doble con dos camas");
        addRoom("105", RoomType.DOUBLE, 1, "Habitación doble con cama queen");
        addRoom("201", RoomType.SINGLE, 2, "Habitación sencilla con vista a la ciudad");
        addRoom("202", RoomType.SINGLE, 2, "Habitación sencilla silenciosa");
        addRoom("203", RoomType.DOUBLE, 2, "Habitación doble con balcón");
        addRoom("204", RoomType.DOUBLE, 2, "Habitación doble con vista a la piscina");
        addRoom("205", RoomType.DOUBLE, 2, "Habitación doble familiar");
        addRoom("301", RoomType.SINGLE, 3, "Habitación sencilla ejecutiva");
        addRoom("302", RoomType.DOUBLE, 3, "Habitación doble ejecutiva");
        addRoom("303", RoomType.DOUBLE, 3, "Habitación doble con jacuzzi");
        addRoom("401", RoomType.SUITE, 4, "Suite junior con sala de estar");
        addRoom("402", RoomType.SUITE, 4, "Suite con terraza privada");
        addRoom("501", RoomType.SUITE, 5, "Suite presidencial con vista panorámica");
    }

    private void addRoom(String number, RoomType type, int floor, String description) {
        rooms.put(number, new Room(number, type, floor, description));
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    public Room findRoom(String number) {
        Room room = rooms.get(number);
        if (room == null) {
            throw new NotFoundException("No existe la habitación " + number);
        }
        return room;
    }

    public boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.isActive())
                .filter(reservation -> reservation.getRoom().getNumber().equals(room.getNumber()))
                .noneMatch(reservation -> reservation.overlaps(checkIn, checkOut));
    }

    /** Returns the rooms of the requested type (or all types when null), whether available or not. */
    public List<Room> findRooms(RoomType type) {
        return rooms.values().stream()
                .filter(room -> type == null || room.getType() == type)
                .toList();
    }

    /** Validates that the room can be booked for the given dates. Must be called inside a lock. */
    public Room reserveRoom(String number, LocalDate checkIn, LocalDate checkOut, int numberOfGuests) {
        validateDates(checkIn, checkOut);
        Room room = findRoom(number);
        if (numberOfGuests > room.getType().getCapacity()) {
            throw new BusinessException("La habitación " + number + " admite máximo "
                    + room.getType().getCapacity() + " huésped(es)");
        }
        if (!isAvailable(room, checkIn, checkOut)) {
            throw new BusinessException("La habitación " + number + " no está disponible en esas fechas");
        }
        return room;
    }

    public void occupyRoom(Room room) {
        room.setStatus(RoomStatus.OCCUPIED);
    }

    public void releaseRoom(Room room) {
        room.setStatus(RoomStatus.AVAILABLE);
    }

    public void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new BusinessException("Debe indicar la fecha de llegada y la fecha de salida");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new BusinessException("La fecha de salida debe ser posterior a la fecha de llegada");
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha de llegada no puede estar en el pasado");
        }
    }
}
