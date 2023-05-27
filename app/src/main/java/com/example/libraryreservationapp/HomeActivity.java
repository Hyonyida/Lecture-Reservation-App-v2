package com.example.libraryreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    //private member variables
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private View header;
    private TextView nameTextView;
    private TextView emailTextView;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbarStudent);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        nameTextView = header.findViewById(R.id.headerName);
        emailTextView = header.findViewById(R.id.headerEmail);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //ensures that if the screen is rotated it doesn't reload the autoscreen if they rotate on another fragment
        if(savedInstanceState == null){
            //for the fragments to be replaced when items are selected in the drawer
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            //opens the home screen fragment when the activity loads
            ft.replace(R.id.fragment_container, new HomeFragment()).commit();
            //sets the checked item in the navigation view
            navigationView.setCheckedItem(R.id.nav_home);
        }

        //sets up the navigation drawer
        setNavDrawer();

        //listener for if the backstack is changed
        this.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        //gets the current fragment by calling the function
                        Fragment current = getCurrentFragment();
                        if (current instanceof HomeFragment) {
                            //sets the menu item of the home to checked
                            navigationView.setCheckedItem(R.id.nav_home);
                        }
                        if (current instanceof ReserveRoomFragment){
                            //sets the menu item of the reserve room to checked
                            navigationView.setCheckedItem(R.id.nav_reserve_room);
                        }
                        if (current instanceof BoardFragment){
                            //sets the menu item of the reserve book to checked
                            navigationView.setCheckedItem(R.id.nav_board);
                        }
                    }
                });

    }

    @Override
    public void onBackPressed(){
        //checks to see if the drawer should be closed first rather than closing the activity
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        //if the drawer is closed, actually closes the activity
        else{
            super.onBackPressed();
        }
    }

    private void setNavDrawer(){
        //supports the toolbar that is defined in the layout for the AdminHomeActivity
        setSupportActionBar(toolbar);

        //creates the toggle for the navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //sets the name and email of the user in the header
        String userID = auth.getUid();

        //gets the information for and sets the textViews in the header
        db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //gets the document
                    DocumentSnapshot document = task.getResult();

                    String name = document.getString("rName");
                    String email = document.getString("email");

                    //sets the textViews
                    nameTextView.setText(name);
                    emailTextView.setText(email);
                }
                else{
                   Log.d("MYDEBUG", "Could not get the user information for the drawer");
                }
            }
        });

        //passes the activity as the listener for the navigation view
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //for the fragments to be replaced when items are selected in the drawer
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                //converts the selected navigation item to do the proper result
                switch(item.getItemId()){
                    case R.id.nav_home:
                        //replaces the fragment with the home fragment
                        ft.replace(R.id.fragment_container, new HomeFragment()).commit();
                        break;
                    case R.id.nav_logout:
                        //signs out user
                        FirebaseAuth.getInstance().signOut();
                        //Starts LoginActivity if this button is clicked
                        Intent intToLogin = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intToLogin);
                        break;
                    case R.id.nav_reserve_room:
                        //replaces the fragment with the home fragment
                        ft.replace(R.id.fragment_container, new ReserveRoomFragment()).addToBackStack("parent").commit();
                        break;
                    case R.id.nav_board:
                        ft.replace(R.id.fragment_container, new BoardFragment()).addToBackStack("parent").commit();
                        break;
                }
                //closes the drawer
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    public Fragment getCurrentFragment() {
        //gets the current fragment that is in the container
        return this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }
}