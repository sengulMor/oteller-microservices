package com.oteller.events;

import java.time.LocalDate;

public class RoomReservedEvent {

    private Long roomId;
    private String status;

    public RoomReservedEvent() {}

    public RoomReservedEvent(Long roomId, String status) {
        this.roomId = roomId;
        this.status = status;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
