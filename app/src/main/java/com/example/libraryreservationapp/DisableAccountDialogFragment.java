package com.example.libraryreservationapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class DisableAccountDialogFragment extends DialogFragment {
    //for event callbacks
    public interface DisableAccountDialogListener{
        void onDialogPositiveClick(DialogFragment dialog, String r);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private DisableAccountDialogFragment.DisableAccountDialogListener listener;
    private EditText reasonEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Uses builder class for convienent dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Get layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_disable, null);

        boolean isDisabled = getArguments().getBoolean("isDisabled");
        reasonEditText = view.findViewById(R.id.reason);

        String buttonText;
        if(isDisabled){
            buttonText = getContext().getResources().getString(R.string.disable);
            reasonEditText.setHint(getContext().getResources().getString(R.string.reason_disable));
        }
        else{
            buttonText = getContext().getResources().getString(R.string.enable);
            reasonEditText.setHint(getContext().getResources().getString(R.string.reason_enable));
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //send the negative button event back to the DisableAccountActivity
                        listener.onDialogNegativeClick(DisableAccountDialogFragment.this);
                    }
                });
        //creates the dialog and returns it
        return builder.create();


    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog)getDialog();

        if(dialog != null){
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean closeDialog = true;
                    String r = reasonEditText.getText().toString();
                    //clears the errors to ensure there is no false error
                    reasonEditText.setError(null);

                    //checks to see if it is an empty edit text
                    if(r.equals("")){
                        //adds an error message
                        reasonEditText.setError("이유를 입력해주세요");
                        closeDialog = false;

                    }
                    else{
                        //send the positive button event back to the DisableAccountActivity
                        listener.onDialogPositiveClick(DisableAccountDialogFragment.this, r);
                    }

                    if(closeDialog == true){
                        dismiss();
                    }
                }
            });
        }
    }

    //overrides the fragment.onAttach() method to instantiate the listener
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        // instantiates the listener to send events to the host
        listener = (DisableAccountDialogListener) context;
    }


}
