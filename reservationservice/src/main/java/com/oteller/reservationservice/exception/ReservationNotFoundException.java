package com.oteller.reservationservice.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("Reservation with ID " + id + " not found");
    }
}
