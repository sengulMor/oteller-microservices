package com.oteller.events;

public class ReservationCreatedEvent {
    private Long reservationId;
    private String status;

    public ReservationCreatedEvent() {}

    public ReservationCreatedEvent(Long reservationId, String status) {
        this.reservationId = reservationId;
        this.status = status;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
