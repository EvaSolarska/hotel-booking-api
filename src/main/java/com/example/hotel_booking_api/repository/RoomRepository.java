package com.example.hotel_booking_api.repository;

import com.example.hotel_booking_api.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByIdAndHotelId(Long roomId, Long hotelId);
    List<Room> findAllByHotelId(Long hotelId);

    boolean existsByHotelIdAndRoomNumber(Long hotelId, String roomNumber);
    boolean existsByHotelIdAndRoomNumberAndIdNot(Long hotelId, String roomNumber, Long roomId);
}
