package com.example.libraryreservationapp.Common;

import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.Room;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;


public class Common {

    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_ROOM_SELECTED = "ROOM_SELECTED";
    public static final String KEY_ENABLE_BUTTON_NEXT = "KEY_ENABLE_BUTTON_NEXT";
    public static final int TIME_SLOT_TOTAL = 14;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_RESERVATION = "CONFIRM_RESERVATION";



    public static int step = 0; // first step is 0
    public static Room currentRoom;
    public static int currentTimeSlot = -1;
    public static Calendar currentDate = Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd"); // only use it when needed for key
    public static String userID;



    public static String convertTimeSlotToString(int slot) {
        if (slot == 0 && (LocalTime.now().isBefore(LocalTime.parse("09:00"))  || isDayAfterToday()))
            return "8:00AM-9:00AM";
        else if (slot == 1 && (LocalTime.now().isBefore(LocalTime.parse("10:00"))  || isDayAfterToday()))
            return "9:00AM-10:00AM";
        else if (slot == 2 && (LocalTime.now().isBefore(LocalTime.parse("11:00"))  || isDayAfterToday()))
            return "10:00AM-11:00AM";
        else if (slot == 3 && (LocalTime.now().isBefore(LocalTime.parse("12:00"))  || isDayAfterToday()))
            return "11:00AM-12:00PM";
        else if (slot == 4 && (LocalTime.now().isBefore(LocalTime.parse("13:00"))  || isDayAfterToday()))
            return "12:00PM-13:00PM";
        else if (slot == 5 && (LocalTime.now().isBefore(LocalTime.parse("14:00"))  || isDayAfterToday()))
            return "13:00PM-14:00PM";
        else if (slot == 6 && (LocalTime.now().isBefore(LocalTime.parse("15:00"))  || isDayAfterToday()))
            return "14:00PM-15:00PM";
        else if (slot == 7 && (LocalTime.now().isBefore(LocalTime.parse("16:00"))  || isDayAfterToday()))
            return "15:00PM-16:00PM";
        else if (slot == 8 && (LocalTime.now().isBefore(LocalTime.parse("17:00"))  || isDayAfterToday()))
            return "16:00PM-17:00PM";
        else if (slot == 9 && (LocalTime.now().isBefore(LocalTime.parse("18:00"))  || isDayAfterToday()))
            return "17:00PM-18:00PM";
        else if (slot == 10 && (LocalTime.now().isBefore(LocalTime.parse("19:00"))  || isDayAfterToday()))
            return "18:00PM-19:00PM";
        else if (slot == 11 && (LocalTime.now().isBefore(LocalTime.parse("20:00"))  || isDayAfterToday()))
            return "19:00PM-20:00PM";
        else if (slot == 12 && (LocalTime.now().isBefore(LocalTime.parse("21:00"))  || isDayAfterToday()))
            return "20:00PM-21:00PM";
        else if (slot == 13 && (LocalTime.now().isBefore(LocalTime.parse("22:00"))  || isDayAfterToday()))
            return "21:00PM-22:00PM";
        else
            return String.valueOf(R.string.closed);
    }

    public static boolean isDayAfterToday(){
        return LocalDate.now().getDayOfYear() < currentDate.get(Calendar.DAY_OF_YEAR);
    }

}