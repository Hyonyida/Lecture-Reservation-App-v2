package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {

    private TextView notification;
    private EditText edBooksName;
    private EditText edCourseName;
    private EditText edIsbn;
    private EditText edQuantity;
    private EditText edStatus;
    private FirebaseFirestore db;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // method getMessage called:
        getMessage();

        // gets firestore instance
        db = FirebaseFirestore.getInstance();
        //gets the document ID of the item clicked on from the intent
        Intent intent = getIntent();
        String documentID = intent.getStringExtra("docID");

        //creates a reference to a specific document in the collection
        documentReference = db.collection("requests").document(documentID);
        // method getMessage called:
        getDataFromDb();

    }// END of onCreate Method + + + + + + + + + + + + + + + + + + + + +

    private void getMessage()
    {
        notification = findViewById(R.id.text_view_notification);

        String message = getIntent().getStringExtra("message");
        notification.setText(message);
    }// END of getMessage Method + + + + + + + + + + + + + + + + + + + + +


    private void getDataFromDb()
    {

        edBooksName = (EditText)findViewById(R.id.edit_bookName);
        edCourseName = (EditText)findViewById(R.id.edit_courseName);
        edIsbn = (EditText)findViewById(R.id.edit_isbn);
        edQuantity = (EditText)findViewById(R.id.edit_quantity);
        edStatus = (EditText)findViewById(R.id.edit_status);

        db = FirebaseFirestore.getInstance();

        //gets the document for the specific book request
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    //gets the document
                    DocumentSnapshot docRef = task.getResult();

                  // gets the title of the book requested
                  String title = docRef.getString("title");
                  // sets the title to its appropriate editText
                  edBooksName.setText(title);

                  // gets author of the book requested
                  String className = docRef.getString("course");
                  // sets the author to its appropriate editText
                  edCourseName.setText(className);

                  // gets the isbn of the book requested
                  String isbn = docRef.getString("isbn");
                  // sets the isbn to its appropriate editText
                  edIsbn.setText(isbn);

                  // gets the quantity of the book requested
                  String Quantity = docRef.getString("quantity");
                  // sets the quantity to its appropriate editText
                  edQuantity.setText(Quantity);

                  //gets the status of the book request
                  String Status = docRef.getString("status");
                  // sets the status to its appropriate editText
                  edStatus.setText(Status);

              } else  {
                  Log.d("MYDEBUG", "Error getting document values");
              }
          }
      });
    }
}   // END of NotificationActivity extends AppCompatActivity


