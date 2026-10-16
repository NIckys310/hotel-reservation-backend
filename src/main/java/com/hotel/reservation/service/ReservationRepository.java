package com.hotel.reservation.service;

import com.hotel.reservation.exception.NotFoundException;
import com.hotel.reservation.model.Reservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** In-memory storage for reservations, backed by a Java collection. */
@Component
public class ReservationRepository {

    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    public Reservation save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    public Reservation findById(String id) {
        Reservation reservation = reservations.get(id == null ? null : id.trim().toUpperCase());
        if (reservation == null) {
            throw new NotFoundException("No existe una reserva con el código " + id);
        }
        return reservation;
    }

    public Collection<Reservation> findAll() {
        return reservations.values();
    }

    public List<Reservation> findAllSorted() {
        List<Reservation> list = new ArrayList<>(reservations.values());
        list.sort(Comparator.comparing(Reservation::getCreatedAt).reversed());
        return list;
    }
}
