package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.request.RoleRequest;
import com.example.hotel_booking_api.dto.response.UserResponse;
import com.example.hotel_booking_api.entity.Role;
import com.example.hotel_booking_api.entity.User;
import com.example.hotel_booking_api.exception.ResourceNotFoundException;
import com.example.hotel_booking_api.mapper.UserMapper;
import com.example.hotel_booking_api.repository.UserRepository;
import com.example.hotel_booking_api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("name")
                .lastName("surname")
                .role(Role.USER)
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("name")
                .lastName("surname")
                .role(Role.USER)
                .build();
    }

    @Test
    void shouldChangeRoleSuccessfully() {
        RoleRequest request = new RoleRequest(Role.ADMIN);

        UserResponse adminResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(adminResponse);

        UserResponse result = userService.changeRole(user.getId(), request);

        assertEquals(Role.ADMIN, user.getRole());
        assertEquals(Role.ADMIN, result.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowWhenUserNotFoundOnChangeRole() {
        RoleRequest request = new RoleRequest(Role.HOTEL_MANAGER);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> userService.changeRole(user.getId(), request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        List<UserResponse> result = userService.getAllUsers();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void shouldGetUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserById(user.getId());

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }


    @Test
    void shouldThrowWhenUserNotFoundOnGetById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());
        verify(userRepository).delete(user);
    }


    @Test
    void shouldThrowWhenUserNotFoundOnDelete() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(user.getId()));
        verify(userRepository, never()).delete(any());
    }
}