package com.oteller.reservationservice.mapper;


import com.oteller.events.RoomReservedEvent;
import com.oteller.reservationservice.dto.ReservationDTO;
import com.oteller.reservationservice.model.Reservation;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReservationMapperTest {

    private final ReservationMapper mapper = Mappers.getMapper(ReservationMapper.class);

    @Test
    void testToEvent() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setRoomId(10L);
        reservation.setHotelId(20L);
        reservation.setGuestName("Jane Doe");
        reservation.setCheckInDate(LocalDate.of(2025, 7, 20));
        reservation.setCheckOutDate(LocalDate.of(2025, 7, 25));

        RoomReservedEvent event = mapper.toEvent(reservation);

        assertNotNull(event);
        assertEquals(reservation.getRoomId(), event.getRoomId());
        assertEquals(reservation.getHotelId(), event.getHotelId());
        assertEquals(reservation.getGuestName(), event.getGuestName());
        assertEquals(reservation.getCheckInDate(), event.getCheckInDate());
        assertEquals(reservation.getCheckOutDate(), event.getCheckOutDate());
    }

    @Test
    void testToEntity() {
        RoomReservedEvent event = new RoomReservedEvent();
        event.setRoomId(5L);
        event.setHotelId(3L);
        event.setGuestName("John Smith");
        event.setCheckInDate(LocalDate.of(2025, 8, 1));
        event.setCheckOutDate(LocalDate.of(2025, 8, 5));

        Reservation reservation = mapper.toEntity(event);

        assertNotNull(reservation);
        assertEquals(event.getRoomId(), reservation.getRoomId());
        assertEquals(event.getHotelId(), reservation.getHotelId());
        assertEquals(event.getGuestName(), reservation.getGuestName());
        assertEquals(event.getCheckInDate(), reservation.getCheckInDate());
        assertEquals(event.getCheckOutDate(), reservation.getCheckOutDate());
    }

    @Test
    void testToDTO() {
        Reservation reservation = new Reservation();
        reservation.setId(100L);
        reservation.setRoomId(1L);
        reservation.setHotelId(2L);
        reservation.setGuestName("Alice");
        reservation.setCheckInDate(LocalDate.of(2025, 9, 1));
        reservation.setCheckOutDate(LocalDate.of(2025, 9, 3));

        ReservationDTO dto = mapper.toDTO(reservation);

        assertNotNull(dto);
        assertEquals(reservation.getId(), dto.getId());
        assertEquals(reservation.getRoomId(), dto.getRoomId());
        assertEquals(reservation.getHotelId(), dto.getHotelId());
        assertEquals(reservation.getGuestName(), dto.getGuestName());
        assertEquals(reservation.getCheckInDate(), dto.getCheckInDate());
        assertEquals(reservation.getCheckOutDate(), dto.getCheckOutDate());
    }

    @Test
    void testToDtoList() {
        Reservation res1 = new Reservation();
        res1.setId(1L);
        res1.setGuestName("Guest 1");

        Reservation res2 = new Reservation();
        res2.setId(2L);
        res2.setGuestName("Guest 2");

        List<ReservationDTO> dtoList = mapper.toDtoList(List.of(res1, res2));

        assertEquals(2, dtoList.size());
        assertEquals("Guest 1", dtoList.get(0).getGuestName());
        assertEquals("Guest 2", dtoList.get(1).getGuestName());
    }
}
