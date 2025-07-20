package com.oteller.reservationservice.kafka.producer;

import com.oteller.enums.ReservationStatus;
import com.oteller.events.ReservationCreatedEvent;
import com.oteller.reservationservice.kafka.config.KafkaTopicsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReservationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    public ReservationEventProducer(KafkaTemplate<String, Object> kafkaTemplate,
                                    KafkaTopicsConfig kafkaTopicsConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicsConfig = kafkaTopicsConfig;
    }

    public void sendReservationCreatedEvent(Long reservationId, String email) {
        ReservationCreatedEvent event =
                new ReservationCreatedEvent(reservationId, ReservationStatus.CONFIRMED, email);
        kafkaTemplate.send(kafkaTopicsConfig.getReservationCreated(), event);
        log.info("Sent ReservationCreatedEvent for reservationId: {}", reservationId);
    }
}

