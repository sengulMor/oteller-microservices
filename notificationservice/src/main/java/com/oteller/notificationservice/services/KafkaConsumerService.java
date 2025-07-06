package com.oteller.notificationservice.services;

import com.oteller.events.ReservationCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    private final EmailService emailService;

    @Autowired
    public KafkaConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }


    @KafkaListener(topics = "reservation-created-events", groupId = "notification-group")
    public void handleReservationCreatedEvent(ReservationCreatedEvent event) {
        log.info("Sending email for reservation: {}", event.getReservationId());
        String message = "{\"reservationId\":" + event.getReservationId() + ",\"status\":\"CREATED\"}";
        // Simulate extracting user email from message
        String email = "receiverfakemail@gmail.com"; // Replace with dynamic logic later
        emailService.sendReservationConfirmation(email, "Thank you for your reservation!\nDetails:\n" + message);
    }
}
