package com.example.hotel_booking_api.dto.response;

import com.example.hotel_booking_api.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalPrice;
    private ReservationStatus status;
    private int numberOfGuests;
    private LocalDateTime createdAt;
    private Long roomId;
    private Long userId;
}
