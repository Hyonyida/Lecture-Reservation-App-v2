package com.example.libraryreservationapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlertReceiver_DeleteReservation extends BroadcastReceiver {
    //private member variables
    private FirebaseFirestore db;
    private String userID;
    private String roomID;
    private String reservationID;
    private String timeSlot;
    private String date;

    @Override
    public void onReceive(final Context context, Intent intent) {
        //gets the extra information that was passed in
        userID = intent.getStringExtra("userID");
        reservationID = intent.getStringExtra("reservationID");
        roomID = intent.getStringExtra("roomID");
        timeSlot = intent.getStringExtra("timeSlot");
        date = intent.getStringExtra("date");

        db = FirebaseFirestore.getInstance();

        //gets the document in the users current reservation collection of the reservation that is being tested
        db.collection("users").document(userID).collection("currentReservations").document(reservationID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    //gets the document
                    DocumentSnapshot document = task.getResult();
                    //gets value of the reservations checkedIn field
                    boolean checkedIn = document.getBoolean("checkedIn");

                    //if the user is not checkedIn when this alarm goes off
                    if (!checkedIn) {
                        //deletes the reservation from the users collection
                        db.collection("users").document(userID).collection("currentReservations").document(reservationID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("MYDEBUG", "Successfully deleted the room that was not checked into from the users reservations");
                                } else {
                                    Log.d("MYDEBUG", "Failed to delete the room that was not checked into from the users reservations");
                                }

                            }
                        });

                        //deletes the reservation from the books collection
                        db.collection("room").document(roomID).collection(date).document(timeSlot).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d("MYDEBUG", "Successfully deleted the room that was not checked into from the room reservations");
                                }
                                else{
                                    Log.d("MYDEBUG", "Failed to delete the room that was not checked into from the room reservations");

                                }
                            }
                        });

                        //sends a broadcast to HomeFragment that will refresh the fragment and the recyclerViews to display the correct layout
                        Intent intent = new Intent("alarm_executed");
                        context.sendBroadcast(intent);

                    }
                    else{
                        Log.d("MYDEBUG", "User was checked in, so did not remove it");
                    }

                }
                else{
                    Log.d("MYDEBUG", "Couldn't retrieve document");
                }
            }
        });

    }
}
