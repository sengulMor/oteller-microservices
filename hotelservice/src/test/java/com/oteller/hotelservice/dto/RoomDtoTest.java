package com.oteller.hotelservice.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoomDtoTest {

    @Test
    void testLombokGeneratedMethods() {
        RoomDto room = new RoomDto();
        room.setId(1L);
        room.setRoomNumber("101A");
        room.setCapacity(2);
        room.setPricePerNight(BigDecimal.valueOf(99.99));
        room.setGuestName("Alice");
        room.setAvailable(true);
        room.setCheckInDate(LocalDate.of(2025, 7, 21));
        room.setCheckOutDate(LocalDate.of(2025, 7, 23));
        room.setHotelId(10L);
        room.setCreatedAt(LocalDateTime.now());
        room.setUpdatedAt(LocalDateTime.now());

        assertEquals("101A", room.getRoomNumber());
        assertEquals(2, room.getCapacity());
        assertTrue(room.isAvailable());

        RoomDto builtRoom = RoomDto.builder()
                .id(2L)
                .roomNumber("202B")
                .capacity(3)
                .pricePerNight(BigDecimal.valueOf(120.0))
                .available(false)
                .hotelId(20L)
                .build();

        assertEquals("202B", builtRoom.getRoomNumber());
        assertEquals(3, builtRoom.getCapacity());
    }
}

