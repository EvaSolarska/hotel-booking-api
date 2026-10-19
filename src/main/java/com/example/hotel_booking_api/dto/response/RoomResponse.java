package com.example.hotel_booking_api.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
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
