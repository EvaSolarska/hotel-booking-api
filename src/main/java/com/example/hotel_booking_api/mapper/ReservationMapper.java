package com.example.hotel_booking_api.mapper;

import com.example.hotel_booking_api.dto.request.ReservationRequest;
import com.example.hotel_booking_api.dto.response.ReservationResponse;
import com.example.hotel_booking_api.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "userId", source = "user.id")
    ReservationResponse toResponse(Reservation reservation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "user", ignore = true)
    Reservation toEntity(ReservationRequest request);
}
