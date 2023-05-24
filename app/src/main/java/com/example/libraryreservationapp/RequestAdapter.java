package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RequestAdapter extends FirestoreRecyclerAdapter<Request, RequestAdapter.RequestHolder>{

    // creates an interface for the listener
    interface RequestAdapterListener
    {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    private RequestAdapterListener listener;

    public void setOnItemClickListener(RequestAdapterListener listener){ this.listener = listener; }

    // creates an adapter with the query and configuration that was passed in
    public RequestAdapter(@NonNull FirestoreRecyclerOptions<Request> options) {super(options);}


    //specifies the typer of ViewHolder for this specific project
    public class RequestHolder extends RecyclerView.ViewHolder
    {

        public TextView classNameView;
        public TextView bookNameView;

        //constructor for ViewHolder
        public RequestHolder(View view)
        {
            super(view);

            // assign the member variables the correct TextViews
            classNameView = view.findViewById(R.id.textViewClassName);
            bookNameView = view.findViewById(R.id.textViewBooksName);

            //onClickListener for the items in the recyclerView
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    //gets the position of the clicked
                    int position =getAdapterPosition();
                    //makes sure the position is valid and listener exists
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }
    // binds the correct item into the recyclerView
    @Override
    protected void onBindViewHolder(@NonNull RequestHolder requestHolder, int i, @NonNull Request request)
    {
        String Test_bookTitle = String.format(request.getTitle());

        // puts the class name and book name into the textViews for the position(i)
        requestHolder.classNameView.setText(request.getCourse());
        requestHolder.bookNameView.setText("Title: " + Test_bookTitle);
    }

    //creates a new ViewHolder everytime one is needed and inflates the individual item's layout
    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_recyclerview_item, parent, false);
        return new RequestHolder(itemView);
    }

}

