package com.example.gio.bigproject.model.direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright by Gio.
 * Created on 4/21/2017.
 */

public class OverViewPolyline {
    @SerializedName("points")
    @Expose
    private String points;

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPoints() {

        return points;
    }
}
