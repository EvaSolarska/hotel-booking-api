package com.example.hotel_booking_api.controller;

import com.example.hotel_booking_api.dto.request.RoomRequest;
import com.example.hotel_booking_api.dto.response.RoomResponse;
import com.example.hotel_booking_api.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @securityService.isHotelManager(#hotelId)")
    public ResponseEntity<RoomResponse> addRoom(@PathVariable Long hotelId, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.addRoom(hotelId, request));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getHotelRooms(hotelId));
    }


    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(hotelId, roomId));
    }

    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isHotelManager(#hotelId)")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long hotelId, @PathVariable Long roomId, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(hotelId, roomId, request));
    }

    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isHotelManager(#hotelId)")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
        roomService.deleteRoom(hotelId, roomId);
        return ResponseEntity.noContent().build();
    }
}
