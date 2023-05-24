package com.example.libraryreservationapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.libraryreservationapp.Fragments.BooksStep1Fragment;
import com.example.libraryreservationapp.Fragments.BooksStep2Fragment;
import com.example.libraryreservationapp.Fragments.BooksStep3Fragment;

public class SelectBookAdapter extends FragmentStatePagerAdapter {

    public SelectBookAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return BooksStep1Fragment.getInstance();
            case 1:
                return BooksStep2Fragment.getInstance();
            case 2:
                return BooksStep3Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
