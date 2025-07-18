package com.oteller.events;


public class RoomReservedEvent {

    private Long roomId;
    private Long hotelId;
    private String status;

    public RoomReservedEvent() {}

    public RoomReservedEvent(Long roomId, Long hotelId, String status) {
        this.roomId = roomId;
        this.hotelId = hotelId;
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

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
