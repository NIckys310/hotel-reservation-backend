package com.hotel.reservation.service;

import com.hotel.reservation.model.Reservation;
import com.hotel.reservation.model.ReservationStatus;
import com.hotel.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/** Subsystem 5: simulated digital keys (UUID) for the reserved room. */
@Service
public class AccessService {

    private final ReservationRepository reservationRepository;

    public AccessService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public String generateDigitalKey(Reservation reservation) {
        return UUID.randomUUID().toString();
    }

    /** A key opens its room only while the guest is checked in; check-out deactivates it. */
    public boolean isKeyValid(String key, String roomNumber) {
        return reservationRepository.findByDigitalKey(key)
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CHECKED_IN)
                .filter(reservation -> reservation.getRoom().getNumber().equals(roomNumber))
                .isPresent();
    }
}
