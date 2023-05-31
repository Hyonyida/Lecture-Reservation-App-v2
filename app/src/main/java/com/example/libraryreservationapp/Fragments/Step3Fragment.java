package com.example.libraryreservationapp.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.libraryreservationapp.AlertReceiver_DeleteReservation;
import com.example.libraryreservationapp.AlertReceiver_ehez;
import com.example.libraryreservationapp.Common.Common;
import com.example.libraryreservationapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Step3Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    TextView txt_reservation_date_text, txt_reservation_time_text, txt_reservation_building_text, txt_reservation_room_text, txt_reservation_capacity_text;
    Button btn_confirm;
    ImageView img_computer, img_wifi, img_engineering;
    FirebaseFirestore fStore;
    Context mContext;
    String reservationID;

    BroadcastReceiver confirmReservationReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            setData();
        }
    };


    private void setData() {

        resetVectorImages();

        txt_reservation_building_text.setText(Common.currentRoom.getBuilding());
        txt_reservation_room_text.setText("강의실 : "+Common.currentRoom.getRoomNumber());
        txt_reservation_capacity_text.setText("수용 인원 : "+Common.currentRoom.getCapacity());
        txt_reservation_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot)));
        txt_reservation_date_text.setText(simpleDateFormat.format(Common.currentDate.getTime()));

        if(Common.currentRoom.isComputer())
            img_computer.setColorFilter(getResources().getColor(R.color.available));
        if(Common.currentRoom.isWifi())
            img_wifi.setColorFilter(getResources().getColor(R.color.available));
        if(Common.currentRoom.isEngineering())
            img_engineering.setColorFilter(getResources().getColor(R.color.available));

    }

    static Step3Fragment instance;

    public static Step3Fragment getInstance(){
        if (instance == null)
            instance = new Step3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //apply format to date display
        simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmReservationReceiver, new IntentFilter(Common.KEY_CONFIRM_RESERVATION));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmReservationReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_step3, container, false);

        txt_reservation_date_text = itemView.findViewById(R.id.txt_reservation_date_text);
        txt_reservation_time_text = itemView.findViewById(R.id.txt_reservation_time_text);
        txt_reservation_building_text = itemView.findViewById(R.id.txt_reservation_building_text);
        txt_reservation_room_text = itemView.findViewById(R.id.txt_reservation_room_text);
        txt_reservation_capacity_text = itemView.findViewById(R.id.txt_reservation_capacity_text);

        img_computer = itemView.findViewById(R.id.img_computer);
        img_wifi = itemView.findViewById(R.id.img_wifi);
        img_engineering = itemView.findViewById(R.id.img_engineering);

        btn_confirm = itemView.findViewById(R.id.btn_confirm);

        fStore = FirebaseFirestore.getInstance();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                //creates a hashmap to store all the data for the room reservation to put into the specific users collection of reservations
                Map<String, Object> info = new HashMap<>();
                info.put("building", Common.currentRoom.getBuilding());
                info.put("roomNumber", String.valueOf(Common.currentRoom.getRoomNumber()));
                info.put("date", simpleDateFormat.format(Common.currentDate.getTime()));
                info.put("time", Common.convertTimeSlotToString(Common.currentTimeSlot));
                info.put("roomId", Common.currentRoom.getRoomId());
                info.put("checkedIn", false);

                //adds the reservation to the users collection
                fStore.collection("users").document(Common.userID).collection("currentReservations").add(info).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        //gets the id of the document that was just created
                        reservationID = documentReference.getId();

                        Map<String, Object> reservationInfo = new HashMap<>();
                        reservationInfo.put("building", Common.currentRoom.getBuilding());
                        reservationInfo.put("roomNumber", String.valueOf(Common.currentRoom.getRoomNumber()));
                        reservationInfo.put("roomId", Common.currentRoom.getRoomId());
                        reservationInfo.put("time", Common.convertTimeSlotToString(Common.currentTimeSlot));
                        reservationInfo.put("date", simpleDateFormat.format(Common.currentDate.getTime()));
                        reservationInfo.put("slot", Long.valueOf(Common.currentTimeSlot));
                        reservationInfo.put("userId", Common.userID);
                        reservationInfo.put("reservationId", reservationID);

                        //submit to room document
                        DocumentReference reservationDate = fStore.collection("room")
                                .document(Common.currentRoom.getRoomId()).collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
                                .document(String.valueOf(Common.currentTimeSlot));

                        //Write reservation data to Firestore
                        reservationDate.set(reservationInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //creates a calendar instance
                                Calendar cal = Calendar.getInstance();

                                //converts the reservation time to a string
                                String timeSlot = Common.convertTimeSlotToString(Common.currentTimeSlot);
                                timeSlot = timeSlot.replace("a", "AM");
                                timeSlot = timeSlot.replace("p", "PM");

                                //creates a pattern of the time and separates them into groups
                                Pattern pattern = Pattern.compile("([0-9]{1,2}):([0-9]{2})([A-Z]{2})(.*)");
                                Matcher matcher = pattern.matcher(timeSlot);

                                if(matcher.find()){
                                    String hour = matcher.group(1);
                                    String mins = matcher.group(2);
                                    String period = matcher.group(3);

                                    //sets the calendar information for the reservation
                                    cal.set(Calendar.HOUR, Integer.parseInt(hour));
                                    cal.set(Calendar.MINUTE, Integer.parseInt(mins));
                                    cal.add(Calendar.MINUTE, -60);
                                    if (period.equals("AM")) {
                                        cal.set(Calendar.AM_PM, Calendar.AM);
                                    } else {
                                        cal.set(Calendar.AM_PM, Calendar.PM);
                                    }
                                }

                                cal.set(Calendar.MONTH, Common.currentDate.get(Calendar.MONTH));
                                cal.set(Calendar.DATE, Common.currentDate.get(Calendar.DATE));
                                cal.set(Calendar.YEAR, Common.currentDate.get(Calendar.YEAR));

                                //clones the reservation so that 15 minutes can be added to it
                                Calendar reservationPlus15 = (Calendar) cal.clone();
                                reservationPlus15.add(Calendar.MINUTE, 75);

                                //gets the current time when the confirm button is clicked
                                Calendar currentCal = Calendar.getInstance();
                                //if the current time is 15 minutes after the reservation time
                                if(currentCal.after(reservationPlus15)){
                                    //creates a calendar for the end time of the reservation
                                    Calendar reservationEnd = (Calendar) cal.clone();
                                    reservationEnd.add(Calendar.MINUTE, 90);

                                    //uses the end time of the reservation for the time for the alarm
                                    startAlarmForDelete(reservationEnd, reservationID);
                                }
                                else{
                                    //calls to start the alarm for the deletion of the reservation with the calendar of 15 minutes past the reservation time
                                    startAlarmForDelete(reservationPlus15, reservationID);
                                }

                                //calls to start the alarm for the warning of an upcoming reservation in an hour
                                startAlarm(cal);

                                resetStaticData();

                                //pops the backstack to return to the homescreen
                                getActivity().getSupportFragmentManager().popBackStack();
                                Toast.makeText(getContext(), "예약 되었습니다", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }

        });

        unbinder = ButterKnife.bind(this, itemView);

        return itemView;
    }

    private void startAlarm(Calendar cal) {
        //creates the alarm
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlertReceiver_ehez.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1, intent, 0);

        //sets the alarm time to be the exact time of an hour before the reservation time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private void startAlarmForDelete(Calendar cal, String reservationID){
        //gets the value of day, month, and year of calendar passed in (reservationPlus15)
        int monthNum = cal.get(Calendar.MONTH) + 1;
        int dayNum = cal.get(Calendar.DATE);
        String year = String.valueOf(cal.get(Calendar.YEAR));

        //formats the values to display leading 0's if necessary
        String day = String.format("%02d", dayNum);
        String month = String.format("%02d", monthNum);

        //creates the string of how the date is stored in the reservation part of the system
        String date = year + "_" + month + "_" + day;

        String timeSlot = String.valueOf(Common.currentTimeSlot);
        String userID = Common.userID;
        String roomID = Common.currentRoom.getRoomId();

        //creates the alarm
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlertReceiver_DeleteReservation.class);

        //puts in the extra information that is needed to be passed in
        intent.putExtra("userID", userID);
        intent.putExtra("roomID", roomID);
        intent.putExtra("reservationID", reservationID);
        intent.putExtra("timeSlot", timeSlot);
        intent.putExtra("date", date);

        Random rand = new Random();
        int num1 = rand.nextInt(10000);
        int num2 = rand.nextInt(10000);
        int requestCode = num1 + num2;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent, 0);

        //sets the alarm time to be the exact time of the reservationPlus15 time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentRoom = null;
        Common.currentDate.add(Calendar.DATE, 0); //current date

    }

    private void resetVectorImages(){
        img_computer.setColorFilter(getResources().getColor(R.color.colorButton));
        img_wifi.setColorFilter(getResources().getColor(R.color.colorButton));
        img_engineering.setColorFilter(getResources().getColor(R.color.colorButton));
    }
}
