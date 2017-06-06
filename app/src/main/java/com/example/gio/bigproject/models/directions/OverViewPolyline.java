package com.example.gio.bigproject.models.directions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Copyright by Gio.
 * Created on 4/21/2017.
 */

@Data
public class OverViewPolyline {
    @SerializedName("points")
    @Expose
    private String points;
}
