package com.example.libraryreservationapp;

public class RoomReviews {

    private float rating;
    private String review;
    private String email;

    // Empty Constructor
    public RoomReviews() {
    }

    public RoomReviews(int rating, String review, String email)
    {
        this.rating = rating;
        this.review = review;
        this.email = email;
    }
    public float getRating()

    {
        return rating;
    }


    public String getReview()
    {
        return review;
    }

    public String getEmail()

    {
        return email;
    }
}
