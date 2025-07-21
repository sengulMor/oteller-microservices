package com.oteller.reservationservice.service;

import com.oteller.enums.ReservationStatus;
import com.oteller.events.RoomReservedEvent;
import com.oteller.reservationservice.mapper.ReservationMapper;
import com.oteller.reservationservice.model.Reservation;
import com.oteller.reservationservice.repository.ReservationRepository;
import com.oteller.reservationservice.services.ReservationTransactionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReservationTransactionalServiceTest {

    private ReservationTransactionalService reservationTransactionalService;
    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservationMapper = mock(ReservationMapper.class);
        reservationTransactionalService = new ReservationTransactionalService(reservationRepository, reservationMapper);
    }


    @Test
    void create_shouldReturnDto_whenSuccess() {
        RoomReservedEvent event = getRoomReservedEvent();
        Reservation reservation = getReservation();
        Reservation savedReservation = getReservation();
        savedReservation.setId(1L);

        when(reservationMapper.toEntity(event)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(savedReservation);

        Long id = reservationTransactionalService.createReservation(event);

        assertEquals(1L, id);
        verify(reservationRepository).save(reservation);
    }

    private Reservation getReservation() {
        return new Reservation(1L, 1L,
                "guestName", LocalDate.now(), LocalDate.now().plusWeeks(1),
                ReservationStatus.CONFIRMED, "email");
    }

    private RoomReservedEvent getRoomReservedEvent() {
        return new RoomReservedEvent(1L, 1L,
                "guestName", LocalDate.now(), LocalDate.now().plusWeeks(1),
                ReservationStatus.CONFIRMED, "email");
    }
}
