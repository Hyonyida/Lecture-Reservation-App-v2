package com.example.libraryreservationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SeeRequestActivity extends AppCompatActivity
{
    //private member variables
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_request);

        toolbar = findViewById(R.id.toolbarSeeRequests);

        //gets instance of firestore
        fStore = FirebaseFirestore.getInstance();

        //supports the toolbar that is defined in the layout for the AdminHomeActivity
        setSupportActionBar(toolbar);

        //calls the recycler view for it to be set up
        MakeRequestRecyclerView();

        //listens on the request adapter
        adapter.setOnItemClickListener(new RequestAdapter.RequestAdapterListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //gets the documentID of the item clicked
                String docID = documentSnapshot.getReference().getId();
                //Starts UpdateApproveRequestActivity if an item in the recyclerView is clicked passing the documentID
                Intent intToUpdateApproveRequest = new Intent(SeeRequestActivity.this, UpdateApproveRequestActivity.class);
                intToUpdateApproveRequest.putExtra("docID", docID);
                startActivity(intToUpdateApproveRequest);
            }
        });

    }

    private void MakeRequestRecyclerView() {
        // creates a query that uses the collection reference to get the courses in ascending order. changed Combo to Book's name
        Query query = fStore.collection("requests").orderBy("title", Query.Direction.ASCENDING);

        // creates configurations for the adapter and binds the query to the recyclerView
        // .setLifecycleOwner(this) allows for deletion of onStart and onStop overrides
        FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>().setQuery(query, Request.class).setLifecycleOwner(this).build();

        // create the adapter with corresponding parameter
        adapter = new RequestAdapter(options);

        // gets the recyclerView id for reference
        recyclerView = findViewById(R.id.recyclerViewRequest);
        // sets the layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //adds horizontal line between different items
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        // sets the adapter
        recyclerView.setAdapter(adapter);
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
                Intent intToLogin = new Intent(SeeRequestActivity.this, LoginActivity.class);
                startActivity(intToLogin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}