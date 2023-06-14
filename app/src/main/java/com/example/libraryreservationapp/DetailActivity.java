package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();
    private LinearLayout comment_layout;


    // 사용할 컴포넌트 선언
    TextView title_tv, date_tv, content_tv;
    EditText comment_et;
    Button submit_btn;

    // Firestore 인스턴스
    FirebaseFirestore db;
    int boardSeq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Firestore 인스턴스 초기화
        db = FirebaseFirestore.getInstance();

        // 컴포넌트 초기화
        title_tv = findViewById(R.id.title_tv);
        date_tv = findViewById(R.id.date_tv);
        content_tv = findViewById(R.id.content_tv);
        comment_et = findViewById(R.id.comment_et);
        submit_btn = findViewById(R.id.submit_btn);
        comment_layout = findViewById(R.id.comment_layout); // 추가된 코드

        // 게시글 정보 가져오기
        String boardSeqString = getIntent().getStringExtra("board_seq");
        if (boardSeqString != null && !boardSeqString.isEmpty()) {
            boardSeq = Integer.parseInt(boardSeqString);
            getBoardData(boardSeq);
        } else {
            Toast.makeText(this, "게시글을 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 댓글 등록 버튼 클릭 시 이벤트 처리
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = comment_et.getText().toString().trim();
                if (!comment.isEmpty()) {
                    addComment(comment);
                } else {
                    Toast.makeText(DetailActivity.this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getBoardData(int board_Seq) {
        db.collection("boards")
                .document(String.valueOf(board_Seq))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String title = documentSnapshot.getString("title");
                        String date = documentSnapshot.getString("date");
                        String content = documentSnapshot.getString("content");

                        // 가져온 데이터를 화면에 표시
                        title_tv.setText(title);
                        date_tv.setText(date);
                        content_tv.setText(content);

                        // 댓글 가져오기
                        loadBoardComments();
                    } else {
                        Toast.makeText(this, "게시글을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting document", e);
                    Toast.makeText(this, "게시글을 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void loadBoardComments() {
        DocumentReference boardRef = db.collection("boards").document(String.valueOf(boardSeq));
        CollectionReference commentsCollectionRef = boardRef.collection("comments_collection");

        commentsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // 기존 댓글 레이아웃 초기화
                    comment_layout.removeAllViews();

                    for (DocumentSnapshot document : task.getResult()) {
                        String userId = document.getString("userid");
                        String content = document.getString("content");
                        String date = document.getString("date");

                        View customView = getLayoutInflater().inflate(R.layout.custom_comment, null);
                        TextView userId_tv = customView.findViewById(R.id.cmt_userid_tv);
                        TextView content_tv = customView.findViewById(R.id.cmt_content_tv);
                        TextView date_tv = customView.findViewById(R.id.cmt_date_tv);

                        userId_tv.setText(userId);
                        content_tv.setText(content);
                        date_tv.setText(date);

                        comment_layout.addView(customView);
                    }
                } else {
                    Log.d(TAG, "Error getting board comments: ", task.getException());
                }
            }
        });
    }

    private void addComment(String comment) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getEmail();

            Map<String, Object> commentData = new HashMap<>();
            commentData.put("userid", userId);
            commentData.put("content", comment);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            commentData.put("date", currentDate);

            // 댓글 저장
            CollectionReference commentsRef = db.collection("boards").document(String.valueOf(boardSeq))
                    .collection("comments_collection");
            commentsRef.add(commentData)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                String commentId = task.getResult().getId();

                                // 댓글을 게시판에도 저장
                                DocumentReference boardRef = db.collection("boards").document(String.valueOf(boardSeq));
                                boardRef.collection("comments_collection").document(commentId).set(commentData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // 댓글 등록 후 댓글 목록 업데이트
                                                    loadBoardComments();
                                                    comment_et.setText(""); // 댓글 입력란 초기화
                                                } else {
                                                    Log.d(TAG, "Error adding comment to board: ", task.getException());
                                                }
                                            }
                                        });
                            } else {
                                Log.d(TAG, "Error adding comment: ", task.getException());
                            }
                        }
                    });
        }
    }
}
