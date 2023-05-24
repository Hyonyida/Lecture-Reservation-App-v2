package com.example.libraryreservationapp;

import com.example.libraryreservationapp.MyResponse;
import com.example.libraryreservationapp.NotificationSender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type: application/json",
            "Authorization:key = AAAAF7SKskM:APA91bF5876w1fPNHZ97iovObhJ07Gf0zQMDkloeMwNpftRdcfMLr-CSUWELTVra-S34NsGIoWb-RIiXMJUtsOHvYuFfAAjEDrCP03CQXp6kJZGb2-lk7QkEkoA0ETjGvyPu6EGMVpCw"

    }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
