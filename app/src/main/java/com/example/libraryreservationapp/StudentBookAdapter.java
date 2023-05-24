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

public class StudentBookAdapter extends RecyclerView.Adapter<StudentBookAdapter.MyViewHolder> {
    Context context;
    List<Book> bookList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public StudentBookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_book, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        holder.txt_book_title.setText(bookList.get(i).getTitle());
        holder.txt_book_author.setText((bookList.get(i).getAuthor()));
        holder.txt_book_isbn.setText((bookList.get(i).getIsbn()));


        if(!cardViewList.contains((holder.card_book))){
            cardViewList.add(holder.card_book);
        }

        holder.setRecyclerBookSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set cards not selected to white
                for(CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                //set selected backgroud color
                holder.card_book.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));

                //Send Broadcast to tell Reserve Book Activity enable Button next
                Intent intent = new Intent(Common.KEY_ENABLE_BOOK_BUTTON_NEXT);
                intent.putExtra(Common.KEY_BOOK_SELECTED, bookList.get(pos));
                intent.putExtra(Common.KEY_BOOK_STEP, 1);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_book_title, txt_book_author, txt_book_isbn;
        CardView card_book;

        RecyclerItemSelectedListener recyclerItemSelectedListener;

        public void setRecyclerBookSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_book = itemView.findViewById(R.id.card_book);
            txt_book_title = (TextView) itemView.findViewById(R.id.txt_book_title);
            txt_book_author = (TextView) itemView.findViewById(R.id.txt_book_author);
            txt_book_isbn = (TextView) itemView.findViewById(R.id.txt_book_isbn);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerItemSelectedListener.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}
