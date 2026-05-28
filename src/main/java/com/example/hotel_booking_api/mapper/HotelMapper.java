package com.example.hotel_booking_api.mapper;

import com.example.hotel_booking_api.dto.request.AddressRequest;
import com.example.hotel_booking_api.dto.response.AddressResponse;
import com.example.hotel_booking_api.dto.request.HotelRequest;
import com.example.hotel_booking_api.dto.response.HotelResponse;
import com.example.hotel_booking_api.entity.Address;
import com.example.hotel_booking_api.entity.Hotel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {
    HotelResponse toResponse(Hotel hotel);
    Hotel toEntity(HotelRequest request);
    AddressResponse toAddressResponse(Address address);
    Address toAddressEntity(AddressRequest request);
}
