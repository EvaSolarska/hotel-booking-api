package com.example.hotel_booking_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;
}
