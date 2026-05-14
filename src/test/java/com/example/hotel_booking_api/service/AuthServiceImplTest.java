package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.AuthResponse;
import com.example.hotel_booking_api.dto.LoginRequest;
import com.example.hotel_booking_api.dto.RegisterRequest;
import com.example.hotel_booking_api.entity.User;
import com.example.hotel_booking_api.exception.InvalidCredentialsException;
import com.example.hotel_booking_api.exception.UserAlreadyExistsException;
import com.example.hotel_booking_api.repository.UserRepository;
import com.example.hotel_booking_api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest("test@test.com","password","Name", "Surname",null);

        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashPassword");
        when(jwtService.generateToken(any())).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("token", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("test@test.com","password","Name", "Surname",null);

        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.register(request)
        );

        assertEquals("User with this email already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }


    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("test@test.com", "password");
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("token");

        AuthResponse response = authService.login(request);
        assertNotNull(response);
        assertEquals("token", response.getToken());
    }


    @Test
    void shouldThrowWhenInvalidCredentials() {
        LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad credentials"));

        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        assertEquals("Invalid email or password", exception.getMessage());
    }
}
