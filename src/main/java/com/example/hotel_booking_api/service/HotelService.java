package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.request.HotelRequest;
import com.example.hotel_booking_api.dto.response.HotelResponse;

import java.util.List;

public interface HotelService {
    HotelResponse addHotel(HotelRequest request);
    List<HotelResponse> getAllHotels();
    HotelResponse getHotelById(Long id);
    HotelResponse updateHotel(Long id, HotelRequest request);
    void deleteHotel(Long id);
}
