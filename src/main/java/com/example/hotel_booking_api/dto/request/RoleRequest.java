package com.example.hotel_booking_api.dto.request;

import com.example.hotel_booking_api.entity.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    @NotNull(message = "Role is required")
    private Role role;
}

