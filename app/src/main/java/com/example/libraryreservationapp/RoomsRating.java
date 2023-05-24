package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libraryreservationapp.R;
import com.example.libraryreservationapp.Ratings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomsRating extends AppCompatActivity {

    // .:::: Static and global variables declared ::::.

    private static final String KEY_REVIEW = "review";
    private static final String KEY_RATING = "rating";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "userid";

    // .:::: Fields from layout and firebase declared ::::.

    private TextView txtReviewRoom;
    private Button btnSubmitReview;
    private RatingBar ratingBar;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private String roomID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms_rating);

        Bundle extras = getIntent().getExtras();
        roomID = extras.getString("roomID");

        // Calling Firebase Database method:
        FirebaseDB();

        // Passing values:
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtReviewRoom = (TextView) findViewById(R.id.txtReviewRoom);
        btnSubmitReview = (Button) findViewById(R.id.btnSubmitReview);

        // Sets number of stars:
        ratingBar.setNumStars(5);
        // Sets default rating to 3.5 as float:
        ratingBar.setRating((float) 3.0);

        // Calls all the actions when button is clicked
        getReviews();

    }// END of onCreate + + + + + + + + + + + + + + + + + + + + + + +

    public void FirebaseDB(){

//                    .:::: Get Firebase Instance ::::.

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }// END of FirebaseDB method + + + + + + + + + + + +


    public void getReviews(){

//                     .:::: BUTTON SUBMIT ::::.

        // BUTTON When click to submit the review starts here:
        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // flags variable that will validate errors:
                int flags = 0;

//-----------------------------------------------------------------------------------------------------------------

//                .:::: Creates the room review text inputted by user ::::.

                String test_review = txtReviewRoom.getText().toString().trim();
                if(test_review.equals("")) {
                    flags++;
                    txtReviewRoom.setError("Please write a review");
                } else { String review  = txtReviewRoom.getText().toString().trim(); }

//-----------------------------------------------------------------------------------------------------------------

//                .::::: Rating Bar Section :::::.

                // Creates an int to get number os stars:
                int totalNumOfStars = ratingBar.getNumStars();
                // Creates float to get Rating Value:
                float RatedValue = ratingBar.getRating();

                // Toast message displaying picked ratings out of /5
                Toast.makeText(getApplicationContext(), "Your rating: " + RatedValue + "/" + totalNumOfStars, Toast.LENGTH_SHORT).show();

//-----------------------------------------------------------------------------------------------------------------

//                  .:::: Creating a review into DB ::::.

                // Gets the userid of the current user:
                String userID = mFirebaseAuth.getCurrentUser().getUid();
                // Gets the email of the current user:
                String email = mFirebaseAuth.getCurrentUser().getEmail();


                if(flags == 0) {
                    // Creates a hashmap to store reviews
                    Map<String, Object> roomReviews = new HashMap<>();
                    roomReviews.put(KEY_REVIEW , test_review);
                    roomReviews.put(KEY_RATING, RatedValue);
                    roomReviews.put(KEY_USER_ID, userID);
                    roomReviews.put(KEY_EMAIL, email);



                    // Adds to the database the new room with the hashmap
                    db.collection("room").document(roomID)
                            .collection("reviews").add(roomReviews)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(), "Add was successful!", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Add failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    //-----------------------------------------------------------------------------------------------------------------
                    // Clears section into blank fields after clicked:
                    txtReviewRoom.setText("");
                    //-----------------------------------------------------------------------------------------------------------------
                    // Closes the activity
                    finish();
                }
//-----------------------------------------------------------------------------------------------------------------
            }
        }); // END of btnSubmitReview   + + + + + + + + + + + + + + + + + + + +
    }// END of getReviews method  + + + + + + + + + + + + + + + + + + + + + + +
} // END of RoomsRating extends AppCompatActivity + + + + + + + + + + + + + + +