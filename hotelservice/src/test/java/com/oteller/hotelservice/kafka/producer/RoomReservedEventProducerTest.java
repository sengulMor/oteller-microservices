package com.oteller.hotelservice.kafka.producer;


import com.oteller.events.RoomReservedEvent;
import com.oteller.hotelservice.kafka.config.KafkaTopicsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class RoomReservedEventProducerTest {

    private KafkaTemplate<String, RoomReservedEvent> kafkaTemplate;
    private KafkaTopicsConfig kafkaTopicsConfig;
    private RoomReservedEventProducer roomReservedEventProducer;

    @BeforeEach
    void setup() {
        kafkaTemplate = mock(KafkaTemplate.class);
        kafkaTopicsConfig = mock(KafkaTopicsConfig.class);
        roomReservedEventProducer = new RoomReservedEventProducer(kafkaTemplate, kafkaTopicsConfig);
    }

    @Test
    void testSendRoomReservedEvent_success() {
        // Given
        RoomReservedEvent event = new RoomReservedEvent();
        event.setRoomId(1L);
        event.setHotelId(10L);

        when(kafkaTopicsConfig.getRoomReserved()).thenReturn("room-reserved-topic");
        when(kafkaTemplate.send(anyString(), any(RoomReservedEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        // When
        roomReservedEventProducer.sendRoomReservedEvent(event);

        // Then
        verify(kafkaTopicsConfig, times(1)).getRoomReserved();
        verify(kafkaTemplate, times(1)).send("room-reserved-topic", event);
    }
}
