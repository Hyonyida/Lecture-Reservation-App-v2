package com.example.libraryreservationapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryreservationapp.Common.Common;
import com.example.libraryreservationapp.Interface.RecyclerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class StudentRoomAdapter extends RecyclerView.Adapter<StudentRoomAdapter.MyViewHolder> {

    Context context;
    List<Room> roomList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public StudentRoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_room, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        holder.txt_room_number.setText("강의실 : "+roomList.get(i).getRoomNumber());
        holder.txt_room_desc.setText("수용 인원 : "+(roomList.get(i).getCapacity()));

        if(roomList.get(i).isComputer())
            holder.img_computer.setColorFilter(context.getResources().getColor(R.color.available));
        if(roomList.get(i).isWifi())
            holder.img_wifi.setColorFilter(context.getResources().getColor(R.color.available));
        if(roomList.get(i).isEngineering())
            holder.img_engineering.setColorFilter(context.getResources().getColor(R.color.available));


        if(!cardViewList.contains((holder.card_room))){
            cardViewList.add(holder.card_room);
        }

        holder.setRecyclerRoomSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set cards not selected to white
                for(CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                //set selected backgroud color
                holder.card_room.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));

                //Send Broadcast to tell Reserve Room Activity enable Button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_ROOM_SELECTED, roomList.get(pos));
                intent.putExtra(Common.KEY_STEP, 1);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_room_number, txt_room_desc;
        ImageView img_computer, img_wifi, img_engineering;
        CardView card_room;

        RecyclerItemSelectedListener recyclerItemSelectedListener;

        public void setRecyclerRoomSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_room = itemView.findViewById(R.id.card_room);
            txt_room_number = (TextView) itemView.findViewById(R.id.text_room_number);
            txt_room_desc = (TextView) itemView.findViewById(R.id.text_room_desc);

            img_computer = itemView.findViewById(R.id.img_computer);
            img_wifi = itemView.findViewById(R.id.img_wifi);
            img_engineering = itemView.findViewById(R.id.img_engineering);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}
