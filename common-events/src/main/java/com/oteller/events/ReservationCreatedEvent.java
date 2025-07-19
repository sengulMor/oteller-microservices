package com.oteller.events;

import com.oteller.enums.ReservationStatus;

public class ReservationCreatedEvent {
    private Long reservationId;
    private ReservationStatus status;

    public ReservationCreatedEvent() {}

    public ReservationCreatedEvent(Long reservationId, ReservationStatus status) {
        this.reservationId = reservationId;
        this.status = status;
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
}
