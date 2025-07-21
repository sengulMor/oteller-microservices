package com.oteller.hotelservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();


    @Test
    void handleHotelNotFoundException_shouldReturnNotFoundMessage() {
        Long id = 1L;
        HotelNotFoundException ex = new HotelNotFoundException(id);
        ResponseEntity<String> response = handler.handleHotelNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Hotel with ID " + id + " not found", response.getBody());
    }


    @Test
    void handleRoomNotFoundException_shouldReturnNotFoundMessage() {
        Long id = 1L;
        RoomNotFoundException ex = new RoomNotFoundException(id);
        ResponseEntity<String> response = handler.handleRoomNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Room with ID " + id + " not found", response.getBody());
    }


    @Test
    void handleDataIntegrityViolation_shouldReturnBadRequest() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Unique constraint failed", new RuntimeException());
        ResponseEntity<String> response = handler.handleDataIntegrityViolation(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Data integrity violation"));
    }

    @Test
    void handleDataAccessException_shouldReturnInternalServerError() {
        CannotAcquireLockException ex = new CannotAcquireLockException("Database error", new RuntimeException());
        ResponseEntity<String> response = handler.handleDataAccess(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Database error"));
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        RuntimeException ex = new RuntimeException("Unexpected error occurred", new RuntimeException());
        ResponseEntity<String> response = handler.handleGeneric(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Unexpected error occurred"));
    }

    @Test
    void handleValidationErrors_shouldReturnBadRequestWithErrors() {
        FieldError fieldError = new FieldError("Hotel", "name", "must not be blank");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodParameter methodParameter = mock(MethodParameter.class); // ✅ no reflection

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        // Call the handler
        ResponseEntity<List<ValidationError>> response = handler.handleValidationErrors(ex);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("name", response.getBody().get(0).field());
        assertEquals("must not be blank", response.getBody().get(0).message());
    }

}


