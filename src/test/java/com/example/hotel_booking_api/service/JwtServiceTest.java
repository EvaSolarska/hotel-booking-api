package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private static final String TEST_SECRET_KEY = "dGVzdFNlY3JldEtleVRoYXRJc0xvbmdFbm91Z2hGb3JUZXN0aW5n";
    private static final long TEST_EXPIRATION = 86400000L;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService,"secretKey", TEST_SECRET_KEY);
        ReflectionTestUtils.setField(jwtService,"expiration", TEST_EXPIRATION);
    }

    @Test
    void shouldGenerateToken() {
        setupUserDetails();

        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractEmailFromToken() {
        setupUserDetails();

        String token = jwtService.generateToken(userDetails);
        assertEquals("test@test.com", jwtService.extractEmail(token));
    }


    @Test
    void shouldReturnTrueWhenTokenIsValid() {
        setupUserDetails();

        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token,userDetails));
    }

    @Test
    void shouldReturnFalseWhenTokenIsExpired() {
        setupUserDetails();

        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);
        String token = jwtService.generateToken(userDetails);
        assertFalse(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldReturnFalseWhenTokenIsInvalid() {
        boolean result = jwtService.isTokenValid(
                "eyJhbGciOiJIUzI1NiJ9.wrongpayload.wrongsignature",
                userDetails
        );
        assertFalse(result);
    }

    private void setupUserDetails() {
        when(userDetails.getUsername()).thenReturn("test@test.com");
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .when(userDetails).getAuthorities();
    }
}
