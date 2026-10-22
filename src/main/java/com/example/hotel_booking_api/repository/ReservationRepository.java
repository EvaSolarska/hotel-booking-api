package com.example.hotel_booking_api.repository;

import com.example.hotel_booking_api.entity.Reservation;
import com.example.hotel_booking_api.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT COUNT(r) > 0
        FROM Reservation r
        WHERE r.room.id = :roomId AND
              r.checkIn<:checkOut AND 
              r.checkOut>:checkIn AND 
              r.status != :cancelledStatus
        """)
    boolean isRoomOccupied(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut, @Param("roomId") Long roomId, @Param("cancelledStatus") ReservationStatus cancelledStatus);
    List<Reservation> findAllByRoom_Hotel_Id(Long hotelId);
    List<Reservation> findAllByUserId(Long userId);
}
