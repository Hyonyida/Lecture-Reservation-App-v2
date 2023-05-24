package com.example.libraryreservationapp;

public class Orders {

    //private member variables
    public String course;
    public String title;
    public String isbn;
    public String quantity;
    public String status;


    //empty constructor
    public Orders() {

    }

    public Orders(String course, String title, String isbn, String quantity) {
        this.course = course;
        this.title = title;
        this.isbn = isbn;
        this.quantity = quantity;
        this.status = status;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() { return status;}

    public void setStatus(String status) { this.status = status;}
}

