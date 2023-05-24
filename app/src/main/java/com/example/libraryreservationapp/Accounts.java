package com.example.libraryreservationapp;

public class Accounts {

    //member variables
    public String email;
    public String rName;

    public String student_id;
    public String type;
    public boolean isDisabled;
    public String reason;


    public Accounts(){
        //empty constructor
    }

    public Accounts(String email, String fName, String lName, String ram_id, String type, boolean isDisabled, String reason) {
        this.email = email;
        this.rName = rName;
        this.student_id = student_id;
        this.type = type;
        this.isDisabled = isDisabled;
        this.reason = reason;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
