package com.oteller.reservationservice.dto;

import com.oteller.reservationservice.validation.DateInRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DateInRange
public class ReservationDTO {

    private Long id;

    @NotNull(message = "Hotel Id is required")
    private Long hotelId;

    @NotNull(message = "Room Id is required")
    private Long roomId;

    @NotBlank(message = "Guest name is required")
    private String guestName;

    @NotNull(message = "Check-In date is required")
    private LocalDate checkInDate;

    @NotNull(message = "Check-Out date is required")
    private LocalDate checkOutDate;
}