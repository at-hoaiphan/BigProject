package com.example.gio.bigproject.models.bus_stops;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Copyright by Gio.
 * Created on 4/17/2017.
 */
@Data
class Geometry {
    @SerializedName("location")
    @Expose
    private LocaBus location;
}