package com.example.gio.bigproject.models.directions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Copyright by Gio.
 * Created on 4/20/2017.
 */
@Data
public class RouteDirec {
    @SerializedName("legs")
    @Expose
    private List<Legs> legs = null;
    @SerializedName("overview_polyline")
    @Expose
    private OverViewPolyline overViewPolyline;
}
