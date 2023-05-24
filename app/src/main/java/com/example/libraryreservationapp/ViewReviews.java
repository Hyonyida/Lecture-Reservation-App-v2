package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Map;

public class ViewReviews extends AppCompatActivity {

    // .::::: Static and global variables declared (NOT USING THEM) :::::.
    private static final String TAG = "ViewReviews";
    private static final String KEY_REVIEW = "review";
    private static final String KEY_RATING = "rating";
    private static final String EMAIL = "email";

//  .::::: Variables ::::::.
    private TextView textViewData;
    private FirebaseAuth mFirebaseAuth;
    private Toolbar toolbar;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference notebookRef = db.collection("room/7lVHHvffUwU4PWgyhuB1/reviews");
    private String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

        toolbar = findViewById(R.id.toolbarViewReviews);
        //supports the toolbar that is defined in the layout for the AdminHomeActivity
        setSupportActionBar(toolbar);

//      .::::: get authentication from database :::::.
        mFirebaseAuth = FirebaseAuth.getInstance();
//      .::::: Creates a reference to a specific document in the collection :::::...
        textViewData = findViewById(R.id.text_view_data);
//      .::::: Call loadNotes method :::::.
        loadNotes();
    }// + + + + + END of onCreate + + + + +

    private void loadNotes(){

//              .::::: get information from database :::::.
        db.collection("room").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot room : queryDocumentSnapshots)
                {
                    String docID = room.getId();
                    Log.d("MYDEBUG","In the outer for loop "+ docID);
                    final String building = room.getString("building");
                    final String roomNumber = String.valueOf(room.getLong("roomNumber"));
                    db.collection("room").document(docID).collection("reviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){

                                for(QueryDocumentSnapshot reviewDoc : task.getResult()){
                                    String doc = reviewDoc.getId();
                                    Log.d("MYDEBUG","In the inner for loop "+ doc);

//                 .::::: Create Object from RoomReviews.java file :::::.
                                    RoomReviews note = reviewDoc.toObject(RoomReviews.class);
                                    float rating = note.getRating();
                                    String review = note.getReview();
                                    String email = note.getEmail();
                                    data += building + " Room Number: " + roomNumber
                                            + "\nRating: \t" + rating + " Stars"
                                            + "\nE-mail: \t" + email + "\nReview: \t" + review + "\n\n";
//              .::::: Display the results into Nested Scroll View :::::.
                                    textViewData.setText(data);
                                }
                            }
                            else{
                                Log.d("MYDEBUG", "Couldn't access the inner loop to get the reviews");
                            }
                        }
                    });
                }


            }
        });
    }// + + + + + END of loadNotes method() + + + + +

    //inflates the menu and toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_to_home_only, menu);
        return true;
    }

    //selects the proper idea when an item is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //converts the selected menu item to do the proper activity
        switch(item.getItemId()){
            case R.id.menuItemToHomeOnlyHome:
                //Starts AdminHomeActivity if the button is clicked
                Intent intToHome = new Intent(ViewReviews.this, AdminHomeActivity.class);
                startActivity(intToHome);
                return true;
            case R.id.menuItemToHomeOnlyLogout:
                //signs out user
                FirebaseAuth.getInstance().signOut();
                //Starts LoginActivity if this button is clicked
                Intent intToLogin = new Intent(ViewReviews.this, LoginActivity.class);
                startActivity(intToLogin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}// + + + + + END of ViewReviews extends AppCompatActivity + + + + +










