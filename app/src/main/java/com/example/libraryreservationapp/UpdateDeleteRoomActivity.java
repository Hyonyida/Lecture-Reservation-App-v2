package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
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

public class UpdateDeleteRoomActivity extends AppCompatActivity implements DeleteRoomDialogFragment.DeleteRoomDialogListener {

        //private member variables
        private SeekBar capacitySeekBar;
        private Button updateButton;
        private Button deleteButton;
        private EditText roomNumberEditText;
        private RadioGroup buildingRadioGroup;
        private Switch computerSwitch;
        private Switch wifiSwitch;
        private Switch engineeringSwitch;
        private Switch availableSwitch;
        private FirebaseFirestore fStore;
        private RadioButton nuriganRadioButton;
        private DocumentReference docRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_room);

        //gets the references to the layout components
        fStore = FirebaseFirestore.getInstance();

        capacitySeekBar = findViewById(R.id.seekBarCapacityUpdate);
        updateButton = findViewById(R.id.btnUpdateRoom);
        deleteButton = findViewById(R.id.btnDeleteRoom);
        roomNumberEditText = findViewById(R.id.editTextRoomNumberUpdate);
        buildingRadioGroup = findViewById(R.id.radioGroupBuildingUpdate);
        computerSwitch = findViewById(R.id.switchComputerUpdate);
        wifiSwitch = findViewById(R.id.switchWifiUpdate);
        engineeringSwitch = findViewById(R.id.switchEngineeringUpdate);
        availableSwitch = findViewById(R.id.switchAvailableUpdate);
        nuriganRadioButton = findViewById(R.id.radioButtonNuriganUpdate);

        //gets the document ID of the item clicked on from the intent
        Intent intent = getIntent();
        String documentID = intent.getStringExtra("docID");
        //creates a reference to a specific document in the collection
        docRef = fStore.collection("room").document(documentID);

        //retrieves the data from firestore for the room that is selected
        getDataFromFirestore();

        //Update Room on click listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calls the method that updates firestore
                updateDataForRoom();
            }
        });

        //Delete Room on click listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shows the delete confirmation dialog
                showDeleteDialog();
            }
        });

    }

    //updates the firestore
    public void updateDataForRoom() {
        int flags = 0;
        int roomNumber = 0;
        String building = "";
        String combo = "";

        //clears the errors to ensure there is no false error
        roomNumberEditText.setError(null);
        nuriganRadioButton.setError(null);

        //gets the capacity value
        int capacity = capacitySeekBar.getProgress();

        //gets the room number from the edit text
        String sRoomNumber = roomNumberEditText.getText().toString();
        //checks to see if it is an empty edit text
        if(sRoomNumber.equals("")){
            //adds a flag and an error message
            flags++;
            roomNumberEditText.setError("강의실 번호를 입력해주세요");
        }
        else{
            //converts the string to an integer
            roomNumber = Integer.parseInt(sRoomNumber);
        }

        //gets the values of the switches
        boolean computer = computerSwitch.isChecked();
        boolean wifi = wifiSwitch.isChecked();
        boolean engineering = engineeringSwitch.isChecked();
        boolean available = availableSwitch.isChecked();

        //gets the selected radiobutton
        int selectedBuilding = buildingRadioGroup.getCheckedRadioButtonId();
        //checks to make sure a radio button was selected
        if(selectedBuilding == -1){
            //adds a flag and an error
            flags++;
            nuriganRadioButton.setError("건물을 선택해주세요");
        }
        else{
            //switches the selected building id to the proper string of the building
            switch (selectedBuilding){
                case R.id.radioButtonNuriganUpdate:
                    building = getString(R.string.nurigan);
                    break;
                case R.id.radioButtonHyangpaganUpdate:
                    building = getString(R.string.hyangpagan);
                    break;
            }
        }

        //creates a combo of what the recycler view should be ordered by to allow for ordering by multiple fields
        combo = building + " " + roomNumber;

        //checks to see if there are any errors
        if(flags == 0){
            //creates a hashmap to store all the data for the room
            Map<String, Object> roomInfo = new HashMap<>();
            roomInfo.put("capacity", capacity);
            roomInfo.put("computer", computer);
            roomInfo.put("wifi", wifi);
            roomInfo.put("engineering", engineering);
            roomInfo.put("building", building);
            roomInfo.put("roomNumber", roomNumber);
            roomInfo.put("available", available);
            roomInfo.put("combo", combo);

            //updates the database for the specific room with the hashmap
            docRef.update(roomInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Update was successful!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //closes the activity
            finish();
        }
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

                    //gets the roomNumber value from database
                    Long roomNumber = document.getLong("roomNumber");
                    //converts from a long to a string
                    String sRoomNumber = roomNumber.toString();
                    //sets the editText to the value
                    roomNumberEditText.setText(sRoomNumber);

                    //gets the computer value from database
                    boolean computer = document.getBoolean("computer");
                    //sets the value for the switch
                    computerSwitch.setChecked(computer);

                    //gets the whiteboard value from database
                    boolean engineering = document.getBoolean("engineering");
                    //sets the value for the switch
                    engineeringSwitch.setChecked(engineering);

                    //gets the wifi value from database
                    boolean wifi = document.getBoolean("wifi");
                    //sets the value for the switch
                    wifiSwitch.setChecked(wifi);

                    //gets the available value from database
                    boolean available = document.getBoolean("available");
                    //sets the value for the switch
                    availableSwitch.setChecked(available);

                    //gets the capacity value from the database
                    long c = document.getLong("capacity");
                    //converts the long into an int
                    int capacity = (int)c;
                    //sets the seekbar with the value
                    capacitySeekBar.setProgress(capacity);

                    //gets the building value from the database
                    String building = document.getString("building").trim();

                    //selects the correct radioButton to check depending on if it matches the one from the database
                    if(building.equals(getString(R.string.nurigan))){
                        buildingRadioGroup.check(R.id.radioButtonNuriganUpdate);
                    }
                    if(building.equals(getString(R.string.hyangpagan))){
                        buildingRadioGroup.check(R.id.radioButtonHyangpaganUpdate);
                    }
                }
                else{
                    Log.d("MYDEBUG", "Error getting document values");
                }
            }
        });
    }

    //shows the dialog
    public void showDeleteDialog(){
        DialogFragment dialog = new DeleteRoomDialogFragment();
        dialog.show(getSupportFragmentManager(), "DeleteRoomDialogFragment");
    }

    //if the user clicked the positive dialog button
    @Override
    public void onDialogPositiveClick(DialogFragment dialog){
        //deletes the document from the database
        docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "강의실이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    //closes the activity
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "강의실이 삭제되지 않았습니다", Toast.LENGTH_SHORT).show();
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
