package com.example.hotel_booking_api.mapper;

import com.example.hotel_booking_api.dto.response.UserResponse;
import com.example.hotel_booking_api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
