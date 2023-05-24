package com.example.libraryreservationapp;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddRoomActivity extends AppCompatActivity {

    //private member variables
    private SeekBar capacitySeekBar;
    private Button addRoomButton;
    private EditText roomNumberEditText;
    private RadioGroup buildingRadioGroup;
    private Switch computerSwitch;
    private Switch wifiSwitch;
    private Switch engineeringSwitch;
    private RadioButton nuriganRadioButton;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        //gets the references from the layout for the variables
        fStore = FirebaseFirestore.getInstance();

        capacitySeekBar = findViewById(R.id.seekBarCapacity);
        addRoomButton = findViewById(R.id.btnAddRoom);
        roomNumberEditText = findViewById(R.id.editTextRoomNumber);
        buildingRadioGroup = findViewById(R.id.radioGroupBuilding);
        computerSwitch = findViewById(R.id.switchComputer);
        wifiSwitch = findViewById(R.id.switchWifi);
        engineeringSwitch = findViewById(R.id.switchEngineering);
        nuriganRadioButton = findViewById(R.id.radioButtonNurigan);

        //Add Room on click listener
        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calls to gather the data
                gatherData();
            }
        });
    }

    //gathers data from the form to be put into database
    public void gatherData(){
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
            roomNumberEditText.setError("Please enter a numeric room number");
        }
        else{
            //converts the string to an integer
            roomNumber = Integer.parseInt(sRoomNumber);
        }

        //gets the values of the switches
        boolean computer = computerSwitch.isChecked();
        boolean wifi = wifiSwitch.isChecked();
        boolean engineering = engineeringSwitch.isChecked();
        boolean available = true;

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
                case R.id.radioButtonNurigan:
                    building = getString(R.string.nurigan);
                    break;
                case R.id.radioButtonHyangpagan:
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

            //adds to the database the new room with the hashmap
            fStore.collection("room").add(roomInfo).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "강의실을 추가하였습니다", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "강의실이 추가되지 않았습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            //closes the activity
            finish();
        }
    }
}
