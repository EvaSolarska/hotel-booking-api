package com.example.hotel_booking_api.exception;

import lombok.Getter;

@Getter
public class InvalidOperationException extends RuntimeException {
    private final String field;

    public InvalidOperationException(String message) {
        super(message);
        this.field = "error";
    }

    public InvalidOperationException(String field, String message) {
        super(message);
        this.field = field;
    }
}
