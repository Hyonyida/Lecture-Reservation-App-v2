package com.example.libraryreservationapp;

public class RoomReservationInformation {
    public String time, date, roomId, roomNumber, building, userId, reservationId;
    public Long slot;

    public RoomReservationInformation() {
    }

    public RoomReservationInformation(String time, String date, String roomId, String roomNumber, String building, String userId, String reservationId, Long slot) {
        this.time = time;
        this.date = date;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.building = building;
        this.userId = userId;
        this.reservationId = reservationId;
        this.slot = slot;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() { return date;}

    public void setDate(String date) {this.date = date;}

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
}
