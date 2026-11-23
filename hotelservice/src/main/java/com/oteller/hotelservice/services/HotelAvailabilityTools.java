package com.oteller.hotelservice.services;

import com.oteller.hotelservice.dto.HotelAvailabilityDto;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HotelAvailabilityTools {

    private final RoomService roomService;

    public HotelAvailabilityTools(RoomService roomService) {
        this.roomService = roomService;
    }

    @Tool(description = "Prüft, ob für ein bestimmtes Hotel und Datum freie Zimmer verfügbar sind")
    public boolean checkRoomAvailability(
            @ToolParam(description = "ID des Hotels") Long hotelId,
            @ToolParam(description = "Check-in Datum im Format YYYY-MM-DD") LocalDate checkIn,
            @ToolParam(description = "Check-out Datum im Format YYYY-MM-DD") LocalDate checkOut
    ) {
        HotelAvailabilityDto availabilityDto = HotelAvailabilityDto.builder().hotelId(hotelId).checkInDate(checkIn).checkOutDate(checkOut).build();
        return roomService.checkIfAvailable(availabilityDto);
    }
}
