package com.oteller.notificationservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();


    @Test
    void handleHotelNotFoundException_shouldReturnNotFoundMessage() {
        EmailSendingFailedException ex = new EmailSendingFailedException("email", new RuntimeException());
        ResponseEntity<String> response = handler.handleEmailSendingFailed(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to send email to email", response.getBody());
    }

}


