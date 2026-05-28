package com.example.hotel_booking_api.service;


import com.example.hotel_booking_api.dto.response.AuthResponse;
import com.example.hotel_booking_api.dto.request.LoginRequest;
import com.example.hotel_booking_api.dto.request.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
