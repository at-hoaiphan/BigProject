package com.example.gio.bigproject.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright by Gio.
 * Created on 4/17/2017.
 */

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
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}