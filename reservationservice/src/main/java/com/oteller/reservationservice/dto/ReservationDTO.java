package com.oteller.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;
    private Long roomId;
    private String guestName;
    private LocalDate startDate;
    private LocalDate endDate;
}
