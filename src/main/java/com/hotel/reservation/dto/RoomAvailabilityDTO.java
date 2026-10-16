package com.hotel.reservation.dto;

import com.hotel.reservation.model.RoomType;

import java.util.List;

public record RoomAvailabilityDTO(String number, RoomType type, String typeLabel, String name, int floor,
                                  int capacity, String description, int sizeM2, String bedType, String view,
                                  String imageUrl, List<String> amenities, boolean available, QuoteDTO quote) {
}
