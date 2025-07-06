package com.oteller.hotelservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private Long id;

    @NotNull(message = "Room Number is required")
    private String roomNumber;

    private int capacity;
    private BigDecimal pricePerNight;
    private boolean available;
    private boolean reserved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull(message = "Hotel is required")
    @Valid
    private Long hotelId;
}
