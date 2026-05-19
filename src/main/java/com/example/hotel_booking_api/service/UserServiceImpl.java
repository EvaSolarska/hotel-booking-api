package com.example.hotel_booking_api.service;

import com.example.hotel_booking_api.dto.RoleRequest;
import com.example.hotel_booking_api.dto.UserResponse;
import com.example.hotel_booking_api.entity.User;
import com.example.hotel_booking_api.exception.ResourceNotFoundException;
import com.example.hotel_booking_api.mapper.UserMapper;
import com.example.hotel_booking_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
        User user = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
        user.setRole(request.getRole());
        userRepository.save(user);
        return userMapper.toResponse(user);
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
