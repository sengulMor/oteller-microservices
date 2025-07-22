package com.oteller.hotelservice.exception;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void testErrorResponseValues() {
        int status = 500;
        String message = "Internal Server Error";
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse errorResponse = new ErrorResponse(status, message, now);

        assertEquals(status, errorResponse.status());
        assertEquals(message, errorResponse.message());
        assertEquals(now, errorResponse.timestamp());
    }

    @Test
    void testErrorResponseEquality() {
        LocalDateTime time = LocalDateTime.of(2025, 7, 21, 15, 0);
        ErrorResponse error1 = new ErrorResponse(404, "Not Found", time);
        ErrorResponse error2 = new ErrorResponse(404, "Not Found", time);

        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
    }
}
