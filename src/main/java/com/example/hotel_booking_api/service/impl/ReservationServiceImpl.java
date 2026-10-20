package com.example.hotel_booking_api.service.impl;

import com.example.hotel_booking_api.dto.request.ReservationRequest;
import com.example.hotel_booking_api.dto.response.ReservationResponse;
import com.example.hotel_booking_api.entity.Reservation;
import com.example.hotel_booking_api.entity.ReservationStatus;
import com.example.hotel_booking_api.entity.Room;
import com.example.hotel_booking_api.entity.User;
import com.example.hotel_booking_api.exception.DuplicateResourceException;
import com.example.hotel_booking_api.exception.InvalidOperationException;
import com.example.hotel_booking_api.exception.ResourceNotFoundException;
import com.example.hotel_booking_api.mapper.ReservationMapper;
import com.example.hotel_booking_api.repository.ReservationRepository;
import com.example.hotel_booking_api.repository.RoomRepository;
import com.example.hotel_booking_api.repository.UserRepository;
import com.example.hotel_booking_api.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;

    @Transactional
    @Override
    public ReservationResponse createReservation(ReservationRequest request) {

        if(!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new InvalidOperationException(
                    "check-out",
                    "Check-out date must be after check-in date"
            );
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(()-> new ResourceNotFoundException("Room not found"));

        if(request.getNumberOfGuests() > room.getCapacity()) {
            throw new InvalidOperationException(
                    "numberOfGuests",
                    "Number of guests exceeds room capacity"
            );
        }

        if(reservationRepository.isRoomOccupied(request.getCheckIn(), request.getCheckOut(), request.getRoomId(), ReservationStatus.CANCELLED)) {
            throw new DuplicateResourceException(
                    "dates",
                    "Room is already booked for the selected dates"
            );
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        BigDecimal totalPrice = calculateTotalPrice(room.getPricePerNight(), request.getCheckIn(), request.getCheckOut());

        Reservation reservation = reservationMapper.toEntity(request);
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setTotalPrice(totalPrice);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toResponse(savedReservation);
    }

    private BigDecimal calculateTotalPrice(BigDecimal pricePerNight, LocalDate checkIn, LocalDate checkOut) {
        long daysDiff = ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricePerNight.multiply(BigDecimal.valueOf(daysDiff));
    }

    @Override
    public void cancelReservation(Long id) {

    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return List.of();
    }

    @Override
    public ReservationResponse getReservationById(Long id) {
        return null;
    }


    @Override
    public List<ReservationResponse> getMyReservations() {
        return null;
    }
}
