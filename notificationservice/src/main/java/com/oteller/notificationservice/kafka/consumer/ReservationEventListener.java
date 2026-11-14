package com.oteller.notificationservice.kafka.consumer;

import com.oteller.events.ReservationCreatedEvent;
import com.oteller.notificationservice.services.AiNotificationService;
import com.oteller.notificationservice.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservationEventListener {

    private final EmailService emailService;
    private final AiNotificationService aiNotificationService;

    public ReservationEventListener(EmailService emailService, AiNotificationService aiNotificationService) {
        this.emailService = emailService;
        this.aiNotificationService = aiNotificationService;
    }


    @KafkaListener(topics = "${spring.kafka.topic.reservation-created}", groupId = "notification-group")
    public void listen(ReservationCreatedEvent event) {
        if (event.getReservationId() == null ||
                event.getStatus() == null || event.getEmail() == null) {
            log.warn("Invalid event received: {}", event);
            throw new IllegalArgumentException("Invalid event received, empty fields.");
        }

        // Generate email message via Spring AI
        String aiMessage = aiNotificationService.generateWelcomeMessage(
                event.getName(),
                event.getHotelName(),
                event.getRoomType()
        );

        emailService.sendReservationConfirmation(event.getEmail(), aiMessage);

        log.info("AI-enhanced email for Reservation {} sent successfully!", event.getReservationId());
    }
}
