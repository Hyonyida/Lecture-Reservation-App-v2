package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateDeleteBookActivity extends AppCompatActivity {

    private Button updateBtn;
    private Button deleteBtn;
    private RadioGroup courseRadioGroup;
    private EditText bookEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private Switch availabilitySwitch;
    private FirebaseFirestore fStore;
    private RadioButton testRadioButton;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_book);


        updateBtn = findViewById(R.id.btnUpdateBook);
        deleteBtn = findViewById(R.id.btnDeleteBook);
        courseRadioGroup = findViewById(R.id.radioGroupCoursesUpdate);
        bookEditText = findViewById(R.id.editBookUpdate);
        authorEditText = findViewById(R.id.editAuthorUpdate);
        isbnEditText = findViewById(R.id.editISBNUpdate);
        availabilitySwitch = findViewById(R.id.switchAvailabilityUpdate);
        testRadioButton = findViewById(R.id.radioButtonMovieUpdate);

        // gets firestore instance
        fStore = FirebaseFirestore.getInstance();

        //gets the document ID of the item clicked on from the intent
        Intent intent = getIntent();
        String documentID = intent.getStringExtra("docID");

        //creates a reference to a specific document in the collection
        documentReference = fStore.collection("books").document(documentID);

        //retrieves the data from firestore for the room that is selected
        getDataFromFirestore();


        //Update Book on click listener
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calls the method that updates firestore
                updateBook();
            }
        });


        //Delete Book on click listener
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calls the method that updates firestore
                deleteBook();
            }
        });


    }

    public void updateBook() {

        int flags = 0;
        String course = "", title = "", author = "", isbn = "", combo = "";

        //clears the errors to ensure there is no false error
        bookEditText.setError(null);
        authorEditText.setError(null);
        isbnEditText.setError(null);
        testRadioButton.setError(null);

        //gets the title from the edit text
        String test_title = bookEditText.getText().toString();
        //checks to see if it is an empty edit text
        if(test_title.equals("")){
            //adds a flag and an error message
            flags++;
            bookEditText.setError("Please enter a book title");
        }
        else{
            title = bookEditText.getText().toString();
        }

        //gets the author from the edit text
        String test_author = authorEditText.getText().toString();
        //checks to see if it is an empty edit text
        if(test_author.equals("")){
            //adds a flag and an error message
            flags++;
            authorEditText.setError("Please enter a author");
        }
        else{
            author = authorEditText.getText().toString();
        }

        //gets the isbn from the edit text
        String test_isbn = isbnEditText.getText().toString();
        //checks to see if it is an empty edit text
        if(test_isbn.equals("")){
            //adds a flag and an error message
            flags++;
            authorEditText.setError("please enter a isbn");
        }
        else{
            isbn = isbnEditText.getText().toString();
        }

        boolean availability = availabilitySwitch.isChecked();

        //gets the selected radiobutton
        int selectedCourse = courseRadioGroup.getCheckedRadioButtonId();
        //checks to make sure a radio button was selected
        if (selectedCourse == -1) {
            flags++;
            testRadioButton.setError("Please choose a course");
        } else {
            //switches the selected courses id to equal the intended value of that course
            switch (selectedCourse) {
                case R.id.radioButtonUnixUpdate:
                    course = getString(R.string.unix);
                    break;
                case R.id.radioButtonCUpdate:
                    course = getString(R.string.Cplus);
                    break;
                case R.id.radioButtonJavaUpdate:
                    course = getString(R.string.java);
                    break;
                case R.id.radioButtonHistoryUpdate:
                    course = getString(R.string.history);
                    break;
                case R.id.radioButtonEconomicsUpdate:
                    course = getString(R.string.economics);
                    break;
                case R.id.radioButtonEnglishUpdate:
                    course = getString(R.string.english);
                    break;
                case R.id.radioButtonRamUpdate:
                    course = getString(R.string.RAM);
                    break;
                case R.id.radioButtonScienceUpdate:
                    course = getString(R.string.science);
                    break;
                case R.id.radioButtonMovieUpdate:
                    course = getString(R.string.movie);
                    break;
                case R.id.radioButtonMathUpdate:
                    course = getString(R.string.math);
                    break;
            }
        }

        //creates a combo of what the recycler view should be ordered by to allow for ordering by multiple fields
        combo = course + " " + title;

        //checks to see if there are any errors
        if (flags == 0) {
            Map<String, Object> bookInfo = new HashMap<>();
            bookInfo.put("course", course);
            bookInfo.put("title", title);
            bookInfo.put("author", author);
            bookInfo.put("isbn", isbn);
            bookInfo.put("combo", combo);
            bookInfo.put("availability", availability);

            //updates the database
            documentReference.update(bookInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Update was successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // closes the activity
            finish();
        }

    }




    // deletes the document from the collection
    public void deleteBook() {
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Delete was successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Delete failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        finish();
    }


    //Pulls  data from firestore to display information about the course
    public void getDataFromFirestore() {

        //gets the document for the specific book that was selected
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //gets the document
                    DocumentSnapshot document = task.getResult();
                    // gets the title of the book
                    String title = document.getString("title");
                    // sets the title to its appropriate editText
                    bookEditText.setText(title);
                    // gets author of the book
                    String author = document.getString("author");
                    // sets the author to its appropriate editText
                    authorEditText.setText(author);
                    // gets the isbn of the book
                    String isbn = document.getString("isbn");
                    // sets the isbnto its appropriate editText
                    isbnEditText.setText(isbn);
                    // gets the availability value from database
                    boolean availability = document.getBoolean("availability");
                    // sets the value for the switch
                    availabilitySwitch.setChecked(availability);


                    //gets the course value from the database
                    String course = document.getString("course").trim();

                    //selects the correct radioButton to check depending on if it matches the one from the database

                    if (course.equals(getString(R.string.unix))) {
                        courseRadioGroup.check(R.id.radioButtonUnixUpdate);
                    }
                    if (course.equals(getString(R.string.Cplus))) {
                        courseRadioGroup.check(R.id.radioButtonCUpdate);
                    }
                    if (course.equals(getString(R.string.java))) {
                        courseRadioGroup.check(R.id.radioButtonJavaUpdate);
                    }
                    if (course.equals(getString(R.string.history))) {
                        courseRadioGroup.check(R.id.radioButtonHistoryUpdate);
                    }
                    if (course.equals(getString(R.string.economics))) {
                        courseRadioGroup.check(R.id.radioButtonEconomicsUpdate);
                    }
                    if (course.equals(getString(R.string.english))) {
                        courseRadioGroup.check(R.id.radioButtonEnglishUpdate);
                    }
                    if (course.equals(getString(R.string.RAM))) {
                        courseRadioGroup.check(R.id.radioButtonRamUpdate);
                    }
                    if (course.equals(getString(R.string.science))) {
                        courseRadioGroup.check(R.id.radioButtonScienceUpdate);
                    }
                    if (course.equals(getString(R.string.movie))) {
                        courseRadioGroup.check(R.id.radioButtonMovieUpdate);
                    }
                    if (course.equals(getString(R.string.math))) {
                        courseRadioGroup.check(R.id.radioButtonMathUpdate);
                    }

                } else {
                    Log.d("MYDEBUG", "Error getting document values");
                }
            }
        });
    }
}
