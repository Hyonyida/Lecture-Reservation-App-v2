package com.example.libraryreservationapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryreservationapp.Common.Common;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookReservationAdapter extends FirestoreRecyclerAdapter<BookReservationInformation, BookReservationAdapter.MyViewHolder>{
    //creates an interface for the listener
    interface BookReservationAdapterListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    private BookReservationAdapter.BookReservationAdapterListener listener;

    public void setOnItemClickListener(BookReservationAdapter.BookReservationAdapterListener listener){
        this.listener = listener;
    }

    //creates an adapter with the query and configurations that was passed in
    public BookReservationAdapter(@NonNull FirestoreRecyclerOptions<BookReservationInformation> options){
        super(options);
    }

    //specifies the type of ViewHolder for this specific project
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTimeTextView;
        public TextView bookTitleTextView;;
        public FirebaseFirestore fStore;

        //constructor for ViewHolder
        public MyViewHolder(View view){
            super(view);

            //assigns the member variables the correct TextViews and imageViews
            dateTimeTextView = view.findViewById(R.id.textViewReservationDateTime);
            bookTitleTextView = view.findViewById(R.id.textViewBookTitle);

            //gets the instance of the database
            fStore = FirebaseFirestore.getInstance();


            //onClickListener for the items in the recyclerView
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gets the position of thel clicked
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
    protected void onBindViewHolder(@NonNull final BookReservationAdapter.MyViewHolder myViewHolder, int i, @NonNull BookReservationInformation info) {

        // Puts the information into the textViews for the position (i)
        myViewHolder.dateTimeTextView.setText(info.getDate() + " at " + info.getTime());
        myViewHolder.bookTitleTextView.setText(info.getTitle());
        String bookID = info.getBookId();

       myViewHolder.fStore.collection("books").document(info.getBookId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            ////        myViewHolder.fStore.collection("room").document("HB4SZJsxEeymqpbRsYp9").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //gets the document with the correct document path
                    DocumentSnapshot documentSnapshot = task.getResult();
                }
                else{
                    Log.d("MYDEBUG", "The book was not found in the database");
                }
            }
        });



    }

    //creates a new ViewHolder everytime one is needed and inflates the individual item's layout
    @NonNull
    @Override
    public BookReservationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_book_reservation_recyclerview_item, parent, false);
        return new BookReservationAdapter.MyViewHolder(itemView);
    }

}
