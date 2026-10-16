package com.hotel.reservation.service;

import com.hotel.reservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Subsystem 5: simulated digital keys (UUID) for the reserved room. */
@Service
public class AccessService {

    /** Active keys mapped to the room number they open. */
    private final Map<String, String> activeKeys = new ConcurrentHashMap<>();

    public String generateDigitalKey(Reservation reservation) {
        String key = UUID.randomUUID().toString();
        activeKeys.put(key, reservation.getRoom().getNumber());
        return key;
    }

    public void revokeDigitalKey(String key) {
        if (key != null) {
            activeKeys.remove(key);
        }
    }

    public boolean isKeyValid(String key, String roomNumber) {
        return roomNumber.equals(activeKeys.get(key));
    }
}
