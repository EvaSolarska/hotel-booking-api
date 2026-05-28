package com.example.hotel_booking_api.repository;

import com.example.hotel_booking_api.entity.HotelManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelManagerRepository extends JpaRepository<HotelManager, Long> {
    List<HotelManager> findByHotelId(Long hotelId);
    boolean existsByHotelIdAndUserId(Long hotelId, Long userId);
    void deleteByHotelIdAndUserId(Long hotelId, Long userId);
}
