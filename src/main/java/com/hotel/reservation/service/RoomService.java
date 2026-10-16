package com.hotel.reservation.service;

import com.hotel.reservation.exception.BusinessException;
import com.hotel.reservation.exception.NotFoundException;
import com.hotel.reservation.model.Room;
import com.hotel.reservation.model.RoomStatus;
import com.hotel.reservation.model.RoomType;
import com.hotel.reservation.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/** Subsystem 1: room inventory, availability by dates and type, reserving and releasing rooms. */
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public RoomService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAllByOrderByNumberAsc();
    }

    public Room findRoom(String number) {
        return roomRepository.findById(number)
                .orElseThrow(() -> new NotFoundException("No existe la habitación " + number));
    }

    public boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return reservationRepository.findAll().stream()
                .filter(reservation -> reservation.isActive())
                .filter(reservation -> reservation.getRoom().getNumber().equals(room.getNumber()))
                .noneMatch(reservation -> reservation.overlaps(checkIn, checkOut));
    }

    /** Returns the rooms of the requested type (or all types when null), whether available or not. */
    public List<Room> findRooms(RoomType type) {
        return type == null ? roomRepository.findAllByOrderByNumberAsc() : roomRepository.findByTypeOrderByNumberAsc(type);
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
        roomRepository.save(room);
    }

    public void releaseRoom(Room room) {
        room.setStatus(RoomStatus.AVAILABLE);
        roomRepository.save(room);
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
