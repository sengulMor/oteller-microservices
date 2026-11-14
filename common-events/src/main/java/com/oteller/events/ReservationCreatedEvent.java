package com.oteller.events;

import com.oteller.enums.ReservationStatus;

public class ReservationCreatedEvent {
    private Long reservationId;
    private ReservationStatus status;
    private String email;
    private String name;
    private String hotelName;
    private String roomType;


    public ReservationCreatedEvent() {
    }

    public ReservationCreatedEvent(Long reservationId, ReservationStatus status, String email,
                                   String name, String hotelName, String roomType) {
        this.reservationId = reservationId;
        this.status = status;
        this.email = email;
        this.name = name;
        this.hotelName = hotelName;
        this.roomType = roomType;
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

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getName() {
        return name;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getRoomType() {
        return roomType;
    }
}
