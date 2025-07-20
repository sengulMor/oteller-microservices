package com.oteller.reservationservice.kafka.consumer;

import com.oteller.events.RoomReservedEvent;
import com.oteller.reservationservice.kafka.producer.ReservationEventProducer;
import com.oteller.reservationservice.services.ReservationTransactionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomReservedEventListener {

    private final ReservationTransactionalService reservationService;
    private final ReservationEventProducer eventProducer;

    public RoomReservedEventListener(ReservationTransactionalService reservationService,
                                     ReservationEventProducer eventProducer) {
        this.reservationService = reservationService;
        this.eventProducer = eventProducer;
    }

    @KafkaListener(topics = "${spring.kafka.topic.room-reserved}", groupId = "reservation-group")
    public void listen(RoomReservedEvent reservedEvent) {
        if (reservedEvent.getGuestName() == null ||
                reservedEvent.getCheckInDate() == null ||
                reservedEvent.getCheckOutDate() == null) {
            log.warn("Invalid event received: {}", reservedEvent);
            throw new IllegalArgumentException("Invalid event received, empty fields.");
        }
        Long id = reservationService.createReservation(reservedEvent);
        eventProducer.sendReservationCreatedEvent(id, reservedEvent.getEmail());
        log.info("Reservation created for room: {}", reservedEvent.getRoomId());
    }
}

