package com.example.gio.bigproject.datas;

import com.example.gio.bigproject.models.directions.SOPlacesDirectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright by Gio.
 * Created on 4/20/2017.
 */

public interface SOServiceDirection {
        @GET("directions/json")
        Call<SOPlacesDirectionResponse> getPlacesDirection(@Query("origin") String origin, @Query("destination") String destination, @Query("mode") String walking_bicycling_driving_transit, @Query("key") String KEY);

        @GET("directions/json?origin=16.08,108.22&destination=16.078,108.2&key=AIzaSyCWRI3EjJmfetSH3hK-NoZY_wMGPXx5ncg")
        Call<SOPlacesDirectionResponse> getPlacesDirection();
}
