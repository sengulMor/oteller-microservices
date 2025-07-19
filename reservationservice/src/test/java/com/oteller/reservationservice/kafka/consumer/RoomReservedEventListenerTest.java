package com.oteller.reservationservice.kafka.consumer;

import com.oteller.events.RoomReservedEvent;
import com.oteller.reservationservice.kafka.producer.ReservationEventProducer;
import com.oteller.reservationservice.services.ReservationTransactionalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomReservedEventListenerTest {

    @Mock
    private ReservationTransactionalService reservationService;

    @Mock
    private ReservationEventProducer eventProducer;

    @InjectMocks
    private RoomReservedEventListener listener;

    @Test
    void testListen_ValidEvent_ShouldCreateReservationAndSendEvent() {
        // Given
        RoomReservedEvent event = new RoomReservedEvent();
        event.setRoomId(1L);
        event.setHotelId(2L);
        event.setGuestName("John Doe");
        event.setCheckInDate(LocalDate.now());
        event.setCheckOutDate(LocalDate.now().plusDays(2));

        when(reservationService.createReservation(event)).thenReturn(123L);

        // When
        listener.listen(event);

        // Then
        verify(reservationService).createReservation(event);
        verify(eventProducer).sendReservationCreatedEvent(123L);
    }

    @Test
    void testListen_InvalidEvent_ShouldThrowException() {
        // Given: missing guestName
        RoomReservedEvent event = new RoomReservedEvent();
        event.setRoomId(1L);
        event.setHotelId(2L);
        event.setCheckInDate(LocalDate.now());
        event.setCheckOutDate(LocalDate.now().plusDays(2));

        // Then
        assertThrows(IllegalArgumentException.class, () -> listener.listen(event));
        verifyNoInteractions(reservationService, eventProducer);
    }
}

