package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.UserResponse;

import java.util.List;

public interface HotelManagerService {
    void assignManager(Long hotelId, Long userId);
    void removeManager(Long hotelId, Long userId);
    List<UserResponse> getManagers(Long hotelId);
    boolean isHotelManager(Long hotelId, String email);
}
