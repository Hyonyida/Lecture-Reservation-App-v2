package com.example.libraryreservationapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class CheckInCheckOutDialogFragment extends DialogFragment {
    //for event callbacks
    public interface CheckInCheckOutDialogListener{
        void onDialogPositiveClick(DialogFragment dialog, boolean checkedIn, int type);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public CheckInCheckOutDialogFragment.CheckInCheckOutDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //get values from bundle
        final int type = getArguments().getInt("type");
        final boolean checkedIn = getArguments().getBoolean("checkedIn");

        String positiveButtonText = "";
        String messageText = "";

        //if the type is a room
        if(type == 0){
            //sets the message text and positive button text to be different if you are already checked in or not
            if(checkedIn){
                positiveButtonText = getContext().getResources().getString(R.string.check_out);
                messageText = getContext().getResources().getString(R.string.check_out_question);
            }
            else{
                positiveButtonText = getContext().getResources().getString(R.string.check_in);
                messageText = getContext().getResources().getString(R.string.check_in_question);
            }
        }

        //if the type is a book
        if(type == 1){
            //sets the message text and positive button text to be different if you are already checked in or not
            if(checkedIn){
                positiveButtonText = getContext().getResources().getString(R.string.check_out);
                messageText = getContext().getResources().getString(R.string.check_out_question_book);
            }
            else{
                positiveButtonText = getContext().getResources().getString(R.string.check_in);
                messageText = getContext().getResources().getString(R.string.check_in_question_book);
            }
        }


        // Inflate and set the layout for the dialog
        builder.setMessage(messageText)
                .setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the HomeFragment
                        listener.onDialogPositiveClick(CheckInCheckOutDialogFragment.this, !checkedIn, type);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //send the negative button event back to the HomeFragment
                        listener.onDialogNegativeClick(CheckInCheckOutDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
