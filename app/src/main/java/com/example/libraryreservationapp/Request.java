package com.example.libraryreservationapp;

class Request {

    //private member variables
    public String title;
    public String course;
    public String quantity;
    public String isbn;

    //empty constructor
    public Request()
    {

    }

    //constructor for Request
    public Request(String title ,String course, String isbn, String quantity)
    {
        this.title = title;
        this.course = course;
        this.isbn = isbn;
        this.quantity = quantity;
    }

    //gets title
    public String getTitle() {
        return title;
    }

    // sets title
    public void setTitle(String title) {
        this.title = title;
    }

    //gets course
    public String getCourse() {
        return course;
    }

    //sets course
    public void setCourse(String course) {
        this.course = course;
    }

    // gets quantity
    public String getQuantity() {
        return quantity;
    }

    // sets quantity
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    // gets isbn
    public String getIsbn() {
        return isbn;
    }

    // sets isbn
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }



}
