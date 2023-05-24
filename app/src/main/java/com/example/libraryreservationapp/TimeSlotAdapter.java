package com.example.libraryreservationapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryreservationapp.Common.Common;
import com.example.libraryreservationapp.Interface.RecyclerItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {
    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    RecyclerItemSelectedListener recyclerItemSelectedListener;

    public TimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_time_slot, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());


            if (timeSlotList.size() == 0) // if all positions are available, show list
            {
                if (!holder.txt_time_slot.getText().toString().equals(String.valueOf(R.string.closed))) {
                    //if all time slots empty, all cards enabled
                    holder.card_time_slot.setEnabled(true);

                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                    holder.txt_time_slot_description.setText("Available");
                    holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
                } else{
                    setClosedTimes(holder);
                }
            } else // some time is reserved
            {
                if (!holder.txt_time_slot.getText().toString().equals(String.valueOf(R.string.closed))) {
                    for (TimeSlot slotValue : timeSlotList) { // list of time slots that are full
                        int slot = Integer.parseInt(slotValue.getSlot().toString());
                        if (slot == position) {
                            holder.card_time_slot.setEnabled(false);
                            holder.card_time_slot.setTag(Common.DISABLE_TAG);
                            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                            holder.txt_time_slot_description.setText("Full");
                            holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.white));
                            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
                        }
                    }
                } else{
                    setClosedTimes(holder);
                }
            }

            //add available time slots to list
            if (!cardViewList.contains(holder.card_time_slot)) {
                cardViewList.add(holder.card_time_slot);
            }

            //check if card time slot is available
            if (!timeSlotList.contains(position)) {
                holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
                    @Override
                    public void onItemSelectedListener(View view, int pos) {
                        //loop through cards in card list
                        for (CardView cardView : cardViewList) {
                            if (cardView.getTag() == null) //only available cards will be altered
                                cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                        }
                        //selected card will change color
                        holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));

                        //Now send broadcast message to enable button Next
                        Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                        intent.putExtra(Common.KEY_TIME_SLOT, position); // put index of time slot we have selected
                        intent.putExtra(Common.KEY_STEP, 2); // go to step 2
                        localBroadcastManager.sendBroadcast(intent);
                    }
                });
            }

    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public void setClosedTimes(MyViewHolder holder){
        holder.card_time_slot.setEnabled(false);
        holder.card_time_slot.setTag(Common.DISABLE_TAG);
        holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        holder.txt_time_slot.setText(R.string.closed);
        holder.txt_time_slot_description.setText("");
        holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.white));
        holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;

        RecyclerItemSelectedListener recyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
        }
    }

}
