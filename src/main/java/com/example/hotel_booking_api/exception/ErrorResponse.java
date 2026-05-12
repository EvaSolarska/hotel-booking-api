package com.example.hotel_booking_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private List<FieldErrorResponse> errors;
}
