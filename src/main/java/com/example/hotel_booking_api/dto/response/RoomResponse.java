package com.example.hotel_booking_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private Long id;
    private Long hotelId;
    private String type;
    private String roomNumber;
    private int capacity;
    private BigDecimal pricePerNight;
    private String description;
}
