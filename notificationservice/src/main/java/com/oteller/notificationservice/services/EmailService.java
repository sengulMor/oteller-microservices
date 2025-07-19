package com.oteller.notificationservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendReservationConfirmation(String to, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reservation Confirmation");
        message.setText(messageText);
        message.setFrom("senguelmorneu@gmail.com");

        try {
            mailSender.send(message);
            log.info("Email sent successfully!");
        } catch (MailException e) {
            e.printStackTrace();
            log.info("Error sending email: {}", e.getMessage());
        }
    }
}

