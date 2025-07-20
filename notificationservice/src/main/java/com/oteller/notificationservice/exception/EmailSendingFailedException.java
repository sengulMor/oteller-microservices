package com.oteller.notificationservice.exception;

public class EmailSendingFailedException extends RuntimeException {

    public EmailSendingFailedException(String email, Throwable cause) {
        super("Failed to send email to " + email, cause);
    }
}
