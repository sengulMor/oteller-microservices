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
    private Long hotelId;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
