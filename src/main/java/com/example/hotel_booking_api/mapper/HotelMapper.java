package com.example.hotel_booking_api.mapper;

import com.example.hotel_booking_api.dto.AddressRequest;
import com.example.hotel_booking_api.dto.AddressResponse;
import com.example.hotel_booking_api.dto.HotelRequest;
import com.example.hotel_booking_api.dto.HotelResponse;
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
