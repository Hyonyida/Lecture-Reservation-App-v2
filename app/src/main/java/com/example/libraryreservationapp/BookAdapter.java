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

public class BookAdapter extends FirestoreRecyclerAdapter<Book, BookAdapter.BookHolder> {
    //creates an interface for the listener
    interface BookAdapterListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    private BookAdapterListener listener;

    public void setOnItemClickListener(BookAdapterListener listener){
        this.listener = listener;
    }

    //creates an adapter with the query and configurations that was passed in
    public BookAdapter(@NonNull FirestoreRecyclerOptions<Book> options){
        super(options);
    }

    //specifies the type of ViewHolder for this specific project
    public class BookHolder extends RecyclerView.ViewHolder{
        public TextView courseTextView;
        public TextView titleTextView;

        //constructor for ViewHolder
        public BookHolder(View view){
            super(view);

            //assigns the member variables the correct TextViews
            courseTextView = view.findViewById(R.id.textViewCourse);
            titleTextView = view.findViewById(R.id.textViewTitle);

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
    protected void onBindViewHolder(@NonNull BookHolder bookHolder, int i, @NonNull Book book) {
        String Test_titlex = String.format(book.getTitle());

        // Puts the building and room numbers into the textViews for the position (i)
        bookHolder.courseTextView.setText(book.getCourse());
        bookHolder.titleTextView.setText("Title: " + Test_titlex);
    }

    //creates a new ViewHolder everytime one is needed and inflates the individual item's layout
    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_recyclerview_item, parent, false);
        return new BookHolder(itemView);
    }
}

