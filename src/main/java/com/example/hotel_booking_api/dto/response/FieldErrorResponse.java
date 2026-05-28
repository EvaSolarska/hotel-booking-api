package com.example.hotel_booking_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldErrorResponse {
    private String field;
    private String message;
}
