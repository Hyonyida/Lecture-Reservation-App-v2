package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RequestBookActivity extends AppCompatActivity {

    // Created Constants to add the values to the database
    //private static final String TAG = "RequestBookActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COURSE = "course";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_ISBN  = "isbn";
    private static final String KEY_USERID  = "type";
    private static final String KEY_STATUS  = "status";

    // Declared Variables
    EditText booksName, className, txtIsbn, booksQuantity;
    Button btnRequest, btnClear;
    Toolbar toolbar;

    //Firebase db
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_book);

        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //------------------------------------------------------
        //
        // Assigning the xml Variables
        btnRequest =  findViewById(R.id.btnRequest);
        btnClear = findViewById(R.id.btnClear);
        booksName = findViewById(R.id.booksName);
        className = findViewById(R.id.className);
        txtIsbn =  findViewById(R.id.txtIsbn);
        booksQuantity =  findViewById(R.id.booksQuantity);
        toolbar = findViewById(R.id.toolbarRequestBook);

        //supports the toolbar that is defined in the layout for the AdminHomeActivity
        setSupportActionBar(toolbar);
        // END of Assigning the xml Variables
        //-------------------------------------------------------
        //
        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //
        // Button Configuration to Request the Books
        //
        //////////////////////////////////////////////////////////////
        btnRequest.setOnClickListener((new View.OnClickListener() { //
            //////////////////////////////////////////////////////////////
            @Override
            public void onClick(View view)
            {
                int flags = 0;
                String title, course, isbn, quantity, status;

                // This is where we get the user ID from db:
                String userID = mFirebaseAuth.getCurrentUser().getUid();
                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + +


                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                //
                // If/Else statements to Check for Errors
                //
                // Checking Book's Name - - - - - - - - - - - - - - - - - -


                String test_title = booksName.getText().toString().trim();
                if(test_title.equals("")) {
                    flags++;
                    booksName.setError("Please enter a book title");
                } else { title = booksName.getText().toString().trim(); }


                // Checking Class Name - - - - - - - - - - - - - - - - - -
                String test_course = className.getText().toString().trim();
                if(test_course.equals("")){
                    flags++;
                    className.setError("Please enter a Class Name");
                } else { course = className.getText().toString().trim(); }


                // Checking isbn - - - - - - - - - - - - - - - - - -
                String test_isbn = txtIsbn.getText().toString().trim();
                if(test_isbn.equals("")){
                    if (txtIsbn.length() <= 0 || txtIsbn.length() >13){
                        flags++;
                        txtIsbn.setError("Please enter the 13 digit isbn number"); }
                } else{ isbn = txtIsbn.getText().toString().trim(); }


                // Checking quantity - - - - - - - - - - - - - - - - - -
                String test_quantity = booksQuantity.getText().toString().trim();
                if(test_quantity.equals("")){
                    flags++;
                    booksQuantity.setError("Please enter a quantity ");
                } else {
                    quantity = booksQuantity.getText().toString().trim();

                }

                // + + + + + + + + + + + + + + + + + + + + + + + + + + + + +  + + + + + + + + + + + + + + + + + + + + +
                //
                //
                // bookRequest add into DB starts here:
                if(flags == 0)
                {   //  Keys  | Value | Reference    |    Implementation
                    Map<String, Object> bookRequest = new HashMap<>();
                    bookRequest.put(KEY_TITLE, test_title);
                    bookRequest.put(KEY_COURSE, test_course );
                    bookRequest.put(KEY_QUANTITY, test_quantity);
                    bookRequest.put(KEY_ISBN, test_isbn);
                    bookRequest.put(KEY_USERID, userID);
                    bookRequest.put(KEY_STATUS, " "); // --> CREATES AN EMPTY FIELD SO LIBRARIAN CAN BIND DATA
                    //                                      --> (APPROVED / DENIED)
                    //
                    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                    // + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + + +
                    // ---------------------------------------------------------------------------------------------------
                    //
                    //
                    //Creating a collection path: 'requests' into DB
                    fStore.collection("requests").add(bookRequest)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),
                                                "Books were requested successful!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Books failed on request!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //--------------------------------------------------
                    // Clear all the fields after submit a book:
                    booksName.setText("");
                    className.setText("");
                    txtIsbn.setText("");
                    booksQuantity.setText("");
                    //
                    //----------------------------------------------------------------------------
                     finish();
                    //----------------------------------------------------------------------------

                }// END of bookRequest into DB
            }
        }));// END OF btnRequest
        // | | | | | | | | | | | | | | | | | | | | | | | | | | | | | |
        // V V V V V V V V V V V V V V V V V V V V V V V V V V V V V V
        //
        // Button Configuration to Clear the Fields
        //
        //////////////////////////////////////////////////////////////
        btnClear.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booksName.setText("");
                className.setText("");
                txtIsbn.setText("");
                booksQuantity.setText("");
            }
        }));// END OF btnCLear
    }

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
            case R.id.menuItemToHomeOnlyLogout:
                //signs out user
                FirebaseAuth.getInstance().signOut();
                //Starts LoginActivity if this button is clicked
                Intent intToLogin = new Intent(RequestBookActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
