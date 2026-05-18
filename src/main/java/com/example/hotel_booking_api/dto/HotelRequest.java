package com.example.hotel_booking_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelRequest {

    @NotBlank(message = "Hotel name is required")
    private String name;

    private String description;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Valid
    @NotNull(message = "Address is required")
    private AddressRequest address;
}
