package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.UserResponse;
import com.example.hotel_booking_api.entity.Hotel;
import com.example.hotel_booking_api.entity.HotelManager;
import com.example.hotel_booking_api.entity.Role;
import com.example.hotel_booking_api.entity.User;
import com.example.hotel_booking_api.exception.ResourceNotFoundException;
import com.example.hotel_booking_api.mapper.UserMapper;
import com.example.hotel_booking_api.repository.HotelManagerRepository;
import com.example.hotel_booking_api.repository.HotelRepository;
import com.example.hotel_booking_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("hotelManagerService")
@RequiredArgsConstructor
@Transactional
public class HotelManagerServiceImpl implements HotelManagerService {

    private final HotelManagerRepository hotelManagerRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void assignManager(Long hotelId, Long userId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(()-> new ResourceNotFoundException("Hotel not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.HOTEL_MANAGER) {
            throw new IllegalStateException("User must have MANAGER role");
        }

        if (hotelManagerRepository.existsByHotelIdAndUserId(hotelId, userId)) {
            throw new IllegalStateException("User is already a manager of this hotel");
        }

        HotelManager hotelManager = HotelManager.builder()
                .hotel(hotel)
                .user(user)
                .assignedAt(LocalDateTime.now())
                .build();

        hotelManagerRepository.save(hotelManager);
    }

    @Override
    public void removeManager(Long hotelId, Long userId) {

        if(!hotelManagerRepository.existsByHotelIdAndUserId(hotelId, userId)) {
            throw new ResourceNotFoundException("User is not a manager of this hotel");
        }

        hotelManagerRepository.deleteByHotelIdAndUserId(hotelId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getManagers(Long hotelId) {

        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found");
        }

        return hotelManagerRepository.findByHotelId(hotelId)
                .stream()
                .map(hotelManager -> userMapper.toResponse(hotelManager.getUser()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isHotelManager(Long hotelId, String email) {
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException("Hotel not found");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return hotelManagerRepository.existsByHotelIdAndUserId(hotelId, user.getId());
    }
}
