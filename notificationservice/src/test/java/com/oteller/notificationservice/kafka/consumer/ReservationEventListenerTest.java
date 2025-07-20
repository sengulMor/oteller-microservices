package com.oteller.notificationservice.kafka.consumer;

import com.oteller.enums.ReservationStatus;
import com.oteller.events.ReservationCreatedEvent;
import com.oteller.notificationservice.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ReservationEventListenerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ReservationEventListener listener;

    @Test
    void testListen_ValidEvent_ShouldSendEmail() {
        // Given
        ReservationCreatedEvent event = new ReservationCreatedEvent();
        event.setReservationId(1L);
        event.setStatus(ReservationStatus.CONFIRMED);
        event.setEmail("johntest@gmail.com");

        Mockito.doNothing().when(emailService).sendReservationConfirmation(anyString(), anyString());
        // When
        listener.listen(event);

        verify(emailService).sendReservationConfirmation(anyString(), anyString());
    }

    @Test
    void testListen_InvalidEvent_ShouldThrowException() {
        // Given: missing email
        ReservationCreatedEvent event = new ReservationCreatedEvent();
        event.setReservationId(1L);
        event.setStatus(ReservationStatus.CONFIRMED);

        // Then
        assertThrows(IllegalArgumentException.class, () -> listener.listen(event));
        verifyNoInteractions(emailService);
    }
}

