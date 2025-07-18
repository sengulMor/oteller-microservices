package com.oteller.hotelservice.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class HotelUpdateException extends RuntimeException {

    public HotelUpdateException(Long id, DataIntegrityViolationException ex) {
        super("Hotel with ID " + id + " could not updated. Constraint violation.", ex);
    }
}
