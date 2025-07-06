package com.oteller.hotelservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue
    private Long id;
    private String roomNumber;
    private int capacity;
    private BigDecimal pricePerNight;
    private boolean available;
    private boolean reserved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id") // foreign key
    private Hotel hotel;
}
