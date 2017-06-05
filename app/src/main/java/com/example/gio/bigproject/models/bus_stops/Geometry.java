package com.example.gio.bigproject.models.bus_stops;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright by Gio.
 * Created on 4/17/2017.
 */

public class Geometry {
        @SerializedName("location")
        @Expose
        private LocaBus location;

    public LocaBus getLocation() {
        return location;
    }

    public void setLocation(LocaBus location) {
        this.location = location;
    }
    }