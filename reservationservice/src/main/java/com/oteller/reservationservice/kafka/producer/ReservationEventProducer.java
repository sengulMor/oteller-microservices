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

    private final KafkaTemplate<String, ReservationCreatedEvent> kafkaTemplate;
    private final KafkaTopicsConfig kafkaTopicsConfig;

    public ReservationEventProducer(KafkaTemplate<String, ReservationCreatedEvent> kafkaTemplate,
                                    KafkaTopicsConfig kafkaTopicsConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicsConfig = kafkaTopicsConfig;
    }

    public void sendReservationCreatedEvent(Long reservationId, String email) {
        log.info("Sending ReservationCreatedEvent to : {}", email);

        ReservationCreatedEvent event = getReservationCreatedEvent(reservationId, email);

        kafkaTemplate.send(kafkaTopicsConfig.getReservationCreated(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent event=[{}] with offset=[{}]", event, result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send event=[{}] due to: {}", event, ex.getMessage(), ex);
                    }
                });
    }

    private ReservationCreatedEvent getReservationCreatedEvent(Long reservationId, String email) {
        return new ReservationCreatedEvent(reservationId, ReservationStatus.CONFIRMED, email);
    }
}
