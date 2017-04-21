package com.example.gio.bigproject.model.direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright by Gio.
 * Created on 4/20/2017.
 */

public class Step {
    @SerializedName("start_location")
    @Expose
    private StartLocation startLocation;

    public void setStartLocation(StartLocation startLocation) {
        this.startLocation = startLocation;
    }

    public StartLocation getStartLocation() {

        return startLocation;
    }
}
