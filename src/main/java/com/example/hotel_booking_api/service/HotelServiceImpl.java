package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.HotelRequest;
import com.example.hotel_booking_api.dto.HotelResponse;
import com.example.hotel_booking_api.entity.Hotel;
import com.example.hotel_booking_api.exception.ResourceNotFoundException;
import com.example.hotel_booking_api.mapper.HotelMapper;
import com.example.hotel_booking_api.repository.HotelRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public HotelResponse addHotel(HotelRequest request) {
        Hotel hotel = hotelMapper.toEntity(request);
        hotelRepository.save(hotel);
        return hotelMapper.toResponse(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found"));

        return hotelMapper.toResponse(hotel);
    }

    @Override
    public HotelResponse updateHotel(Long id, HotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found"));
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setEmail(request.getEmail());
        hotel.setPhoneNumber(request.getPhoneNumber());
        hotel.setAddress(hotelMapper.toAddressEntity(request.getAddress()));
        hotelRepository.save(hotel);

        return hotelMapper.toResponse(hotel);
    }

    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found"));
        hotelRepository.delete(hotel);
    }
}
