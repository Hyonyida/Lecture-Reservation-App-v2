package com.example.libraryreservationapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class AccountsAdapter extends FirestoreRecyclerAdapter<Accounts, AccountsAdapter.MyViewHolder> {
    //creates an interface for the listener
    interface AccountsAdapterListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    private AccountsAdapter.AccountsAdapterListener listener;

    public void setOnItemClickListener(AccountsAdapter.AccountsAdapterListener listener){
        this.listener = listener;
    }

    //creates an adapter with the query and configurations that was passed in
    public AccountsAdapter(@NonNull FirestoreRecyclerOptions<Accounts> options){
        super(options);
    }

    //specifies the type of ViewHolder for this specific project
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView studentIDTextView;
        public TextView nameTextView;
        public TextView roleTextView;
        public TextView colorTextView;
        public TextView disabledTextView;


        //constructor for ViewHolder
        public MyViewHolder(View view){
            super(view);

            //assigns the member variables the correct TextViews and imageViews
            studentIDTextView = view.findViewById(R.id.textViewSTUDENT);
            nameTextView = view.findViewById(R.id.textViewName);
            roleTextView = view.findViewById(R.id.textViewRole);
            colorTextView = view.findViewById(R.id.textViewColor);
            disabledTextView = view.findViewById(R.id.textViewDisabled);


            //onClickListener for the items in the recyclerView
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gets the position of the clicked
                    int position = getAdapterPosition();
                    //makes sure the position is valid and listener exists
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }

    //binds the correct item into the recyclerView
    @Override
    protected void onBindViewHolder(@NonNull AccountsAdapter.MyViewHolder myViewHolder, int i, @NonNull Accounts accounts) {
        String role = accounts.getType();
//        boolean isDisabled = true;
        boolean isDisabled = accounts.getDisabled();
        Log.d("MYDEBUG", String.valueOf(isDisabled));

        // Puts the ram id, name, and role into the textViews for the position (i)
        myViewHolder.studentIDTextView.setText(accounts.getStudent_id());
        myViewHolder.nameTextView.setText("Name: " + accounts.getrName());
        myViewHolder.roleTextView.setText(role);

        //checks the value of isDisabled
        if(isDisabled){
            //sets the textView and makes the text bold
            myViewHolder.disabledTextView.setText(R.string.disabled);
            myViewHolder.disabledTextView.setTypeface(myViewHolder.disabledTextView.getTypeface(), Typeface.BOLD);
        }
        else{
            myViewHolder.disabledTextView.setText("");
        }

        //color codes the sides of the list
        if(role.equals("Student")) {
            myViewHolder.colorTextView.setBackgroundResource(R.color.student);
        }
        if(role.equals("Admin")) {
            myViewHolder.colorTextView.setBackgroundResource(R.color.admin);
        }
    }

    //creates a new ViewHolder everytime one is needed and inflates the individual item's layout
    @NonNull
    @Override
    public AccountsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.accounts_recyclerview_item, parent, false);
        return new AccountsAdapter.MyViewHolder(itemView);
    }
}