package com.oteller.events;

import com.oteller.enums.ReservationStatus;

public class ReservationCreatedEvent {
    private Long reservationId;
    private ReservationStatus status;
    private String email;

    public ReservationCreatedEvent() {
    }

    public ReservationCreatedEvent(Long reservationId, ReservationStatus status, String email) {
        this.reservationId = reservationId;
        this.status = status;
        this.email = email;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
