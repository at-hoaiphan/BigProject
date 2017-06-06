package com.example.gio.bigproject.models.directions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Copyright by Gio.
 * Created on 4/20/2017.
 */

@Data
class Step {
    @SerializedName("start_location")
    @Expose
    private StartLocation startLocation;
}
