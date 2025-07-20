package com.oteller.notificationservice.services;

import com.oteller.notificationservice.exception.EmailSendingFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;

    public EmailService(JavaMailSender mailSender, MailProperties mailProperties) {
        this.mailSender = mailSender;
        this.mailProperties = mailProperties;
    }

    public void sendReservationConfirmation(String to, String messageText) {
        String from = mailProperties.getUsername();
        SimpleMailMessage message = getSimpleMailMessage(to, messageText, from);
        try {
            mailSender.send(message);
            log.info("Email sent successfully!");
        } catch (MailException e) {
            log.error("Error sending email: {}", e.getMessage(), e);
            throw new EmailSendingFailedException(from, e);
        }
    }

    private SimpleMailMessage getSimpleMailMessage(String to, String messageText, String from) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reservation Confirmation");
        message.setText(messageText);
        message.setFrom(from);
        return message;
    }
}

