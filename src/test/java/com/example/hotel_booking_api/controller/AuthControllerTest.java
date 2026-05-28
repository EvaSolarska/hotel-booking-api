package com.example.hotel_booking_api.controller;

import com.example.hotel_booking_api.config.SecurityConfig;
import com.example.hotel_booking_api.dto.response.AuthResponse;
import com.example.hotel_booking_api.dto.request.LoginRequest;
import com.example.hotel_booking_api.dto.request.RegisterRequest;
import com.example.hotel_booking_api.exception.InvalidCredentialsException;
import com.example.hotel_booking_api.security.JwtService;
import com.example.hotel_booking_api.service.AuthService;
import com.example.hotel_booking_api.security.AppUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AppUserDetailsService appUserDetailsService;

    private static final String REGISTER_URL = "/api/auth/register";
    private static final String LOGIN_URL = "/api/auth/login";

    // Register

    @Test
    void shouldReturnCreatedWhenRegisterSuccessfully() throws Exception {
        RegisterRequest request = new RegisterRequest("test@test.com", "password", "Name", "Surname", null);
        when(authService.register(any())).thenReturn(new AuthResponse("token"));

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token"));
    }


    @ParameterizedTest
    @MethodSource("invalidRegisterRequests")
    void shouldReturnBadRequestWhenRegisterRequestIsInvalid(RegisterRequest request) throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0].field").exists())
                .andExpect(jsonPath("$.errors[0].message").exists());
    }

    // Login

    @Test
    void shouldReturnOkWhenLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest("test@test.com", "password");
        when(authService.login(any())).thenReturn(new AuthResponse("token"));

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void shouldReturnBadRequestWhenLoginEmailIsBlank() throws Exception {
        LoginRequest request = new LoginRequest("", "password");
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("email"))
                .andExpect(jsonPath("$.errors[0].message").value("Email is required"));
    }

    @Test
    void shouldReturn401WhenInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");
        when(authService.login(any()))
                .thenThrow(new InvalidCredentialsException("Invalid email or password"));

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors[0].field").value("credentials"))
                .andExpect(jsonPath("$.errors[0].message").value("Invalid email or password"));
    }

    static Stream<RegisterRequest> invalidRegisterRequests() {
        return Stream.of(
                new RegisterRequest("", "password", "Name", "Surname", null),
                new RegisterRequest("email", "password", "Name", "Surname", null),
                new RegisterRequest("test@test.com", "", "Name", "Surname", null),
                new RegisterRequest("test@test.com", "password", "", "Surname", null),
                new RegisterRequest("test@test.com", "password", "Name", "", null)
        );
    }
}