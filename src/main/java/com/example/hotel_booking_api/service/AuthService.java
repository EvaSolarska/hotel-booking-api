package com.example.hotel_booking_api.service;


import com.example.hotel_booking_api.dto.AuthResponse;
import com.example.hotel_booking_api.dto.LoginRequest;
import com.example.hotel_booking_api.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
