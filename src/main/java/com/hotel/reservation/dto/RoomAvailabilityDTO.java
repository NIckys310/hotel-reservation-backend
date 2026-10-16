package com.hotel.reservation.dto;

import com.hotel.reservation.model.RoomType;

public record RoomAvailabilityDTO(String number, RoomType type, String typeLabel, int floor, int capacity,
                                  String description, boolean available, QuoteDTO quote) {
}
