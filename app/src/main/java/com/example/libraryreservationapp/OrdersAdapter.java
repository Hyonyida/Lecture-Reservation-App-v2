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

public class OrdersAdapter extends FirestoreRecyclerAdapter<Orders, OrdersAdapter.OrdersHolder>
{

    //creates an interface for the listener
    interface OrdersAdapterListener
    { void onItemClick(DocumentSnapshot documentSnapshot, int position); }
    private OrdersAdapter.OrdersAdapterListener listener;
    public void setOnItemClickListener(OrdersAdapter.OrdersAdapterListener listener)
    { this.listener = listener; }

    //creates an adapter with the query and configurations that was passed in
    public OrdersAdapter(@NonNull FirestoreRecyclerOptions<Orders> options)
    {
        super(options);
    }

    // Specifies the type of ViewHolder for this specific project
    public class OrdersHolder extends RecyclerView.ViewHolder
    {
        public TextView courseTextView;
        public TextView titleTextView;
        public TextView isbnTextView;
        public TextView quantityTextView;
        public TextView statusTextView;

        // Constructor for ViewHolder
        public OrdersHolder(View view){
            super(view);

            // Assigns the member variables the correct TextViews
            courseTextView = view.findViewById(R.id.textViewCourse);
            titleTextView = view.findViewById(R.id.textViewTitle);
            isbnTextView = view.findViewById(R.id.textViewIsbn);
            quantityTextView = view.findViewById(R.id.textViewQuantity);
            statusTextView = view.findViewById(R.id.textViewStatus);


            //onClickListener for the items in the recyclerView
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Gets the position of the clicked
                    int position = getAdapterPosition();
                    // Makes sure the position is valid and listener exists
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }

    @Override
    //binds the correct item into the recyclerView
    protected void onBindViewHolder(@NonNull OrdersHolder ordersHolder, int i, @NonNull Orders orders)
    {
        String Test_titlex = String.format(orders.getTitle());

        // Puts the variables into the textViews for the position (i)
        ordersHolder.courseTextView.setText("Course: " + orders.getCourse());
        ordersHolder.titleTextView.setText("Title: " + Test_titlex);
        ordersHolder.quantityTextView.setText("Quantity: " + orders.getQuantity());
        ordersHolder.isbnTextView.setText("ISBN: " + orders.getIsbn());
        ordersHolder.statusTextView.setText("Status: " + orders.getStatus());
    }

    //creates a new ViewHolder every time one is needed and inflates the individual item's layout
    @NonNull @Override
    public OrdersAdapter.OrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_recyclerview_item, parent, false);
        return new OrdersAdapter.OrdersHolder(itemView);
    }
} //END of class OrdersAdapter extends FirestoreRecyclerAdapter<Orders,
