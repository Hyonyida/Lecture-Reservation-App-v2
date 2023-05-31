package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";

    private TextView title_tv, date_tv, content_tv;
    private EditText comment_et;
    private Button submit_btn;
    private LinearLayout comment_layout;

    private FirebaseFirestore db;
    private CollectionReference commentsRef;

    private int boardSeq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title_tv = findViewById(R.id.title_tv);
        date_tv = findViewById(R.id.date_tv);
        content_tv = findViewById(R.id.content_tv);
        comment_et = findViewById(R.id.comment_et);
        submit_btn = findViewById(R.id.submit_btn);
        comment_layout = findViewById(R.id.comment_layout);

        db = FirebaseFirestore.getInstance();
        commentsRef = db.collection("comments");

        boardSeq = getIntent().getIntExtra("board_seq", 0);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = comment_et.getText().toString().trim();
                if (!TextUtils.isEmpty(comment)) {
                    addComment(comment);
                }
            }
        });

        loadBoardDetails();
        loadBoardComments();
    }

    private void loadBoardDetails() {
        db.collection("boards").document(String.valueOf(boardSeq))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String title = document.getString("title");
                                String date = document.getString("date");
                                String content = document.getString("content");

                                // 가져온 값을 뷰에 설정
                                title_tv.setText(title);
                                date_tv.setText(date);
                                content_tv.setText(content);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "Error getting document: ", task.getException());
                        }
                    }
                });
    }

    private void loadBoardComments() {
        DocumentReference boardRef = db.collection("boards").document(String.valueOf(boardSeq));
        CollectionReference commentsCollectionRef = boardRef.collection("comments_collection");

        commentsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    comment_layout.removeAllViews();

                    for (DocumentSnapshot document : task.getResult()) {
                        String userId = document.getString("userid");
                        String content = document.getString("content");

                        View customView = getLayoutInflater().inflate(R.layout.custom_comment, null);
                        TextView userId_tv = customView.findViewById(R.id.cmt_userid_tv);
                        TextView content_tv = customView.findViewById(R.id.cmt_content_tv);

                        userId_tv.setText(userId);
                        content_tv.setText(content);

                        comment_layout.addView(customView);
                    }
                } else {
                    Log.d(TAG, "Error getting board comments: ", task.getException());
                }
            }
        });
    }


    private void loadComments() {
        db.collection("comments").document(String.valueOf(boardSeq))
                .collection("comments_collection")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            comment_layout.removeAllViews();

                            for (DocumentSnapshot document : task.getResult()) {
                                String userId = document.getString("userid");
                                String content = document.getString("content");

                                View customView = getLayoutInflater().inflate(R.layout.custom_comment, null);
                                TextView userId_tv = customView.findViewById(R.id.cmt_userid_tv);
                                TextView content_tv = customView.findViewById(R.id.cmt_content_tv);

                                userId_tv.setText(userId);
                                content_tv.setText(content);

                                comment_layout.addView(customView);
                            }
                        } else {
                            Log.d(TAG, "Error getting comments: ", task.getException());
                        }
                    }
                });
    }

    private void addComment(String comment) {
        String userId = "example_user"; // 유저 아이디는 적절히 변경해주세요

        Map<String, Object> commentData = new HashMap<>();
        commentData.put("userid", userId);
        commentData.put("content", comment);

        // 파이어스토어에 댓글 저장
        commentsRef.document(String.valueOf(boardSeq))
                .collection("comments_collection")
                .add(commentData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            String commentId = task.getResult().getId();

                            DocumentReference boardRef = db.collection("boards").document(String.valueOf(boardSeq));
                            boardRef.collection("comments_collection").document(commentId).set(commentData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // ...

                                                loadBoardComments(); // Update comments
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
