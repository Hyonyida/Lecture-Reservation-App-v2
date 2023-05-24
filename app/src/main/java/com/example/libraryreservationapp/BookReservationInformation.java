package com.example.libraryreservationapp;

public class BookReservationInformation {
    public String time, date, title, author, isbn, bookId, userId, reservationId;
    public Long slot;

    public BookReservationInformation() {
    }

    public BookReservationInformation(String time, String date, String title, String author, String isbn, String bookId, String userId, String reservationId, Long slot) {
        this.time = time;
        this.date = date;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.bookId = bookId;
        this.userId = userId;
        this.reservationId = reservationId;
        this.slot = slot;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
