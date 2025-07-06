package com.oteller.reservationservice.services;

import com.oteller.events.ReservationCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, ReservationCreatedEvent> kafkaTemplate;

    public void sendReservationCreatedEvent(ReservationCreatedEvent event) {
        kafkaTemplate.send("reservation-created-events", event);
    }
}
