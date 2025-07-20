package com.oteller.notificationservice.service;

import com.oteller.notificationservice.exception.EmailSendingFailedException;
import com.oteller.notificationservice.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailProperties mailProperties;

    @InjectMocks
    private EmailService emailService;

    private final String email = "noreply@example.com";

    @Test
    void shouldSendEmail() {
        String to = "to";
        SimpleMailMessage message = getSimpleMailMessage(to, email);
        when(mailProperties.getUsername()).thenReturn(email);
        emailService.sendReservationConfirmation(to, "message");
        verify(mailSender).send(message);
    }

    @Test
    void testSend_ThrowsMailException() {
        when(mailProperties.getUsername()).thenReturn(email);

        doThrow(new MailSendException("Simulated failure"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(EmailSendingFailedException.class, () -> {
            emailService.sendReservationConfirmation(email, "Error Subject");
        });
    }

    private SimpleMailMessage getSimpleMailMessage(String to, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reservation Confirmation");
        message.setText("message");
        message.setFrom(email);
        return message;
    }
}
