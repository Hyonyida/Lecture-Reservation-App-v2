package com.example.libraryreservationapp;

class NotificationSender {

    public Request request;
    public String to;
    public NotificationSender(Request request, String to) { this.request = request; this.to = to; }
    public NotificationSender() {
    }
}
