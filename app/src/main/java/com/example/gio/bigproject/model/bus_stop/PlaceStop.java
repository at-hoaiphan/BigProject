package com.example.gio.bigproject.model.bus_stop;

import android.provider.BaseColumns;

/**
 * Copyright by Gio.
 * Created on 4/24/2017.
 */

public class PlaceStop implements BaseColumns{
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String carriage;
    private String address;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCarriage() {
        return carriage;
    }

    public String getAddress() {
        return address;
    }

    public PlaceStop(int id, String name, double latitude, double longitude, String carriage, String address) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.carriage = carriage;
        this.address = address;
    }
}
