package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddBookActivity extends AppCompatActivity
{
    //private member variables
    private Button addBookBtn;
    private RadioGroup courseRadioGroup;
    private EditText bookEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private FirebaseFirestore fStore;
    private RadioButton testRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        //gets the references from the layout for the variables
        fStore = FirebaseFirestore.getInstance();
        addBookBtn = findViewById(R.id.btnAddBook);
        courseRadioGroup = findViewById(R.id.radioGroupCourses);
        bookEditText = findViewById(R.id.editBook);
        authorEditText = findViewById(R.id.editAuthor);
        isbnEditText = findViewById(R.id.editISBN);
        testRadioButton = findViewById(R.id.radioButtonC);

        //add book on click listener
        addBookBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //calls to gather the data
                gatherData();
            }
        });

    }

    //gathers data from the form to be put into database
    public void gatherData(){
        int flags = 0;
        String course ="", title = "", author = "", isbn ="", combo = "";

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
            title = bookEditText.getText().toString().trim();
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
            author = authorEditText.getText().toString().trim();
        }

        //gets the isbn from the edit text
        String test_isbn = isbnEditText.getText().toString().trim();
        //checks to see if it is an empty edit text
        if(test_isbn.equals("")){
            if (test_isbn.length() <= 0 || test_isbn.length() >13) {
                //adds a flag and an error message
                flags++;
                isbnEditText.setError("Please enter a isbn");
            }
        }
        else{
            isbn = isbnEditText.getText().toString().trim();
        }

        //gets the selected radiobutton
        int selectedCourse = courseRadioGroup.getCheckedRadioButtonId();
        //checks to make sure a radio button was selected
        if(selectedCourse == -1){
            //adds a flag and an error message
            flags++;
            testRadioButton.setError("Please choose a course");
        }
        else{
            //switches the selected course id to the proper string of the course
            switch (selectedCourse){
                case R.id.radioButtonUnix:
                    course = getString(R.string.unix);
                    break;
                case R.id.radioButtonC:
                    course = getString(R.string.Cplus);
                    break;
                case R.id.radioButtonJava:
                    course = getString(R.string.java);
                    break;
                case R.id.radioButtonHistory:
                    course = getString(R.string.history);
                    break;
                case R.id.radioButtonEconomics:
                    course = getString(R.string.economics);
                    break;
                case R.id.radioButtonEnglish:
                    course = getString(R.string.english);
                    break;
                case R.id.radioButtonRam:
                    course = getString(R.string.RAM);
                    break;
                case R.id.radioButtonScience:
                    course = getString(R.string.science);
                    break;
                case R.id.radioButtonMovie:
                    course = getString(R.string.movie);
                    break;
                case R.id.radioButtonMath:
                    course = getString(R.string.math);
                    break;
            }
        }

        //creates a combo of what the recycler view should be ordered by to allow for ordering by multiple fields
        combo = course + " " + title;

        //checks to see if there are any flags
        if(flags == 0){
            //creates a hashmap to store all the data for the book
            Map<String, Object> bookInfo = new HashMap<>();
            bookInfo.put("course", course);
            bookInfo.put("title", title);
            bookInfo.put("author", author);
            bookInfo.put("isbn", isbn);
            bookInfo.put("combo", combo);

            //adds to the database the new book with the hashmap
            fStore.collection("books").add(bookInfo).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Add was successful!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Add failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //closes the activity
            finish();
        }
    }

}





