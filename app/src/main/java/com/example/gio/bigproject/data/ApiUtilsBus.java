package com.example.gio.bigproject.data;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

public class ApiUtilsBus {
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/";

    public static SOServiceBus getSOService() {
        return BusRetrofitClient.getClient(BASE_URL).create(SOServiceBus.class);
    }
}
