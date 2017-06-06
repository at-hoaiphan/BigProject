package com.example.gio.bigproject.models.bus_stops;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Copyright by Gio.
 * Created on 4/17/2017.
 */
@Data
public class Result {
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("photos")
    @Expose
    private List<Photos> photos = null;
}