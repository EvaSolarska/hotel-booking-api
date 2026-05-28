package com.example.hotel_booking_api.controller;

import com.example.hotel_booking_api.dto.request.HotelRequest;
import com.example.hotel_booking_api.dto.response.HotelResponse;
import com.example.hotel_booking_api.dto.response.UserResponse;
import com.example.hotel_booking_api.service.HotelManagerService;
import com.example.hotel_booking_api.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelManagerService hotelManagerService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponse> addHotel(@Valid @RequestBody HotelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.addHotel(request));
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @hotelManagerService.isHotelManager(#id, principal.username)")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelRequest request) {
        return ResponseEntity.ok(hotelService.updateHotel(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/managers/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignManager(@PathVariable Long id, @PathVariable Long userId){
        hotelManagerService.assignManager(id, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}/managers/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeManager(@PathVariable Long id, @PathVariable Long userId) {
        hotelManagerService.removeManager(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/managers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getManagers (@PathVariable Long id) {
        return ResponseEntity.ok(hotelManagerService.getManagers(id));
    }
}
