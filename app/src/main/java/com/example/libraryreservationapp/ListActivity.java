package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private ListView listView;
    private Button reg_button;
    private String userId;
    private FirebaseAuth mAuth;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> seqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ListActivity.this, adapterView.getItemAtPosition(i) + " 클릭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                intent.putExtra("board_seq", seqList.get(i));
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });

        reg_button = findViewById(R.id.reg_button);
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, RegisterActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetBoard getBoard = new GetBoard();
        getBoard.execute();
    }

    class GetBoard extends AsyncTask<String, Void, Void> {

        private ArrayList<String> responseList = new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            try {
                db.collection("boards")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    responseList.add(document.getData().toString());
                                }
                                // 작업이 완료되면 onPostExecute 메서드가 자동으로 호출됨
                                onPostExecute(null);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                onPostExecute(null);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (responseList != null) {
                Log.d(TAG, "onPostExecute, " + responseList.toString());

                titleList.clear();
                seqList.clear();

                for (String data : responseList) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String title = jsonObject.optString("title");
                        String seq = jsonObject.optString("seq");
                        titleList.add(title);
                        seqList.add(seq);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, titleList);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            } else {
                Log.d(TAG, "onPostExecute, result is null");
            }
        }
    }
}
