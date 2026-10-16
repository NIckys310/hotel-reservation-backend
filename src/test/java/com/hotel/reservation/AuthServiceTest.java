package com.hotel.reservation;

import com.hotel.reservation.dto.LoginRequestDTO;
import com.hotel.reservation.dto.RegisterRequestDTO;
import com.hotel.reservation.exception.UnauthorizedException;
import com.hotel.reservation.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    void demoUserCanLogIn() {
        assertEquals("admin", authService.login(new LoginRequestDTO("admin", "hotel123")).username());
    }

    @Test
    void wrongPasswordIsRejected() {
        assertThrows(UnauthorizedException.class, () -> authService.login(new LoginRequestDTO("admin", "nope")));
    }

    @Test
    void registeredUserCanLogIn() {
        authService.register(new RegisterRequestDTO("Ana Pérez", "ana", "secreto1"));
        assertEquals("Ana Pérez", authService.login(new LoginRequestDTO("ANA", "secreto1")).fullName());
    }
}
