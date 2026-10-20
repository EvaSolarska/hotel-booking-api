package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.request.ReservationRequest;
import com.example.hotel_booking_api.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request);
    void cancelReservation(Long id);

    List<ReservationResponse> getAllReservations();
    ReservationResponse getReservationById(Long id);
    List<ReservationResponse> getMyReservations();
}
