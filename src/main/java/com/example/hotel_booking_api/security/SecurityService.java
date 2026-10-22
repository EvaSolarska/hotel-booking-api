package com.example.hotel_booking_api.security;

import com.example.hotel_booking_api.repository.HotelManagerRepository;
import com.example.hotel_booking_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final CurrentUserProvider userProvider;
    private final HotelManagerRepository hotelManagerRepository;
    private final UserRepository userRepository;

    public boolean isHotelManager(Long hotelId) {

        if(hotelId == null) {
            return false;
        }

        String username = userProvider.getCurrentUserName();

        return userRepository.findByEmail(username)
                .map( user -> hotelManagerRepository.existsByHotelIdAndUserId(hotelId, user.getId()))
                .orElse(false);
    }
}
