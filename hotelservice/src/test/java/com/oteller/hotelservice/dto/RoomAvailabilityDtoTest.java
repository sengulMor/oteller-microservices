package com.oteller.hotelservice.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomAvailabilityDtoTest {

    @Test
    void testLombokGeneratedMethods() {
        LocalDate checkInDate = LocalDate.now().plusDays(2);
        LocalDate checkOutDate = LocalDate.now().plusDays(5);
        RoomAvailabilityDto dto = new RoomAvailabilityDto();
        dto.setHotelId(1L);
        dto.setRoomId(2L);
        dto.setGuestName("Alice");
        dto.setEmail("email");
        dto.setCheckInDate(checkInDate);
        dto.setCheckOutDate(checkOutDate);


        assertEquals(2L, dto.getRoomId());
        assertEquals("email", dto.getEmail());
        assertEquals("Alice", dto.getGuestName());

        RoomAvailabilityDto builtDto = RoomAvailabilityDto.builder()
                .hotelId(2L)
                .roomId(6L)
                .guestName("Meli")
                .email("meli@gmail.com")
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();

        assertEquals(checkInDate, builtDto.getCheckInDate());
        assertEquals(checkOutDate, builtDto.getCheckOutDate());
        assertEquals(6L, builtDto.getRoomId());
        assertEquals(2L, builtDto.getHotelId());
        assertEquals("meli@gmail.com", builtDto.getEmail());
    }
}
