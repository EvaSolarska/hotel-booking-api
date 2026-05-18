package com.example.hotel_booking_api.repository;

import com.example.hotel_booking_api.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
