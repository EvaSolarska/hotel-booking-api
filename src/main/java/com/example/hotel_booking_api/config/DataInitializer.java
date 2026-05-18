package com.example.hotel_booking_api.config;

import com.example.hotel_booking_api.entity.Role;
import com.example.hotel_booking_api.entity.User;
import com.example.hotel_booking_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@admin.com}")
    private String adminEmail;

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin already exists: {}", adminEmail);
            return;
        }

        User admin = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .firstName("Admin")
                .lastName("Admin")
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        log.info("Admin created successfully: {}", adminEmail);
    }
}
