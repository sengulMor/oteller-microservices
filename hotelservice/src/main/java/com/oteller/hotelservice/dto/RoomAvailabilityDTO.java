package com.oteller.hotelservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityDTO {

    private Long roomId;
    private LocalDate startDate;
    private LocalDate endDate;
}
