package com.example.gio.bigproject.data;

import com.example.gio.bigproject.model.bus_stop.SOStationsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

public interface SOServiceBus {

    @GET("place/textsearch/json")
    Call<SOStationsResponse> getBusStop(@Query("query") String query, @Query("location") String location, @Query("key") String KEY);


    @GET("place/textsearch/json?query=tram+xe+buyt&location=16.08,108.22&key=AIzaSyCWRI3EjJmfetSH3hK-NoZY_wMGPXx5ncg")
    Call<SOStationsResponse> getBusStop();
}
