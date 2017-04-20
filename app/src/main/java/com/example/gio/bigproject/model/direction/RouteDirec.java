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
    @SerializedName("overview_polyline")
    @Expose
    private String overViewPolyline;

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }

    public void setOverViewPolyline(String overViewPolyline) {
        this.overViewPolyline = overViewPolyline;
    }

    public List<Legs> getLegs() {

        return legs;
    }

    public String getOverViewPolyline() {
        return overViewPolyline;
    }


}
