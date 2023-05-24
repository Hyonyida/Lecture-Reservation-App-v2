package com.example.libraryreservationapp.Interface;

import com.example.libraryreservationapp.Room;

import java.util.List;

public interface RoomLoadListener {
    void onAllRoomLoadSuccess(List<Room> roomList);
    void onAllRoomLoadFailed(String message);
}
