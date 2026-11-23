package com.oteller.hotelservice.service;

import com.oteller.hotelservice.dto.HotelAvailabilityDto;
import com.oteller.hotelservice.services.HotelAvailabilityTools;
import com.oteller.hotelservice.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelAvailabilityToolsTest {

    @Mock
    private RoomService roomService;

    private HotelAvailabilityTools tools;

    @BeforeEach
    void setUp() {
        tools = new HotelAvailabilityTools(roomService);
    }

    @Test
    void shouldReturnTrueAndPassAvailabilityDto() {
        Long hotelId = 4L;
        LocalDate checkIn = LocalDate.of(2026, 6, 10);
        LocalDate checkOut = LocalDate.of(2026, 6, 15);
        when(roomService.checkIfAvailable(any(HotelAvailabilityDto.class))).thenReturn(true);

        boolean result = tools.checkRoomAvailability(hotelId, checkIn, checkOut);

        assertTrue(result);
        ArgumentCaptor<HotelAvailabilityDto> captor = ArgumentCaptor.forClass(HotelAvailabilityDto.class);
        verify(roomService).checkIfAvailable(captor.capture());
        HotelAvailabilityDto dto = captor.getValue();
        assertEquals(hotelId, dto.getHotelId());
        assertEquals(checkIn, dto.getCheckInDate());
        assertEquals(checkOut, dto.getCheckOutDate());
    }

    @Test
    void shouldReturnFalseWhenRoomServiceReturnsFalse() {
        when(roomService.checkIfAvailable(any(HotelAvailabilityDto.class))).thenReturn(false);

        boolean result = tools.checkRoomAvailability(4L,
                LocalDate.of(2026, 6, 10),
                LocalDate.of(2026, 6, 15));

        assertFalse(result);
        verify(roomService).checkIfAvailable(any(HotelAvailabilityDto.class));
    }

    @Test
    void shouldRejectNullHotelId() {
        assertThrows(IllegalArgumentException.class,
                () -> tools.checkRoomAvailability(null,
                        LocalDate.of(2026, 6, 10),
                        LocalDate.of(2026, 6, 15)));
        verify(roomService, never()).checkIfAvailable(any());
    }

    @Test
    void shouldRejectNonPositiveHotelId() {
        assertThrows(IllegalArgumentException.class,
                () -> tools.checkRoomAvailability(0L,
                        LocalDate.of(2026, 6, 10),
                        LocalDate.of(2026, 6, 15)));
        verify(roomService, never()).checkIfAvailable(any());
    }

    @Test
    void shouldRejectNullCheckInDate() {
        assertThrows(IllegalArgumentException.class,
                () -> tools.checkRoomAvailability(4L, null,
                        LocalDate.of(2026, 6, 15)));
        verify(roomService, never()).checkIfAvailable(any());
    }

    @Test
    void shouldRejectNullCheckOutDate() {
        assertThrows(IllegalArgumentException.class,
                () -> tools.checkRoomAvailability(4L,
                        LocalDate.of(2026, 6, 10), null));
        verify(roomService, never()).checkIfAvailable(any());
    }

    @Test
    void shouldRejectCheckOutBeforeOrAtCheckIn() {
        LocalDate checkIn = LocalDate.of(2026, 6, 10);

        assertThrows(IllegalArgumentException.class,
                () -> tools.checkRoomAvailability(4L, checkIn, checkIn));
        assertThrows(IllegalArgumentException.class,
                () -> tools.checkRoomAvailability(4L, checkIn, checkIn.minusDays(1)));
        verify(roomService, never()).checkIfAvailable(any());
    }
}
