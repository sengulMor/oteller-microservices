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

    @Tool(description = "Prüft, ob für das Hotel mindestens ein als verfügbar markiertes Zimmer vorhanden ist. " +
            "Check-in und Check-out werden auf Pflichtangabe und korrekte Reihenfolge geprüft; " +
            "die aktuelle Verfügbarkeitsabfrage berücksichtigt die Belegung im angegebenen Zeitraum nicht.")
    public boolean checkRoomAvailability(
            @ToolParam(description = "Pflichtangabe: positive numerische ID des Hotels.") Long hotelId,
            @ToolParam(description = "Pflichtangabe: Check-in-Datum im ISO-Format YYYY-MM-DD.") LocalDate checkIn,
            @ToolParam(description = "Pflichtangabe: Check-out-Datum im ISO-Format YYYY-MM-DD. " +
                    "Muss nach dem Check-in-Datum liegen.") LocalDate checkOut
    ) {
        validateParameters(hotelId, checkIn, checkOut);

        HotelAvailabilityDto availabilityDto = HotelAvailabilityDto.builder()
                .hotelId(hotelId)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .build();
        return roomService.checkIfAvailable(availabilityDto);
    }

    private void validateParameters(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        if (hotelId == null || hotelId <= 0) {
            throw new IllegalArgumentException("Hotel-ID muss eine positive Zahl sein");
        }
        if (checkIn == null) {
            throw new IllegalArgumentException("Check-in-Datum ist erforderlich");
        }
        if (checkOut == null) {
            throw new IllegalArgumentException("Check-out-Datum ist erforderlich");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-out-Datum muss nach dem Check-in-Datum liegen");
        }
    }
}
