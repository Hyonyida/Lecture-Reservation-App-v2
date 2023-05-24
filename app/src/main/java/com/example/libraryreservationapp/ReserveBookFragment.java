package com.example.libraryreservationapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.libraryreservationapp.Common.Common;
import com.example.libraryreservationapp.Common.NonSwipeViewPager;
import com.google.firebase.firestore.CollectionReference;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class ReserveBookFragment extends Fragment {
    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference bookRef;

    StepView stepView;
    NonSwipeViewPager viewPager;
    Button btn_previous_step, btn_next_step;

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //sets the view of the fragment
        View v = inflater.inflate(R.layout.fragment_reserve_book, container, false);

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BOOK_BUTTON_NEXT));;

        stepView = v.findViewById(R.id.step_view);
        viewPager = v.findViewById(R.id.view_pager);
        btn_previous_step = v.findViewById(R.id.btn_previous_step);
        btn_next_step = v.findViewById(R.id.btn_next_step);

        //Events
        btn_previous_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.bookStep == 2 || Common.bookStep > 0){
                    Common.bookStep--;
                    viewPager.setCurrentItem(Common.bookStep);
                    if (Common.bookStep < 2) //always enable NEXT when Step < 2
                    {
                        btn_next_step.setEnabled(true);
                        setColorButton();
                    }
                }
            }
        });

        btn_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.bookStep < 2 || Common.bookStep == 0){
                    Common.bookStep++;
                    if(Common.bookStep == 1) { //pick time
                        if (Common.currentBook != null){
                            loadTimeSlotOfBook(Common.currentBook.getBookId());
                        }
                    }
                    else if (Common.bookStep == 2){ //confirm
                        if(Common.currentBookTimeSlot != -1){
                            confirmReservation();
                        }
                    }
                }
                viewPager.setCurrentItem(Common.bookStep);
            }
        });

        setupStepView();
        setColorButton();

        // View
        viewPager.setAdapter(new SelectBookAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(3); //keep state of 3 screens
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //show step
                stepView.go(position, true);
                if(position == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                btn_next_step.setEnabled(false);
                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return v;
    }

    private void confirmReservation() {
        //Send broadcast to fragment step 3
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOK_RESERVATION);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOfBook(String bookId) {
        //Send Local Broadcast to Fragment step2
        Intent intent = new Intent(Common.KEY_DISPLAY_BOOK_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void setColorButton() {
        if(btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.colorButton);
        } else {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }

        if(btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        } else {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Book");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }


    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_BOOK_STEP, 0);
            if (step == 1)
                Common.currentBook = intent.getParcelableExtra(Common.KEY_BOOK_SELECTED);
            if (step == 2)
                Common.currentBookTimeSlot = intent.getIntExtra(Common.KEY_BOOK_TIME_SLOT, -1);

            btn_next_step.setEnabled(true);
            setColorButton();

        }
    };
}