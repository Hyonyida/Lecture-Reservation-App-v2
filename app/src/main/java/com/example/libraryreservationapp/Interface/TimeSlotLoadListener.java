package com.example.libraryreservationapp.Interface;

import com.example.libraryreservationapp.TimeSlot;

import java.util.List;

public interface TimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
