package com.hotel.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String fullName,
        @NotBlank(message = "El usuario es obligatorio") @Size(min = 3, message = "El usuario debe tener al menos 3 caracteres") String username,
        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password) {
}
