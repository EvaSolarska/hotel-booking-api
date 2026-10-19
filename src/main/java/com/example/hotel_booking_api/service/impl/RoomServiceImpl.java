package com.example.hotel_booking_api.service.impl;

import com.example.hotel_booking_api.dto.request.RoomRequest;
import com.example.hotel_booking_api.dto.response.RoomResponse;
import com.example.hotel_booking_api.entity.Hotel;
import com.example.hotel_booking_api.entity.Room;
import com.example.hotel_booking_api.exception.DuplicateResourceException;
import com.example.hotel_booking_api.exception.ResourceNotFoundException;
import com.example.hotel_booking_api.mapper.RoomMapper;
import com.example.hotel_booking_api.repository.HotelRepository;
import com.example.hotel_booking_api.repository.RoomRepository;
import com.example.hotel_booking_api.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional
    @Override
    public RoomResponse addRoom(Long hotelId, RoomRequest request) {
        Hotel hotel = hotelRepository.findById(hotelId).
                orElseThrow(()-> new ResourceNotFoundException("Hotel not found"));

        if(roomRepository.existsByHotelIdAndRoomNumber(hotelId, request.getRoomNumber())) {
            throw new DuplicateResourceException(
                    "roomNumber",
                    "Room with number " + request.getRoomNumber() + " already exists in this hotel"
            );
        }

        Room room = roomMapper.toEntity(request);
        room.setHotel(hotel);

        Room savedRoom = roomRepository.save(room);
        return roomMapper.toResponse(savedRoom);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoomResponse> getHotelRooms(Long hotelId) {
        if(!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found");
        }

        return roomRepository.findAllByHotelId(hotelId)
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public RoomResponse getRoomById(Long hotelId, Long roomId) {
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with id: " + roomId + " for hotel : " + hotelId));

        return roomMapper.toResponse(room);
    }

    @Transactional
    @Override
    public RoomResponse updateRoom(Long hotelId, Long roomId, RoomRequest request) {
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with id: " + roomId + " for hotel: " + hotelId));

        boolean roomNumberChanged = !room.getRoomNumber().equals(request.getRoomNumber());

        if(roomNumberChanged && roomRepository.existsByHotelIdAndRoomNumberAndIdNot(hotelId, request.getRoomNumber(), roomId)) {
            throw new DuplicateResourceException(
                    "roomNumber",
                    "Room with number " + request.getRoomNumber() + " already exists in this hotel"
            );
        }

        roomMapper.updateEntityFromDto(request, room);

        Room updateRoom = roomRepository.save(room);
        return roomMapper.toResponse(updateRoom);
    }

    @Transactional
    @Override
    public void deleteRoom(Long hotelId, Long roomId) {
        Room room = roomRepository.findByIdAndHotelId(roomId, hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Room not found with id: " + roomId + " for hotel: " + hotelId));

        roomRepository.delete(room);
    }
}
