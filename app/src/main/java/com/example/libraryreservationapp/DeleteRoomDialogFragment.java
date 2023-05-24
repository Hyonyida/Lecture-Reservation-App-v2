package com.example.libraryreservationapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteRoomDialogFragment extends DialogFragment {

    //for event callbacks
    public interface DeleteRoomDialogListener{
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private DeleteRoomDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Uses builder class for convienent dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_room_message)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //send the positive button event back to the UpdateDeleteRoomActivity
                        listener.onDialogPositiveClick(DeleteRoomDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //send the negative button event back to the UpdateDeleteRoomActivity
                        listener.onDialogNegativeClick(DeleteRoomDialogFragment.this);
                    }
                });
        //creates the dialog and returns it
        return builder.create();
    }

    //overrides the fragment.onAttach() method to instantiate the listener
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        // instantiates the listener to send events to the host
        listener = (DeleteRoomDialogListener) context;
    }
}
