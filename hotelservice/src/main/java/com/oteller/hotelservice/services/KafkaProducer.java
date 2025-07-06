package com.oteller.hotelservice.services;

import com.oteller.events.RoomReservedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, RoomReservedEvent> kafkaTemplate;

    public void sendRoomReservedEvent(RoomReservedEvent event) {
        kafkaTemplate.send("room-reserved-events", event);
    }
}
