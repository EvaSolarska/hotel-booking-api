package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.request.ReservationRequest;
import com.example.hotel_booking_api.dto.response.ReservationResponse;
import com.example.hotel_booking_api.entity.*;
import com.example.hotel_booking_api.exception.DuplicateResourceException;
import com.example.hotel_booking_api.exception.InvalidOperationException;
import com.example.hotel_booking_api.exception.ResourceNotFoundException;
import com.example.hotel_booking_api.mapper.ReservationMapper;
import com.example.hotel_booking_api.repository.HotelRepository;
import com.example.hotel_booking_api.repository.ReservationRepository;
import com.example.hotel_booking_api.repository.RoomRepository;
import com.example.hotel_booking_api.repository.UserRepository;
import com.example.hotel_booking_api.security.CurrentUserProvider;
import com.example.hotel_booking_api.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest{

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;


    private User user;
    private Hotel hotel;
    private Room room;

    @BeforeEach
    void setUp() {
         user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("FirstName")
                .lastName("LastName")
                .role(Role.USER)
                .build();

        hotel = Hotel.builder()
                .id(1L)
                .name("Hotel")
                .email("hotel@test.com")
                .address(Address.builder()
                        .id(1L)
                        .city("City")
                        .country("Country")
                        .street("Street")
                        .postalCode("11-111")
                        .latitude(1.0)
                        .longitude(1.0)
                        .build()
                ).build();

         room = Room.builder()
                .id(1L)
                .roomNumber("1")
                .type("Test")
                .capacity(2)
                .pricePerNight(new BigDecimal("100.0"))
                .hotel(hotel)
                .build();
    }


    @Nested
    @DisplayName("createReservation")
    class CreateReservationTests {
        @Test
        void shouldCreateReservationSuccessfully () {
            LocalDate checkIn = LocalDate.now().plusDays(1);
            LocalDate checkOut = LocalDate.now().plusDays(3);
            ReservationRequest request = new ReservationRequest(room.getId(), checkIn, checkOut, 2);
            ReservationResponse expectedResponse = ReservationResponse.builder()
                    .id(1L)
                    .checkIn(request.getCheckIn())
                    .checkOut(request.getCheckOut())
                    .totalPrice(new BigDecimal("200.0"))
                    .status(ReservationStatus.PENDING)
                    .numberOfGuests(2)
                    .roomId(room.getId())
                    .userId(user.getId())
                    .build();

            when(roomRepository.findById(request.getRoomId())).thenReturn(Optional.of(room));
            when(reservationRepository.isRoomOccupied(any(),any(), any())).thenReturn(false);
            when(currentUserProvider.getCurrentUserName()).thenReturn(user.getEmail());
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(reservationMapper.toEntity(request)).thenReturn(new Reservation());
            when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(expectedResponse);

            ReservationResponse result = reservationService.createReservation(request);

            assertThat(result).isNotNull();
            assertThat(result.getRoomId()).isEqualTo(room.getId());
            assertThat(result.getId()).isEqualTo(expectedResponse.getId());
            assertThat(result.getUserId()).isEqualTo(user.getId());
            assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            assertThat(result.getCheckIn()).isEqualTo(request.getCheckIn());
            assertThat(result.getCheckOut()).isEqualTo(request.getCheckOut());
            assertThat(result.getTotalPrice()).isEqualByComparingTo(new BigDecimal("200.00"));

            verify(reservationRepository).save(any(Reservation.class));
        }


        @Test
        void shouldThrowInvalidOperationExceptionWhenCheckOutIsNotAfterCheckIn() {
            LocalDate checkIn = LocalDate.now().plusDays(3);
            LocalDate checkOut = LocalDate.now().plusDays(1);
            ReservationRequest request = new ReservationRequest(room.getId(), checkIn, checkOut, 2);

            assertThatThrownBy(() -> reservationService.createReservation(request))
                    .isInstanceOf(InvalidOperationException.class)
                    .hasMessageContaining("Check-out date must be after check-in date");

            verifyNoInteractions(reservationRepository, roomRepository, userRepository);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenRoomDoesNotExist() {
            LocalDate checkIn = LocalDate.now().plusDays(1);
            LocalDate checkOut = LocalDate.now().plusDays(3);
            ReservationRequest request = new ReservationRequest(room.getId(), checkIn, checkOut, 2);

            when(roomRepository.findById(request.getRoomId())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> reservationService.createReservation(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Room not found");

            verify(reservationRepository, never()).save(any());
        }

        @Test
        void shouldThrowInvalidOperationExceptionWhenNumberOfGuestsExceedCapacity() {
            LocalDate checkIn = LocalDate.now().plusDays(1);
            LocalDate checkOut = LocalDate.now().plusDays(3);
            ReservationRequest request = new ReservationRequest(room.getId(), checkIn, checkOut, 4);

            when(roomRepository.findById(request.getRoomId())).thenReturn(Optional.of(room));

            assertThatThrownBy(() -> reservationService.createReservation(request))
                    .isInstanceOf(InvalidOperationException.class)
                    .hasMessageContaining("Number of guests exceeds room capacity");

            verify(reservationRepository, never()).save(any());
        }

        @Test
        void shouldThrowDuplicateResourceExceptionWhenRoomIsOccupied(){
            LocalDate checkIn = LocalDate.now().plusDays(1);
            LocalDate checkOut = LocalDate.now().plusDays(3);
            ReservationRequest request = new ReservationRequest(room.getId(), checkIn, checkOut, 2);

            when(roomRepository.findById(request.getRoomId())).thenReturn(Optional.of(room));
            when(reservationRepository.isRoomOccupied(any(),any(), any())).thenReturn(true);

            assertThatThrownBy(() -> reservationService.createReservation(request))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("Room is already booked for the selected dates");

            verify(reservationRepository, never()).save(any());
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
            LocalDate checkIn = LocalDate.now().plusDays(1);
            LocalDate checkOut = LocalDate.now().plusDays(3);
            ReservationRequest request = new ReservationRequest(room.getId(), checkIn, checkOut, 2);

            when(roomRepository.findById(request.getRoomId())).thenReturn(Optional.of(room));
            when(reservationRepository.isRoomOccupied(any(),any(), any())).thenReturn(false);
            when(currentUserProvider.getCurrentUserName()).thenReturn(user.getEmail());
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> reservationService.createReservation(request))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found");

            verify(reservationRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("cancelReservation")
    class CancelReservationTests {
        @Test
        void shouldCancelReservationSuccessfully() {
            Reservation reservation = Reservation.builder()
                    .id(1L)
                    .status(ReservationStatus.CONFIRMED)
                    .build();

            when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

            reservationService.cancelReservation(reservation.getId());

            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELLED);

            verify(reservationRepository).save(reservation);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenReservationDoesNotExist() {
            Long reservationId = 1L;

            when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

            assertThatThrownBy(()-> reservationService.cancelReservation(reservationId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Reservation not found");

            verify(reservationRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("getAllReservationsForHotel")
    class GetAllReservationsForHotelTests {

        @Test
        void shouldReturnAllReservationsForHotel() {
            Reservation reservation = new Reservation();
            ReservationResponse expectedResponse = new ReservationResponse();

            when(hotelRepository.existsById(hotel.getId())).thenReturn(true);
            when(reservationRepository.findAllByRoom_Hotel_Id(hotel.getId())).thenReturn(List.of(reservation));
            when(reservationMapper.toResponse(reservation)).thenReturn(expectedResponse);

            List<ReservationResponse> result = reservationService.getAllReservationsForHotel(hotel.getId());

            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(expectedResponse);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenHotelDoesNotExist() {

            when(hotelRepository.existsById(hotel.getId())).thenReturn(false);

            assertThatThrownBy(() -> reservationService.getAllReservationsForHotel(hotel.getId()))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Hotel not found");

            verify(reservationRepository, never()).findAllByRoom_Hotel_Id(any());

        }
    }

    @Nested
    @DisplayName("getReservationById")
    class GetReservationByIdTests {
        @Test
        void shouldReturnReservationById() {
            Long reservationId = 1L;
            Reservation reservation = new Reservation();
            ReservationResponse expectedResponse = new ReservationResponse();

            when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
            when(reservationMapper.toResponse(reservation)).thenReturn(expectedResponse);

            ReservationResponse result = reservationService.getReservationById(reservationId);

            assertThat(result).isEqualTo(expectedResponse);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenReservationDoesNotExist() {
            Long reservationId = 1L;

            when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> reservationService.getReservationById(reservationId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Reservation not found");

            verify(reservationMapper, never()).toResponse(any());
        }
    }

    @Nested
    @DisplayName("getMyReservations")
    class GetMyReservationsTests {

        @Test
        void shouldReturnCurrentUserReservations() {
            Reservation reservation = new Reservation();
            ReservationResponse expectedResponse = new ReservationResponse();

            when(currentUserProvider.getCurrentUserName()).thenReturn(user.getEmail());
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(reservationRepository.findAllByUserId(user.getId())).thenReturn(List.of(reservation));
            when(reservationMapper.toResponse(reservation)).thenReturn(expectedResponse);

            List<ReservationResponse> result = reservationService.getMyReservations();

            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(expectedResponse);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
            when(currentUserProvider.getCurrentUserName()).thenReturn(user.getEmail());
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

            assertThatThrownBy(()->reservationService.getMyReservations())
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User not found");

            verify(reservationRepository, never()).findAllByUserId(any());
        }
    }
}
