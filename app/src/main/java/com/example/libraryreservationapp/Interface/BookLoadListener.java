package com.example.libraryreservationapp.Interface;

import com.example.libraryreservationapp.Book;

import java.util.List;

public interface BookLoadListener {
    void onAllBookLoadSuccess(List<Book> bookList);
    void onAllBookLoadFailed(String message);
}
