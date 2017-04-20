package com.example.gio.bigproject.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright by Gio.
 * Created on 4/20/2017.
 */

public interface SOServiceDirection {
        @GET("json")
        Call<SOServiceDirection> getPlacesDirection(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String KEY);
}
