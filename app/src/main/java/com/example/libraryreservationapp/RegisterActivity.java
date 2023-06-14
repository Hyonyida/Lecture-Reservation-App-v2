package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    EditText title_et, content_et;
    Button reg_button;

    // FirebaseAuth 인스턴스
    FirebaseAuth mAuth;

    // Firestore 인스턴스
    FirebaseFirestore db;

    // 유저아이디 변수
    String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // FirebaseAuth 인스턴스 초기화
        mAuth = FirebaseAuth.getInstance();

        // Firestore 인스턴스 초기화
        db = FirebaseFirestore.getInstance();

        // ListActivity에서 넘긴 userid를 변수로 받음
        userid = getIntent().getStringExtra("userid");

        // 컴포넌트 초기화
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);

        // 버튼 이벤트 추가
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시물 등록 함수 호출
                regBoard(userid, title_et.getText().toString(), content_et.getText().toString());
            }
        });
    }

    private void regBoard(String userId, String title, String content) {
        // Firestore에서 현재 게시글 개수 확인
        db.collection("boards")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // 현재 게시글 개수 + 1을 board_seq 값으로 사용하여 문서 생성
                    int boardSeq = queryDocumentSnapshots.size() + 1;

                    // Firestore에 게시물 등록
                    Map<String, Object> boardData = new HashMap<>();
                    boardData.put("userid", userId);
                    boardData.put("title", title);
                    boardData.put("content", content);
                    boardData.put("board_seq", boardSeq); // board_seq 필드 추가

                    // 현재 날짜와 시간을 문자열로 변환하여 date 필드에 저장
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    boardData.put("date", currentDate);

                    db.collection("boards")
                            .document(String.valueOf(boardSeq)) // 문서 식별자로 board_seq 사용
                            .set(boardData)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + boardSeq);
                                Toast.makeText(RegisterActivity.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(RegisterActivity.this, "등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting documents", e);
                    Toast.makeText(RegisterActivity.this, "게시글 개수를 확인하는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                });
    }
}
