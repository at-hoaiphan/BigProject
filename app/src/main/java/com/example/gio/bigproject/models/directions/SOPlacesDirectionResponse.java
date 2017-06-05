package com.example.gio.bigproject.models.directions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Copyright by Gio.
 * Created on 4/20/2017.
 */

public class SOPlacesDirectionResponse {
    @SerializedName("routes")
    @Expose
    private List<RouteDirec> routes = null;
    @SerializedName("status")
    @Expose
    private String status;

    public void setRoutes(List<RouteDirec> routes) {
        this.routes = routes;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public List<RouteDirec> getRoutes() {
        return routes;
    }

    public String getStatus() {
        return status;
    }
}
