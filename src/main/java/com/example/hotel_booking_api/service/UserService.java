package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.request.RoleRequest;
import com.example.hotel_booking_api.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse changeRole(Long id, RoleRequest role);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);
    void deleteUser(Long id);
}