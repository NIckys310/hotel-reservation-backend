package com.hotel.reservation.service;

import com.hotel.reservation.dto.AuthResponseDTO;
import com.hotel.reservation.dto.LoginRequestDTO;
import com.hotel.reservation.dto.RegisterRequestDTO;
import com.hotel.reservation.exception.BusinessException;
import com.hotel.reservation.exception.UnauthorizedException;
import com.hotel.reservation.model.AppUser;
import com.hotel.reservation.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/** Simple login: users and BCrypt password hashes are stored in the database. */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        AppUser user = userRepository.findByUsernameIgnoreCase(request.username().trim())
                .filter(found -> passwordEncoder.matches(request.password(), found.getPasswordHash()))
                .orElseThrow(() -> new UnauthorizedException("Usuario o contraseña incorrectos"));
        return toResponse(user);
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        String username = request.username().trim().toLowerCase();
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new BusinessException("El usuario " + username + " ya existe");
        }
        AppUser user = userRepository.save(
                new AppUser(username, request.fullName().trim(), passwordEncoder.encode(request.password())));
        return toResponse(user);
    }

    public void createUserIfMissing(String username, String fullName, String password) {
        if (!userRepository.existsByUsernameIgnoreCase(username)) {
            userRepository.save(new AppUser(username, fullName, passwordEncoder.encode(password)));
        }
    }

    private AuthResponseDTO toResponse(AppUser user) {
        return new AuthResponseDTO(user.getId(), user.getUsername(), user.getFullName());
    }
}
