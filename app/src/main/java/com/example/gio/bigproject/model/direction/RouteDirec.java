package com.example.gio.bigproject.model.direction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Copyright by Gio.
 * Created on 4/20/2017.
 */

public class RouteDirec {
    @SerializedName("legs")
    @Expose
    private List<Legs> legs = null;

    public void setOverViewPolyline(OverViewPolyline overViewPolyline) {
        this.overViewPolyline = overViewPolyline;
    }

    public OverViewPolyline getOverViewPolyline() {

        return overViewPolyline;
    }

    @SerializedName("overview_polyline")
    @Expose
    private OverViewPolyline overViewPolyline;

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }


    public List<Legs> getLegs() {

        return legs;
    }
}
