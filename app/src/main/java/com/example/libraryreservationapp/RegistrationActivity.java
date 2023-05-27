package com.example.libraryreservationapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    EditText rName, studentId, email, password;
    Button btnRegister;
    Spinner spinner;

    //Firebase db
    FirebaseAuth mFirebaseAuth;
    //Firestore db
    FirebaseFirestore fStore;
    //Firestore user id
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Get Firebase Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        //Get Firestore Instance
        fStore = FirebaseFirestore.getInstance();

        rName = findViewById(R.id.rName);
        studentId = findViewById(R.id.studentId);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);


        spinner = findViewById(R.id.spinner_user_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String rX = rName.getText().toString();
                final String studentX = studentId.getText().toString();
                final String emailX = email.getText().toString();
                final String typeX = spinner.getSelectedItem().toString();
                String pwdX = password.getText().toString();
                int flags = 0;

                if(rX.length() > 4 || rX.length() < 2 ){
                    rName.setError("2 ~ 4글자를 입력해주세요");
                    flags++;
                }

                if(!isValidEmail(emailX)){
                    email.setError("유효한 이메일 주소가 아닙니다");
                    flags++;
                }

                if(!isValidStudent(studentX)){
                    studentId.setError("학번 : #########");
                    flags++;
                }

                if(pwdX.length() < 6){
                    password.setError("6글자 이상 입력해주세요");
                    flags++;
                }

                if(rX.isEmpty()){
                    rName.setError("이름을 입력해주세요");
                    flags++;
                }
                if (studentX.isEmpty()){
                    studentId.setError("학번을 입력해주세요");
                    flags++;
                }
                if (emailX.isEmpty()){
                    email.setError("이메일을 입력해주세요");
                    flags++;
                }
                if (pwdX.isEmpty()){
                    password.setError("비밀번호를 입력해주세요");
                    flags++;
                }

                if (flags == 0){
                    //Register user in Firebase
                    mFirebaseAuth.createUserWithEmailAndPassword(emailX, pwdX).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                Toast.makeText(RegistrationActivity.this, "회원가입을 실패했습니다: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "계정을 생성하였습니다", Toast.LENGTH_SHORT).show();

                                //Add user information to Firestore
                                userID = mFirebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("rName", rX);
                                user.put("student_id", studentX);
                                user.put("email", emailX);
                                user.put("type", typeX);

                                user.put("isDisabled", false);
                                user.put("reason", "");

                                documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "실패" + e.toString());
                                    }
                                });
                                // Return to Login after Successfully being registered
                                finish();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "오류 발생", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isValidEmail(String e) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return e.matches(regex);
    }

    public boolean isValidStudent(String r) {
        String regex = "^\\d{9}$";
        return r.matches(regex);
    }
}