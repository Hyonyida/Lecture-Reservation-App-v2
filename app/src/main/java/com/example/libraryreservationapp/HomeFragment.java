package com.example.libraryreservationapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeFragment extends Fragment implements CheckInCheckOutDialogFragment.CheckInCheckOutDialogListener{

    //private member variables
    private RecyclerView recyclerView, booksRecyclerView;
    private TextView textViewNoRooms, textViewNoBooks;
    private Button btnReserveRoom, btnReserveBook;
    private RoomReservationAdapter adapter;
    private FirebaseFirestore fStore;
    private FirebaseAuth auth;
    private String userID;
    private String roomDocID;
    private String bookDocID;
    private DocumentReference docRef;
    private DocumentReference bookResRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //sets the view of the fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        btnReserveRoom = v.findViewById(R.id.reserveRoom);
        textViewNoRooms = v.findViewById(R.id.textViewNoRoomReservations);

        btnReserveBook = v.findViewById(R.id.reserveBook);
        textViewNoBooks= v.findViewById(R.id.textViewNoBookReservations);

        //gets instance of firestore
        fStore = FirebaseFirestore.getInstance();
        // gets the recyclerView id for reference
        recyclerView = v.findViewById(R.id.recyclerViewReservations);
        booksRecyclerView = v.findViewById(R.id.recyclerViewBookReservations);
        //gets the instance of the firebase auth
        auth = FirebaseAuth.getInstance();
        //gets the logged in users user id
        userID = auth.getUid();


        btnReserveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for the fragments to be replaced when items are selected in the drawer
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //replaces the fragment with the home fragment
                ft.replace(R.id.fragment_container, new ReserveRoomFragment()).addToBackStack("parent").commit();
            }
        });

        //creates an alarm reciever that if an alarm is received, it will run checkForReservations
        BroadcastReceiver alarm_receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkForReservations();
            }
        };

        IntentFilter intentFilter = new IntentFilter("alarm_executed");

        getActivity().registerReceiver(alarm_receiver, intentFilter);


        return v;
    }

    public void setUpRecyclerView() {
        // creates a query that uses the collection reference to get the current reservations
        Query query = fStore.collection("users").document(userID).collection("currentReservations")
                .orderBy("date", Query.Direction.ASCENDING).orderBy("time", Query.Direction.ASCENDING)
                .orderBy("building", Query.Direction.ASCENDING).orderBy("roomNumber", Query.Direction.ASCENDING);

        Query booksQuery = fStore.collection("users").document(userID).collection("currentBookReservations")
                .orderBy("date", Query.Direction.ASCENDING).orderBy("time", Query.Direction.ASCENDING).orderBy("title", Query.Direction.ASCENDING);

        // creates configurations for the adapter and binds the query to the recyclerView
        // .setLifecycleOwner(this) allows for deletion of onStart and onStop overrides
        FirestoreRecyclerOptions<RoomReservationInformation> options = new FirestoreRecyclerOptions.Builder<RoomReservationInformation>().setQuery(query, RoomReservationInformation.class).setLifecycleOwner(this).build();

        // sets the adapter with the configurations that were just made
        adapter = new RoomReservationAdapter(options);

        // sets the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adds horizontal line between different items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // sets the adapter
        recyclerView.setAdapter(adapter);


        // sets the layout manager
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        booksRecyclerView.setLayoutManager(layoutManager2);
        booksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //adds horizontal line between different items
        booksRecyclerView.addItemDecoration(new DividerItemDecoration(booksRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // sets the adapter


        //listens on the roomReservation adapter
        adapter.setOnItemClickListener(new RoomReservationAdapter.RoomReservationAdapterListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //gets the documentID of the item clicked
                roomDocID = documentSnapshot.getReference().getId();
                //creates a reference to a specific document in the subcollection of the user
                docRef = fStore.collection("users").document(userID).collection("currentReservations").document(roomDocID);

                //gets the value of checkedIn
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            //gets the document
                            DocumentSnapshot document = task.getResult();

                            //gets the value of the time that the reservation is at
                            String time = document.getString("time");
                            String date = document.getString("date");
                            //gets the value from the database for that individual room that was clicked on
                            boolean checkedIn = document.getBoolean("checkedIn");

                            //if checkedIn is true show the dialog
                            if(checkedIn){
                                showDialog(checkedIn, 0);
                            }
                            //if checkedIn is false
                            else{
                                //gets the reservation calendar by calling translateTimeAndDate
                                Calendar reservationCal = translateTimeAndDate(time, date);

                                //value returned by compareTimes which compares the current time and the reservationTime
                                boolean compareTime = compareTimes(reservationCal);

                                //if the time is within the time frame of reservation and reservationPlus15
                                if(compareTime){
                                    //show the dialog
                                    showDialog(checkedIn, 0);
                                }
                                else{
                                    //Give a toast saying that you cannot checkin yet
                                    Toast.makeText(getContext(), "이용 가능한 시간이 아닙니다", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                        else{
                            Log.d("MYDEBUG", "Couldn't get value of checkedIn for that room");
                        }
                    }
                });

            }
        });
    }

    //what occurs when the fragment is resumed
    @Override
    public void onResume() {
        super.onResume();

        checkForReservations();
    }

    //shows the dialog
    public void showDialog(boolean checkedIn, int type){
        //if type = 0, its a room or if type = 1, its a book
        DialogFragment dialog = new CheckInCheckOutDialogFragment();
        // Supply type and checkedIn as arguments
        Bundle args = new Bundle();
        args.putBoolean("checkedIn", checkedIn);
        args.putInt("type", type);

        //sets the bundle to the dialog
        dialog.setArguments(args);
        //shows the dialog
        dialog.show(getChildFragmentManager(), "CheckInCheckOutDialogFragment");

    }

    //if the user clicked the positive dialog button
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, boolean checkedIn, int type) {
        //creates a hashmap with the data for the update
        Map<String, Object> update = new HashMap<>();
        update.put("checkedIn", checkedIn);

        //if type is a room
        if(type == 0){
            //updates the room reservation document that was checked into or out of
            docRef.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "이용 시작", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "아직 이용할 시간이 되지 않았습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if(!checkedIn){
                //get the review for the room
                getReview();
                //moves the room reservation
                moveReservation();
            }
        }

        //if type is a book
        if(type == 1){
            //updates the book reservation document that was checked into or out of
             bookResRef.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Couldn't check you in/out", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if(!checkedIn){
                //moves the book reservation
                moveBookReservation();
            }
        }

    }

    //if the user clicked the negative dialog button
    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //closes the dialog
        dialog.dismiss();
    }

    //overrides the fragment.onAttach() method to instantiate the listener
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        //sets the listener equal to the instance of the checkInCheckOutRoomDialogFragment
        if (fragment instanceof CheckInCheckOutDialogFragment) {
            ((CheckInCheckOutDialogFragment) fragment).listener = this;
        }

    }

    public void moveReservation(){

        //gets the room reservation
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //gets the document at the end of the reference
                    DocumentSnapshot document = task.getResult();
                    //checks if the document is null before proceeding
                    if(document!=null){
                        //adds the reservation to the past reservations collection for the user
                        fStore.collection("users").document(userID).collection("pastReservations").document(roomDocID).set(Objects.requireNonNull(document.getData())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //deletes the reservation from the current reservations collection
                                docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //calls the recycler view for it to be re-set up
                                            checkForReservations();
                                            Log.d("MYDEBUG", "Reservation successfully deleted");
                                        }
                                        else{
                                            Log.d("MYDEBUG", "Deletion of reservation failed");
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MYDEBUG", "Addition of room to the past reservations failed");
                            }
                        });
                    }
                }
                else{
                    Log.d("MYDEBUG", "Couldn't find the reservation in the database");
                }
            }
        });
    }

    public void moveBookReservation(){
        //gets the book reservation
        bookResRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //gets the document at the end of the reference
                    DocumentSnapshot document = task.getResult();
                    //checks if the document is null before proceeding
                    if(document!=null){
                        //adds the reservation to the past book reservations collection for the user
                        fStore.collection("users").document(userID).collection("pastBookReservations").document(bookDocID).set(Objects.requireNonNull(document.getData())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //deletes the reservation from the current book reservations collection
                                bookResRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //calls the recycler view for it to be re-set up
                                            checkForReservations();
                                            Log.d("MYDEBUG", "Book reservation successfully deleted");
                                        }
                                        else{
                                            Log.d("MYDEBUG", "Deletion of book reservation failed");
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("MYDEBUG", "Addition of book to the past reservations failed");
                            }
                        });

                    }
                }
                else{
                    Log.d("MYDEBUG", "Couldn't find the book reservation in the database");
                }
            }
        });
    }

    public void checkForReservations(){
        //checks to see if the logged in user has any upcoming room reservations
        fStore.collection("users").document(userID).collection("currentReservations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //if the query returns as empty
                if(queryDocumentSnapshots.isEmpty()){
                    //show some parts of the layout vs. others
                    recyclerView.setVisibility(View.GONE);
                    textViewNoRooms.setVisibility(View.VISIBLE);
                    btnReserveRoom.setVisibility(View.VISIBLE);
                }
                else{
                    //show some parts of the layout vs. others
                    recyclerView.setVisibility(View.VISIBLE);
                    textViewNoRooms.setVisibility(View.GONE);
                    btnReserveRoom.setVisibility(View.GONE);
                    //calls the recycler view for it to be set up
                    setUpRecyclerView();
                }
            }
        });

        //checks to see if the logged in user has any upcoming book reservations
        fStore.collection("users").document(userID).collection("currentBookReservations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //if the query returns as empty
                if(queryDocumentSnapshots.isEmpty()){
                    //show some parts of the layout vs. others
                    booksRecyclerView.setVisibility(View.GONE);
                    textViewNoBooks.setVisibility(View.VISIBLE);
                    btnReserveBook.setVisibility(View.VISIBLE);
                }
                else{
                    //show some parts of the layout vs. others
                    booksRecyclerView.setVisibility(View.VISIBLE);
                    textViewNoBooks.setVisibility(View.GONE);
                    btnReserveBook.setVisibility(View.GONE);
                    //calls the recycler view for it to be set up
                    setUpRecyclerView();
                }
            }
        });
    }

    public Calendar translateTimeAndDate(String time, String date){
        Calendar cal = Calendar.getInstance();

        time = time.replace("a", "AM");
        time = time.replace("p", "PM");

        //creates a pattern of the time and separates them into groups
        Pattern patternTime = Pattern.compile("([0-9]{1,2}):([0-9]{2})([A-Z]{2})(.*)");
        Matcher matcherTime = patternTime.matcher(time);

        if(matcherTime.find()){
            String hour = matcherTime.group(1);
            String mins = matcherTime.group(2);
            String period = matcherTime.group(3);

            //sets the calendar for the reservation time
            cal.set(Calendar.HOUR, Integer.parseInt(hour));
            cal.set(Calendar.MINUTE, Integer.parseInt(mins));

            if (period.equals("AM")) {
                cal.set(Calendar.AM_PM, Calendar.AM);
            } else {
                cal.set(Calendar.AM_PM, Calendar.PM);
            }
        }

        //creates a pattern of the date and separates them into groups
        Pattern patternDate = Pattern.compile("([0-9]{4})/(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])");
        Matcher matcherDate = patternDate.matcher(date);

        if(matcherDate.find()){
            String month = matcherDate.group(2);
            String day = matcherDate.group(3);
            String year = matcherDate.group(1);

            //sets the calendar for the reservation date
            cal.set(Calendar.MONTH, Integer.parseInt(month));
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DATE, Integer.parseInt(day));
            cal.set(Calendar.YEAR, Integer.parseInt(year));

        }

        //returns the reservation calendar
        return cal;
    }

    public boolean compareTimes(Calendar reservationCalendar){
        //gets the current system time
        Calendar currentCalendar = Calendar.getInstance();

        //creates a calendar 15 minutes after the reservation time
        Calendar reservationPlus15 = (Calendar) reservationCalendar.clone();
        reservationPlus15.add(Calendar.MINUTE, 15);

        //checks that the time is after the reservation time but before the 15 minutes past the reservation time
        if(currentCalendar.after(reservationCalendar)){
            return true;
        }
        else{
            return false;
        }

    }

    public void getReview(){
        //gets the room id of the room that was checked out of to pass into the rating activity
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    //gets the roomID of the room that was selected and checked out of
                    String roomID = documentSnapshot.getString("roomId");

                    //starts the roomReview activity
                    Intent intent = new Intent(getActivity(), RoomsRating.class);
                    intent.putExtra("roomID", roomID);
                    startActivity(intent);
                }
            }
        });
    }

}
