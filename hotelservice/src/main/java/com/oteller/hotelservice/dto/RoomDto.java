package com.oteller.hotelservice.dto;

import com.oteller.hotelservice.validation.ConditionalNotBlank;
import com.oteller.hotelservice.validation.UniqueRoom;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConditionalNotBlank
@UniqueRoom
public class RoomDto {

    private Long id;

    @NotNull
    @NotBlank(message = "Room Number is required")
    private String roomNumber;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotNull(message = "Price per Night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal pricePerNight;

    private String guestName;

    private boolean available;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    @NotNull(message = "Hotel is required")
    @Valid
    private Long hotelId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
