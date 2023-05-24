package com.example.libraryreservationapp.Common;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RoomItemDecoration extends RecyclerView.ItemDecoration {
    private int item;
    public RoomItemDecoration(int i) {
        this.item = i;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = outRect.top = outRect.bottom = outRect.right = item;
    }
}
