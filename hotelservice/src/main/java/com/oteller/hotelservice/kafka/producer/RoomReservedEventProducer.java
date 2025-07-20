package com.oteller.hotelservice.kafka.producer;

import com.oteller.events.RoomReservedEvent;
import com.oteller.hotelservice.kafka.config.KafkaTopicsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoomReservedEventProducer {

    private final KafkaTemplate<String, RoomReservedEvent> kafkaTemplate;
    private final KafkaTopicsConfig kafkaTopics;

    public RoomReservedEventProducer(KafkaTemplate<String, RoomReservedEvent> kafkaTemplate, KafkaTopicsConfig kafkaTopics) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopics = kafkaTopics;
    }

    public void sendRoomReservedEvent(RoomReservedEvent event) {
        log.info("Sending RoomReservedEvent: {}", event);

        kafkaTemplate.send(kafkaTopics.getRoomReserved(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent event=[{}] with offset=[{}]", event, result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send event=[{}] due to: {}", event, ex.getMessage(), ex);
                    }
                });
    }
}
