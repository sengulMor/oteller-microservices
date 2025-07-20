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

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        // When
        eventProducer.sendReservationCreatedEvent(reservationId, "johntest@gmail.com");

        // Then
        ArgumentCaptor<ReservationCreatedEvent> eventCaptor = ArgumentCaptor.forClass(ReservationCreatedEvent.class);
        verify(kafkaTemplate).send(eq(topic), eventCaptor.capture());

        ReservationCreatedEvent sentEvent = eventCaptor.getValue();
        assertEquals(reservationId, sentEvent.getReservationId());
        assertEquals(ReservationStatus.CONFIRMED, sentEvent.getStatus());
    }
}

