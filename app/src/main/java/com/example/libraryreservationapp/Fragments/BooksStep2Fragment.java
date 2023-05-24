package com.example.libraryreservationapp.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryreservationapp.Common.Common;
import com.example.libraryreservationapp.Common.BookItemDecoration;
import com.example.libraryreservationapp.Interface.TimeSlotLoadListener;
import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.TimeSlot;
import com.example.libraryreservationapp.BookTimeSlotAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BooksStep2Fragment extends Fragment implements TimeSlotLoadListener{
    DocumentReference bookDoc;
    TimeSlotLoadListener timeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    RecyclerView recycler_time_slot;
    HorizontalCalendarView calendarView;

    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, 0); // add current date
            loadAvailableTimeSlotOfBook(Common.currentBook.getIsbn(), simpleDateFormat.format(date.getTime()));
        }
    };

    private void loadAvailableTimeSlotOfBook(String bookIsbn, final String reservationDate) {
        dialog.show();


        bookDoc = FirebaseFirestore.getInstance().collection("books")
                .document(Common.currentBook.getBookId());

        //get info about book
        bookDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) //if book available
                    {
                        //get information of reservation
                        // if not created, return empty
                        CollectionReference date = FirebaseFirestore.getInstance().collection("books")
                                .document(Common.currentBook.getBookId()).collection(reservationDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty()) //if no reservations
                                    {
                                        timeSlotLoadListener.onTimeSlotLoadEmpty();
                                    } else //if there are reservations
                                    {
                                        List<TimeSlot> timeSlots = new ArrayList<>(); // list of time slots that are full
                                        for (QueryDocumentSnapshot document:task.getResult()) {

                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        }

                                        timeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                timeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });

                    }
                }

            }
        });
    }

    static BooksStep2Fragment instance;

    public static BooksStep2Fragment getInstance(){
        if (instance == null)
            instance = new BooksStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeSlotLoadListener = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_BOOK_TIME_SLOT));

        simpleDateFormat = new SimpleDateFormat("MM_dd_yyyy");

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView=  inflater.inflate(R.layout.fragment_books_step2, container, false);
        recycler_time_slot = itemView.findViewById(R.id.recycler_time_slot);
        calendarView = itemView.findViewById(R.id.calendarView);
        unbinder = ButterKnife.bind(this, itemView);

        init(itemView);

        return itemView;
    }

    private void init(View itemView) {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new BookItemDecoration(8));

        //Calendar
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 30); // 2 days after todays date

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(1).mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener((new HorizontalCalendarListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSelected(Calendar date, int position) {
                if (Common.currentBookDate.getTimeInMillis() != date.getTimeInMillis()){
                    Common.currentBookDate = date;
                    loadAvailableTimeSlotOfBook(Common.currentBook.getIsbn(), simpleDateFormat.format(date.getTime()));
                }
            }
        }));
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        BookTimeSlotAdapter adapter = new BookTimeSlotAdapter(getContext(), timeSlotList);
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        BookTimeSlotAdapter adapter = new BookTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }
}