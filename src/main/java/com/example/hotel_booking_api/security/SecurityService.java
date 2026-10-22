package com.example.hotel_booking_api.security;

import com.example.hotel_booking_api.entity.Reservation;
import com.example.hotel_booking_api.repository.HotelManagerRepository;
import com.example.hotel_booking_api.repository.ReservationRepository;
import com.example.hotel_booking_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final CurrentUserProvider userProvider;
    private final HotelManagerRepository hotelManagerRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public boolean isHotelManager(Long hotelId) {

        if(hotelId == null) {
            return false;
        }

        String username = userProvider.getCurrentUserName();

        return userRepository.findByEmail(username)
                .map( user -> hotelManagerRepository.existsByHotelIdAndUserId(hotelId, user.getId()))
                .orElse(false);
    }

    public boolean canAccessReservation(Long reservationId) {

        if (reservationId == null) return false;

        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation == null) return false;

        String userName = userProvider.getCurrentUserName();

        if (reservation.getUser().getEmail().equalsIgnoreCase(userName)) {
            return true;
        }

        Long hotelId = reservation.getRoom().getHotel().getId();

        return isHotelManager(hotelId);
    }
}
