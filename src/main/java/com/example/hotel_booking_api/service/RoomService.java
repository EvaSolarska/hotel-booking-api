package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.request.RoomRequest;
import com.example.hotel_booking_api.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {
    RoomResponse addRoom(Long hotelId, RoomRequest request);
    List<RoomResponse> getHotelRooms(Long hotelId);
    RoomResponse getRoomById(Long hotelId, Long roomId);
    RoomResponse updateRoom(Long hotelId, Long roomId, RoomRequest request);
    void deleteRoom(Long hotelId, Long roomId);
}
