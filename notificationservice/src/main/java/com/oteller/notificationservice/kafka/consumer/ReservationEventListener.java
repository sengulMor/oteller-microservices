package com.oteller.notificationservice.kafka.consumer;

import com.oteller.events.ReservationCreatedEvent;
import com.oteller.notificationservice.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservationEventListener {

    private final EmailService emailService;

    @Autowired
    public ReservationEventListener(EmailService emailService) {
        this.emailService = emailService;
    }


    @KafkaListener(topics = "${spring.kafka.topic.reservation-created}", groupId = "notification-group")
    public void listen(ReservationCreatedEvent event) {
        if (event.getReservationId() == null ||
                event.getStatus() == null || event.getEmail() == null) {
            log.warn("Invalid event received: {}", event);
            throw new IllegalArgumentException("Invalid event received, empty fields.");
        }
        String message = getConfirmationMessage(event);
        emailService.sendReservationConfirmation(event.getEmail(), "Thank you for your reservation!\nDetails:\n" + message);
        log.info("Email for Reservation is send: {}", event.getReservationId());
    }

    private String getConfirmationMessage(ReservationCreatedEvent event) {
        return "Thank you for your reservation!\nDetails:\n {\"reservationId\":" + event.getReservationId() + ",\"status\":" + event.getStatus() + "}";
    }
}
