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
        RoomReservedEvent event = getRoomReservedEvent();
        String topic = "room-reserved-topic";
        when(kafkaTopicsConfig.getRoomReserved()).thenReturn(topic);
        when(kafkaTemplate.send(anyString(), any(RoomReservedEvent.class)))
                .thenReturn(CompletableFuture.completedFuture(null));

        roomReservedEventProducer.sendRoomReservedEvent(event);

        verify(kafkaTopicsConfig, times(1)).getRoomReserved();
        verify(kafkaTemplate, times(1)).send(topic, event);
    }


    @Test
    void testSendRoomReservedEvent_exception() {
        RoomReservedEvent event = getRoomReservedEvent();
        String topic = "room-reserved-topic";
        when(kafkaTopicsConfig.getRoomReserved()).thenReturn(topic);
        when(kafkaTemplate.send(topic, event))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Simulated Exception")));

        roomReservedEventProducer.sendRoomReservedEvent(event);

        verify(kafkaTopicsConfig, times(1)).getRoomReserved();
        verify(kafkaTemplate, times(1)).send("room-reserved-topic", event);
    }

    private RoomReservedEvent getRoomReservedEvent() {
        RoomReservedEvent event = new RoomReservedEvent();
        event.setRoomId(1L);
        event.setHotelId(10L);
        return event;
    }
}

