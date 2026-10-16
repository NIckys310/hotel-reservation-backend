package com.hotel.reservation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationRequestDTO(
        @NotBlank(message = "El nombre del huésped es obligatorio") String guestName,
        @NotBlank(message = "El documento de identidad es obligatorio") String documentId,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no es válido") String email,
        String phone,
        @NotBlank(message = "La habitación es obligatoria") String roomNumber,
        @NotNull(message = "La fecha de llegada es obligatoria") LocalDate checkIn,
        @NotNull(message = "La fecha de salida es obligatoria") LocalDate checkOut,
        @Min(value = 1, message = "Debe haber al menos un huésped") int numberOfGuests) {
}
