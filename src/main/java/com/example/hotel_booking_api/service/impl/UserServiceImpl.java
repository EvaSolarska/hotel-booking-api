package com.example.hotel_booking_api.service.impl;

import com.example.hotel_booking_api.dto.request.RoleRequest;
import com.example.hotel_booking_api.dto.response.UserResponse;
import com.example.hotel_booking_api.entity.User;
import com.example.hotel_booking_api.exception.InvalidOperationException;
import com.example.hotel_booking_api.exception.ResourceNotFoundException;
import com.example.hotel_booking_api.mapper.UserMapper;
import com.example.hotel_booking_api.repository.UserRepository;
import com.example.hotel_booking_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse changeRole(Long id, RoleRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        String currentUsername = auth.getName();

        User targetUser = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(targetUser.getEmail().equals(currentUsername)) {
            throw new InvalidOperationException("You cannot change your own role");
        }

        targetUser.setRole(request.getRole());
        userRepository.save(targetUser);
        return userMapper.toResponse(targetUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
