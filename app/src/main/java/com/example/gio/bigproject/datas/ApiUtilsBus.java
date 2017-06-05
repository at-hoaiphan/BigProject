package com.example.gio.bigproject.datas;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

public class ApiUtilsBus {
    public static final String KEY = "AIzaSyDvs1qZM4Efpow9sinhsRCZbXIB24NTVcg";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    static SOServiceBus getSOService() {
        return BusRetrofitClient.getClient(BASE_URL).create(SOServiceBus.class);
    }

    public static SOServiceDirection getSOServiceDirection() {
        return BusRetrofitClient.getClient(BASE_URL).create(SOServiceDirection.class);
    }
}
