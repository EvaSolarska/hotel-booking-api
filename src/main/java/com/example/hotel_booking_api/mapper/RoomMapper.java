package com.example.hotel_booking_api.mapper;

import com.example.hotel_booking_api.dto.request.RoomRequest;
import com.example.hotel_booking_api.dto.response.RoomResponse;
import com.example.hotel_booking_api.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "hotel.id", target = "hotelId")
    RoomResponse toResponse(Room room);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    Room toEntity(RoomRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    void updateEntityFromDto(RoomRequest request, @MappingTarget Room room);
}
