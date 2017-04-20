package com.example.gio.bigproject.data;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

public class ApiUtilsBus {
    public static final String KEY = "AIzaSyCWRI3EjJmfetSH3hK-NoZY_wMGPXx5ncg";
    private static final String BASE_URL_PLACE = "https://maps.googleapis.com/maps/api/place/textsearch/";
    private static final String BASE_URL_DIRECTION = "https://maps.googleapis.com/maps/api/directions/";

    public static SOServiceBus getSOService() {
        return BusRetrofitClient.getClient(BASE_URL_PLACE).create(SOServiceBus.class);
    }

    public static SOServiceDirection getSOServiceDirection() {
        return BusRetrofitClient.getClient(BASE_URL_DIRECTION).create(SOServiceDirection.class);
    }
}
