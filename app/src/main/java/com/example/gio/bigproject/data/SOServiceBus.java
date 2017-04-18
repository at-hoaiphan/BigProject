package com.example.gio.bigproject.data;

import com.example.gio.bigproject.data.model.SOStationsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

public interface SOServiceBus {
    public static final double LATITUDE = 16.08;
    public static final double LONGITUDE = 108.22;
    public static final String QUERY = "tram xe buyt";

    @GET("json?query=" + QUERY + "&location=" + LATITUDE +"," + LONGITUDE + "&key=AIzaSyDY_ANoasVU2WmFVmYfq9fK0sCbxj9Ivhc")
    Call<SOStationsResponse> getAnswers();
}
