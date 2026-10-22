package com.example.hotel_booking_api.controller;

import com.example.hotel_booking_api.dto.request.ReservationRequest;
import com.example.hotel_booking_api.dto.response.ReservationResponse;
import com.example.hotel_booking_api.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(request));
    }

    @PreAuthorize("hasRole('ADMIN') or @securityService.canAccessReservation(#id)")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getMyReservations() {
        return ResponseEntity.ok(reservationService.getMyReservations());
    }

    @GetMapping("/hotel/{hotelId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isHotelManager(#hotelId)")
    public ResponseEntity<List<ReservationResponse>> getAllReservationForHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(reservationService.getAllReservationsForHotel(hotelId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.canAccessReservation(#id)")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
}
