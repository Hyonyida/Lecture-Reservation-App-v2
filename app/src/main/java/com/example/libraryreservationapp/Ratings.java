package com.example.libraryreservationapp;

import com.google.protobuf.Empty;

public class Ratings {

    private String ratings;
    private String ratingBar;

    private String txtReviewRoom;
    private String radionBusiness, radionCampus,
            radioConklin,
            radioGleeson,
            radioGreenley,
            radioHale,
            radioLupton,
            radionSinclair,
            radioThompson,
            radioWhitman;


    // Constructor
    public Ratings(String ratings, String ratingBar, String txtReviewRoom,
                   String radionBusiness, String radionCampus, String radioConklin,
                   String radioGleeson, String radioGreenley, String radioHale, String radioLupton,
                   String radionSinclair, String radioThompson, String radioWhitman)
    {
        this.ratings = ratings;
        this.ratingBar = ratingBar;
        this.txtReviewRoom = txtReviewRoom;
        this.radionBusiness = radionBusiness;
        this.radionCampus = radionCampus;
        this.radioConklin = radioConklin;
        this.radioGleeson = radioGleeson;
        this.radioGreenley = radioGreenley;
        this.radioHale = radioHale;
        this.radioLupton = radioLupton;
        this.radionSinclair = radionSinclair;
        this.radioThompson = radioThompson;
        this.radioWhitman = radioWhitman;
    }
    // Empty Constructor
    public Ratings() {  }


    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(String ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getTxtReviewRoom() {
        return txtReviewRoom;
    }

    public void setTxtReviewRoom(String txtReviewRoom) {
        this.txtReviewRoom = txtReviewRoom;
    }

    public String getRadionBusiness() {
        return radionBusiness;
    }

    public void setRadionBusiness(String radionBusiness) {
        this.radionBusiness = radionBusiness;
    }

    public String getRadionCampus() {
        return radionCampus;
    }

    public void setRadionCampus(String radionCampus) {
        this.radionCampus = radionCampus;
    }

    public String getRadioConklin() {
        return radioConklin;
    }

    public void setRadioConklin(String radioConklin) {
        this.radioConklin = radioConklin;
    }

    public String getRadioGleeson() {
        return radioGleeson;
    }

    public void setRadioGleeson(String radioGleeson) {
        this.radioGleeson = radioGleeson;
    }

    public String getRadioGreenley() {
        return radioGreenley;
    }

    public void setRadioGreenley(String radioGreenley) {
        this.radioGreenley = radioGreenley;
    }

    public String getRadioHale() {
        return radioHale;
    }

    public void setRadioHale(String radioHale) {
        this.radioHale = radioHale;
    }

    public String getRadioLupton() {
        return radioLupton;
    }

    public void setRadioLupton(String radioLupton) {
        this.radioLupton = radioLupton;
    }

    public String getRadionSinclair() {
        return radionSinclair;
    }

    public void setRadionSinclair(String radionSinclair) {
        this.radionSinclair = radionSinclair;
    }

    public String getRadioThompson() {
        return radioThompson;
    }

    public void setRadioThompson(String radioThompson) {
        this.radioThompson = radioThompson;
    }

    public String getRadioWhitman() {
        return radioWhitman;
    }

    public void setRadioWhitman(String radioWhitman) {
        this.radioWhitman = radioWhitman;
    }
}
