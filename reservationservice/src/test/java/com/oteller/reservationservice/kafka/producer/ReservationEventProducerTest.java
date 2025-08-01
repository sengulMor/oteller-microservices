package com.oteller.reservationservice.kafka.producer;

import com.oteller.enums.ReservationStatus;
import com.oteller.events.ReservationCreatedEvent;
import com.oteller.reservationservice.kafka.config.KafkaTopicsConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private KafkaTopicsConfig kafkaTopicsConfig;

    @InjectMocks
    private ReservationEventProducer eventProducer;

    @Test
    void testSendReservationCreatedEvent() {
        // Given
        Long reservationId = 42L;
        String topic = "reservation-created-topic";
        when(kafkaTopicsConfig.getReservationCreated()).thenReturn(topic);
        when(kafkaTemplate.send(eq(topic), any(ReservationCreatedEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        // When
        eventProducer.sendReservationCreatedEvent(reservationId, "johntest@gmail.com");

        // Then
        ArgumentCaptor<ReservationCreatedEvent> eventCaptor = ArgumentCaptor.forClass(ReservationCreatedEvent.class);
        verify(kafkaTemplate).send(eq(topic), eventCaptor.capture());
        ReservationCreatedEvent sentEvent = eventCaptor.getValue();
        assertEquals(reservationId, sentEvent.getReservationId());
        assertEquals(ReservationStatus.CONFIRMED, sentEvent.getStatus());


    }

    @Test
    void testSendReservationCreatedEvent_shouldSendCorrectEvent() {
        // given
        Long reservationId = 42L;
        String email = "test@example.com";
        String topic = "reservation-created-topic";
        when(kafkaTopicsConfig.getReservationCreated()).thenReturn(topic);
        when(kafkaTemplate.send(eq(topic), any(ReservationCreatedEvent.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Simulated Exception")));

        // when
        eventProducer.sendReservationCreatedEvent(reservationId, email);

        // then
        ArgumentCaptor<ReservationCreatedEvent> captor = ArgumentCaptor.forClass(ReservationCreatedEvent.class);
        verify(kafkaTemplate).send(eq(topic), captor.capture());

        ReservationCreatedEvent actual = captor.getValue();
        assertEquals(reservationId, actual.getReservationId());
        assertEquals(email, actual.getEmail());
    }
}

