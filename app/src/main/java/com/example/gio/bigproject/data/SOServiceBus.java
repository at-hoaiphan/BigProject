package com.example.gio.bigproject.data;

import com.example.gio.bigproject.data.model.SOStationsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

public interface SOServiceBus {

    @GET("json?query=xe+buyt&location=16.08,108.22&radius=10000&key=AIzaSyDjJotwDoyLtG6RLKbXhsi56-c9dbjByOg")
    Call<SOStationsResponse> getAnswers();

    @GET("/maps/api/place/textsearch/json?query=xe+buyt&location=16.08,108.22&radius=10000&key=AIzaSyDjJotwDoyLtG6RLKbXhsi56-c9dbjByOg")
    Call<SOStationsResponse> getAnswers(@Query("tagged") String tags);
}
