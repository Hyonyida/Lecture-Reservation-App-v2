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

public class RoomReservationAdapter extends FirestoreRecyclerAdapter<RoomReservationInformation, RoomReservationAdapter.MyViewHolder> {
    //creates an interface for the listener
    interface RoomReservationAdapterListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    private RoomReservationAdapter.RoomReservationAdapterListener listener;

    public void setOnItemClickListener(RoomReservationAdapter.RoomReservationAdapterListener listener){
        this.listener = listener;
    }

    //creates an adapter with the query and configurations that was passed in
    public RoomReservationAdapter(@NonNull FirestoreRecyclerOptions<RoomReservationInformation> options){
        super(options);
    }

    //specifies the type of ViewHolder for this specific project
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTimeTextView;
        public TextView buildingRoomNumberTextView;
        public ImageView wifiImageView;
        public ImageView computerImageView;
        public ImageView engineeringImageView;
        public FirebaseFirestore fStore;

        //constructor for ViewHolder
        public MyViewHolder(View view){
            super(view);

            //assigns the member variables the correct TextViews and imageViews
            dateTimeTextView = view.findViewById(R.id.textViewReservationDateTime);
            buildingRoomNumberTextView = view.findViewById(R.id.textViewReservationBuildingRoomNumber);
            wifiImageView = view.findViewById(R.id.imageViewReservationWifi);
            computerImageView = view.findViewById(R.id.imageViewReservationComputer);
            engineeringImageView = view.findViewById(R.id.imageViewReservationEngineering);

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
    protected void onBindViewHolder(@NonNull final RoomReservationAdapter.MyViewHolder myViewHolder, int i, @NonNull RoomReservationInformation info) {

        // Puts the information into the textViews for the position (i)
        myViewHolder.dateTimeTextView.setText(info.getDate() + " at " + info.getTime());
        myViewHolder.buildingRoomNumberTextView.setText(info.getBuilding() + " 강의실 : " + info.getRoomNumber());

        myViewHolder.fStore.collection("room").document(info.getRoomId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////        myViewHolder.fStore.collection("room").document("HB4SZJsxEeymqpbRsYp9").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if(task.isSuccessful()){
                   //gets the document with the correct document path
                   DocumentSnapshot documentSnapshot = task.getResult();
                   //gets the info about the room that was reserved
                   Boolean wifi = (Boolean) documentSnapshot.get("wifi");
                   Boolean whiteboard = (Boolean) documentSnapshot.get("whiteboard");
                   Boolean computer = (Boolean) documentSnapshot.get("computer");

                   //puts the correct image depending on if the wifi value is true or false for the room
                   if(wifi){
                       myViewHolder.wifiImageView.setImageResource(R.drawable.ic_baseline_wifi_24_green);
                   }
                   else{
                       myViewHolder.wifiImageView.setImageResource(R.drawable.ic_baseline_wifi_24_black);
                   }

                   //puts the correct image depending on if the computer value is true or false for the room
                   if(computer){
                       myViewHolder.computerImageView.setImageResource(R.drawable.ic_baseline_laptop_chromebook_24_green);
                   }
                   else{
                       myViewHolder.computerImageView.setImageResource(R.drawable.ic_baseline_laptop_chromebook_24_black);
                   }

                   //puts the correct image depending on if the whiteboard value is true or false for the room
                   if(whiteboard){
                       myViewHolder.engineeringImageView.setImageResource(R.drawable.ic_baseline_engineering_24_green);
                   }
                   else{
                       myViewHolder.engineeringImageView.setImageResource(R.drawable.ic_baseline_engineering_24_black);
                   }
               }
               else{
                   Log.d("MYDEBUG", "The room was not found in the database");
               }
           }
       });



    }

    //creates a new ViewHolder everytime one is needed and inflates the individual item's layout
    @NonNull
    @Override
    public RoomReservationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Parent is the recyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_room_reservation_recyclerview_item, parent, false);
        return new RoomReservationAdapter.MyViewHolder(itemView);
    }
}
