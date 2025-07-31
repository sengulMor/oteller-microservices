package com.oteller.hotelservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms", uniqueConstraints = {@UniqueConstraint(columnNames = {"room_number", "hotel_id"})})
public class Room extends BaseEntity {

    @NotBlank(message = "Room Number can not be blank")
    @Column(name = "room_number", nullable = false, length = 4)
    private String roomNumber;

    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(name = "capacity", nullable = false)
    private int capacity;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(name = "price_per_night", nullable = false)
    private BigDecimal pricePerNight;

    @Column(name = "available")
    private boolean available;

    @Column(name = "guest_name")
    private String guestName;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    @JsonBackReference
    private Hotel hotel;


}




