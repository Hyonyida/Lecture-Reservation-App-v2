package com.example.libraryreservationapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class RoomAdapter extends FirestoreRecyclerAdapter<Room, RoomAdapter.MyViewHolder>{
    //creates an interface for the listener
    interface RoomAdapterListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    private RoomAdapterListener listener;

    public void setOnItemClickListener(RoomAdapterListener listener){
        this.listener = listener;
    }

    //creates an adapter with the query and configurations that was passed in
    public RoomAdapter(@NonNull FirestoreRecyclerOptions<Room> options){
        super(options);
    }

    //specifies the type of ViewHolder for this specific project
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView buildingTextView;
        public TextView roomNumberTextView;
        public TextView capacityTextView;
        public ImageView wifiImageView;
        public ImageView computerImageView;
        public ImageView engineeringImageView;
        public ImageView availableImageView;

        //constructor for ViewHolder
        public MyViewHolder(View view){
            super(view);

            //assigns the member variables the correct TextViews and imageViews
            buildingTextView = view.findViewById(R.id.textViewBuilding);
            roomNumberTextView = view.findViewById(R.id.textViewRoomNumber);
            capacityTextView = view.findViewById(R.id.textViewCapacity);
            wifiImageView = view.findViewById(R.id.imageViewWifi);
            computerImageView = view.findViewById(R.id.imageViewComputer);
            engineeringImageView = view.findViewById(R.id.imageViewWhiteboard);
            availableImageView = view.findViewById(R.id.imageViewAvailable);


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
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Room room) {
        String sRoomNumber = Integer.toString(room.getRoomNumber());
        String sCapacity = Integer.toString(room.getCapacity());

        // Puts the building, capacity, and room numbers into the textViews for the position (i)
        myViewHolder.buildingTextView.setText(room.getBuilding());
        myViewHolder.roomNumberTextView.setText("강의실 번호: " + sRoomNumber);
        myViewHolder.capacityTextView.setText("수용 인원 : " + sCapacity);

//        if(room.isWifi()){
//            myViewHolder.wifiImageView.setVisibility(View.VISIBLE);
//            myViewHolder.wifiImageView.setColorFilter(Color.rgb(50,205,50));
//        }
//        else{
//            myViewHolder.wifiImageView.setVisibility(View.GONE);
//        }

        //puts the correct image depending on if the wifi value is true or false for the room
        if(room.isWifi()){
            myViewHolder.wifiImageView.setImageResource(R.drawable.ic_baseline_wifi_24_green);
        }
        else{
            myViewHolder.wifiImageView.setImageResource(R.drawable.ic_baseline_wifi_24_black);
        }

        //puts the correct image depending on if the computer value is true or false for the room
        if(room.isComputer()){
            myViewHolder.computerImageView.setImageResource(R.drawable.ic_baseline_laptop_chromebook_24_green);
        }
        else{
            myViewHolder.computerImageView.setImageResource(R.drawable.ic_baseline_laptop_chromebook_24_black);
        }

        //puts the correct image depending on if the whiteboard value is true or false for the room
        if(room.isEngineering()){
            myViewHolder.engineeringImageView.setImageResource(R.drawable.ic_baseline_engineering_24_green);
        }
        else{
            myViewHolder.engineeringImageView.setImageResource(R.drawable.ic_baseline_engineering_24_black);
        }

        //puts the correct image depending on if the available value is true or false for the room
        if(room.isAvailable()){
            myViewHolder.availableImageView.setImageResource(R.drawable.ic_baseline_lock_open_24_green);
        }
        else{
            myViewHolder.availableImageView.setImageResource(R.drawable.ic_baseline_lock_24_black);
        }

    }

    //creates a new ViewHolder everytime one is needed and inflates the individual item's layout
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_recyclerview_item, parent, false);
        return new MyViewHolder(itemView);
    }
}
