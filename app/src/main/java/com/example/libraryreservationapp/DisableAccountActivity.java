package com.example.libraryreservationapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DisableAccountActivity extends AppCompatActivity implements DisableAccountDialogFragment.DisableAccountDialogListener{

    //private member variables
    private FirebaseFirestore fStore;
    private Button disableEnableButton;
    private Button updateButton;
    private EditText studentIDEditText;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText reasonEditText;
    private DocumentReference docRef;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private boolean isDisabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_account);

        //gets the references to the layout components
        fStore = FirebaseFirestore.getInstance();
        disableEnableButton = findViewById(R.id.btnDisableEnableAccount);
        updateButton = findViewById(R.id.btnUpdateAccount);
        studentIDEditText = findViewById(R.id.editTextStudentID);
        nameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.editTextEmail);

        spinner = findViewById(R.id.spinner_user_role);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //gets the document ID of the item clicked on from the intent
        Intent intent = getIntent();
        String documentID = intent.getStringExtra("docID");
        //creates a reference to a specific document in the collection
        docRef = fStore.collection("users").document(documentID);

        //retrieves the data from firestore for the room that is selected
        getDataFromFirestore();

        //Update Account on click listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calls the method that updates firestore
                updateDataForUser();
            }
        });

        //Disable Account on click listener
        disableEnableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shows the dialog to disable or enable
                showDialog();
            }
        });

    }

    //updates the firestore
    public void updateDataForUser() {
        //gets the role that was selected in the spinner
        String role = spinner.getSelectedItem().toString();

        //creates a hashmap to store all the data for the room
        Map<String, Object> accountInfo = new HashMap<>();
        accountInfo.put("type", role);


        //updates the database for the specific room with the hashmap
        docRef.update(accountInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "회원정보가 수정되었습니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "회원정보가 수정되지 않았습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //closes the activity
        finish();

    }

    //collects the data from firestore for the specific room
    public void getDataFromFirestore(){
        //gets the document for the specific room that was selected
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //gets the document
                    DocumentSnapshot document = task.getResult();

                    //gets the ram id value from database
                    String ramid = document.getString("student_id").trim();
                    //sets the editText to the value
                    studentIDEditText.setText(ramid);

                    //gets the name values from database
                    String rName = document.getString("rName").trim();
                    //sets the editText to the value
                    nameEditText.setText(rName);

                    //gets the email value from database
                    String email = document.getString("email").trim();
                    //sets the editText to the value
                    emailEditText.setText(email);

                    //gets the role value from the database
                    String role = document.getString("type").trim();
                    //checks to see if role is null
                    if(role != null){
                        //selects the position in the type array that matches the role
                        int spinnerPosition = adapter.getPosition(role);
                        spinner.setSelection(spinnerPosition);
                    }

                    //gets if account is disabled or enabled
                    isDisabled = document.getBoolean("isDisabled");
                    //sets button to display enabled if they are currently disabled
                    if(isDisabled){
                        disableEnableButton.setText(R.string.enable);
                        disableEnableButton.setTextColor(Color.parseColor("#000000"));
                        disableEnableButton.setBackgroundColor(Color.parseColor("#45f542"));
                    }
                    //sets button to display disabled if they are currently enabled
                    else{
                        disableEnableButton.setText(R.string.disable);
                        disableEnableButton.setTextColor(Color.parseColor("#FFFFFF"));
                        disableEnableButton.setBackgroundColor(Color.parseColor("#F44336"));
                    }
                }
                else{
                    Log.d("MYDEBUG", "Error getting document values");
                }
            }
        });
    }

    //shows the dialog
    public void showDialog(){
        DialogFragment dialog = new DisableAccountDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putBoolean("isDisabled", !isDisabled);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "DisableAccountDialogFragment");

    }

    //if the user clicked the positive dialog button
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String reason){

        //creates a hashmap with the data for the update
        Map<String, Object> update = new HashMap<>();
        update.put("isDisabled", !isDisabled);
        update.put("reason", reason);

        //updates the account in the database
        docRef.update(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "계정이 수정되었습니다", Toast.LENGTH_SHORT).show();
                    //closes the activity
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "수정되지 않았습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //if the user clicked the negative dialog button
    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        //closes the dialog
        dialog.dismiss();
    }

}
