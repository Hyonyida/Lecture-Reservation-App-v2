package com.example.libraryreservationapp.Common;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.libraryreservationapp.Book;
import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.Room;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;


public class Common {

    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_DISPLAY_BOOK_TIME_SLOT = "DISPLAY_BOOK_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_BOOK_STEP = "BOOK_STEP";
    public static final String KEY_ROOM_SELECTED = "ROOM_SELECTED";
    public static final String KEY_BOOK_SELECTED = "BOOK_SELECTED";
    public static final String KEY_ENABLE_BUTTON_NEXT = "KEY_ENABLE_BUTTON_NEXT";
    public static final String KEY_ENABLE_BOOK_BUTTON_NEXT = "KEY_ENABLE_BOOK_BUTTON_NEXT";
    public static final int TIME_SLOT_TOTAL = 20;
    public static final int BOOK_TIME_SLOT_TOTAL = 20;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final Object BOOK_DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_BOOK_TIME_SLOT = "BOOK_TIME_SLOT";
    public static final String KEY_CONFIRM_RESERVATION = "CONFIRM_RESERVATION";
    public static final String KEY_CONFIRM_BOOK_RESERVATION = "CONFIRM_BOOK_RESERVATION";



    public static int step = 0; // first step is 0
    public static int bookStep = 0;
    public static Room currentRoom;
    public static Book currentBook;
    public static int currentTimeSlot = -1;
    public static int currentBookTimeSlot =-1;
    public static Calendar currentDate = Calendar.getInstance();
    public static Calendar currentBookDate = Calendar.getInstance();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM_dd_yyyy"); // only use it when needed for key
    public static String userID;



    public static String convertTimeSlotToString(int slot) {
        if (slot == 0 && (LocalTime.now().isBefore(LocalTime.parse("09:00"))  || isDayAfterToday()))
            return "8:00AM - 9:00AM";
        else if (slot == 1 && (LocalTime.now().isBefore(LocalTime.parse("10:00"))  || isDayAfterToday()))
            return "9:00AM - 10:00AM";
        else if (slot == 2 && (LocalTime.now().isBefore(LocalTime.parse("11:00"))  || isDayAfterToday()))
            return "10:00AM - 11:00AM";
        else if (slot == 3 && (LocalTime.now().isBefore(LocalTime.parse("12:00"))  || isDayAfterToday()))
            return "11:00PM - 12:00PM";
        else if (slot == 4 && (LocalTime.now().isBefore(LocalTime.parse("13:00"))  || isDayAfterToday()))
            return "12:00PM - 13:00PM";
        else if (slot == 5 && (LocalTime.now().isBefore(LocalTime.parse("14:00"))  || isDayAfterToday()))
            return "13:00PM - 14:00PM";
        else if (slot == 6 && (LocalTime.now().isBefore(LocalTime.parse("15:00"))  || isDayAfterToday()))
            return "14:00PM - 15:00PM";
        else if (slot == 7 && (LocalTime.now().isBefore(LocalTime.parse("16:00"))  || isDayAfterToday()))
            return "15:00PM - 16:00PM";
        else if (slot == 8 && (LocalTime.now().isBefore(LocalTime.parse("17:00"))  || isDayAfterToday()))
            return "16:00PM - 17:00PM";
        else if (slot == 9 && (LocalTime.now().isBefore(LocalTime.parse("18:00"))  || isDayAfterToday()))
            return "17:00PM - 18:00PM";
        else if (slot == 10 && (LocalTime.now().isBefore(LocalTime.parse("19:00"))  || isDayAfterToday()))
            return "18:00PM - 19:00PM";
        else if (slot == 11 && (LocalTime.now().isBefore(LocalTime.parse("20:00"))  || isDayAfterToday()))
            return "19:00PM - 20:00PM";
        else if (slot == 12 && (LocalTime.now().isBefore(LocalTime.parse("21:00"))  || isDayAfterToday()))
            return "20:00PM - 21:00PM";
        else if (slot == 13 && (LocalTime.now().isBefore(LocalTime.parse("22:00"))  || isDayAfterToday()))
            return "21:00PM - 22:00PM";
        else
            return String.valueOf(R.string.closed);
    }

    public static String convertTimeSlotToStringBook(int slot) {
        if (slot == 0 && (LocalTime.now().isBefore(LocalTime.parse("09:30"))  || isDayAfterTodayBook()))
            return "9:00a-9:30a";
        else if (slot == 1 && (LocalTime.now().isBefore(LocalTime.parse("10:00"))  || isDayAfterTodayBook()))
            return "9:30a-10:00a";
        else if (slot == 2 && (LocalTime.now().isBefore(LocalTime.parse("10:30"))  || isDayAfterTodayBook()))
            return "10:00a-10:30a";
        else if (slot == 3 && (LocalTime.now().isBefore(LocalTime.parse("11:00"))  || isDayAfterTodayBook()))
            return "10:30a-11:00a";
        else if (slot == 4 && (LocalTime.now().isBefore(LocalTime.parse("11:30"))  || isDayAfterTodayBook()))
            return "11:00a-11:30a";
        else if (slot == 5 && (LocalTime.now().isBefore(LocalTime.parse("12:00"))  || isDayAfterTodayBook()))
            return "11:30a-12:00p";
        else if (slot == 6 && (LocalTime.now().isBefore(LocalTime.parse("12:30"))  || isDayAfterTodayBook()))
            return "12:00p-12:30p";
        else if (slot == 7 && (LocalTime.now().isBefore(LocalTime.parse("13:00"))  || isDayAfterTodayBook()))
            return "12:30p-1:00p";
        else if (slot == 8 && (LocalTime.now().isBefore(LocalTime.parse("13:30"))  || isDayAfterTodayBook()))
            return "1:00p-1:30p";
        else if (slot == 9 && (LocalTime.now().isBefore(LocalTime.parse("14:00"))  || isDayAfterTodayBook()))
            return "1:30p-2:00p";
        else if (slot == 10 && (LocalTime.now().isBefore(LocalTime.parse("14:30"))  || isDayAfterTodayBook()))
            return "2:00p-2:30p";
        else if (slot == 11 && (LocalTime.now().isBefore(LocalTime.parse("15:00"))  || isDayAfterTodayBook()))
            return "2:30p-3:00p";
        else if (slot == 12 && (LocalTime.now().isBefore(LocalTime.parse("15:30"))  || isDayAfterTodayBook()))
            return "3:00p-3:30p";
        else if (slot == 13 && (LocalTime.now().isBefore(LocalTime.parse("16:00"))  || isDayAfterTodayBook()))
            return "3:30p-4:00p";
        else if (slot == 14 && (LocalTime.now().isBefore(LocalTime.parse("16:30"))  || isDayAfterTodayBook()))
            return "4:00p-4:30p";
        else if (slot == 15 && (LocalTime.now().isBefore(LocalTime.parse("17:00"))  || isDayAfterTodayBook()))
            return "4:30p-5:00p";
        else if (slot == 16 && (LocalTime.now().isBefore(LocalTime.parse("17:30"))  || isDayAfterTodayBook()))
            return "5:00p-5:30p";
        else if (slot == 17 && (LocalTime.now().isBefore(LocalTime.parse("18:00"))  || isDayAfterTodayBook()))
            return "5:30p-6:00p";
        else if (slot == 18 && (LocalTime.now().isBefore(LocalTime.parse("18:30"))  || isDayAfterTodayBook()))
            return "6:00p-6:30p";
        else if (slot == 19 && (LocalTime.now().isBefore(LocalTime.parse("19:00"))  || isDayAfterTodayBook()))
            return "6:30p-7:00p";
        else
            return String.valueOf(R.string.closed);
    }



    public static boolean isDayAfterToday(){
        return LocalDate.now().getDayOfYear() < currentDate.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isDayAfterTodayBook(){
        return LocalDate.now().getDayOfYear() < currentBookDate.get(Calendar.DAY_OF_YEAR);
    }
}