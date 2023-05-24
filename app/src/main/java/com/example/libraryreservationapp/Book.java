package com.example.libraryreservationapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable
{
    //private member variables
    private String bookId;
    private String course;
    private String title;
    private String author;
    private String isbn;
    private boolean available;

    //empty constructor
    public Book()
    {

    }

    //constructor for Book
    public Book(String course, String title, String author, String isbn, boolean available)
    {
        this.course = course;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
    }

    protected Book(Parcel in) {
        course = in.readString();
        title = in.readString();
        author = in.readString();
        isbn = in.readString();
        available = in.readByte() != 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    //gets course
    public String getCourse() {
        return course;
    }

    //sets course
    public void setCourse(String course) {
        this.course = course;
    }

    //gets title
    public String getTitle() {
        return title;
    }

    //sets title
    public void setTitle(String title) {
        this.title = title;
    }

    //gets author
    public String getAuthor() {
        return author;
    }

    //sets author
    public void setAuthor(String author) {
        this.author = author;
    }

    //gets isbn
    public String getIsbn() {
        return isbn;
    }

    //sets isbn
    public void setISBN(String isbn) { this.isbn = isbn; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(course);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(isbn);
        dest.writeByte((byte) (available ? 1 : 0));
    }
}


