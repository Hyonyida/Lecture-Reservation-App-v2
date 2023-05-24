package com.example.libraryreservationapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Client {
    private static Retrofit retrofit=null;

    public static Retrofit getRetrofit(String url)
    {
        if(retrofit == null)
        {
            retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

    public static void getClient(String s) {

    }
}
