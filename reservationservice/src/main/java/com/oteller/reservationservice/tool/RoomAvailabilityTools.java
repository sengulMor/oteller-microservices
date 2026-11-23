package com.oteller.reservationservice.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class RoomAvailabilityTools {

    // Hier könntest du z. B. deinen RoomRepository oder RoomService injizieren

    @Tool(description = """
            Prüft die Verfügbarkeit von Hotelzimmern für ein bestimmtes Datum und einen Zimmertyp.
            Nutze dieses Tool immer, wenn ein Gast nach freien Zimmern oder Verfügbarkeiten fragt.
            """)
    public AvailabilityResult checkAvailability(
            @ToolParam(description = "Startdatum im Format YYYY-MM-DD, z. B. 2026-11-27") String date,
            @ToolParam(description = "Zimmertyp, z. B. SINGLE, DOUBLE, SUITE") String roomType
    ) {
        // Beispiel-Logik – hier später durch echte DB-Abfrage ersetzen
        int freeRooms = "DOUBLE".equalsIgnoreCase(roomType) ? 3 : 1;
        return new AvailabilityResult(date, roomType, freeRooms, freeRooms > 0);
    }

    public record AvailabilityResult(
            String date,
            String roomType,
            int availableCount,
            boolean available
    ) {
    }
}
