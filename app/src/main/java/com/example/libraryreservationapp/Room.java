package com.example.libraryreservationapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {

    //private member variables
    private String roomId;
    private String building;
    private int roomNumber;
    private int capacity;
    private boolean engineering;
    private boolean wifi;
    private boolean computer;
    private boolean available;

    //empty constructor
    public Room(){

    }

    //constructor for Room
    public Room(String building, int roomNumber, int capacity, boolean whiteboard, boolean wifi, boolean computer, boolean available) {
        this.building = building;
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.engineering = engineering;
        this.wifi = wifi;
        this.computer = computer;
        this.available = available;
    }

    protected Room(Parcel in) {
        building = in.readString();
        roomNumber = in.readInt();
        capacity = in.readInt();
        engineering = in.readByte() != 0;
        wifi = in.readByte() != 0;
        computer = in.readByte() != 0;
        available = in.readByte() != 0;
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    //gets building
    public String getBuilding() {
        return building;
    }

    //sets building
    public void setBuilding(String building) {
        this.building = building;
    }

    //gets room number
    public int getRoomNumber() {
        return roomNumber;
    }

    //sets room number
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    //gets capacity
    public int getCapacity() {
        return capacity;
    }

    //sets capacity
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    //gets whiteboard value
    public boolean isEngineering() {
        return engineering;
    }

    //sets whiteboard value
    public void setEngineering(boolean engineering) {
        this.engineering = engineering;
    }

    //gets wifi value
    public boolean isWifi() {
        return wifi;
    }

    //sets wifi value
    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    //gets computer value
    public boolean isComputer() {
        return computer;
    }

    //sets computer value
    public void setComputer(boolean computer) {
        this.computer = computer;
    }

    //gets available value
    public boolean isAvailable() {
        return available;
    }

    //sets available value
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(building);
        parcel.writeInt(roomNumber);
        parcel.writeInt(capacity);
        parcel.writeByte((byte) (engineering ? 1 : 0));
        parcel.writeByte((byte) (wifi ? 1 : 0));
        parcel.writeByte((byte) (computer ? 1 : 0));
        parcel.writeByte((byte) (available ? 1 : 0));
    }
}
