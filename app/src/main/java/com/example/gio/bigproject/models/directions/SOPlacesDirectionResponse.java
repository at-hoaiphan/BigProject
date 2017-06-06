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
public class SOPlacesDirectionResponse {
    @SerializedName("routes")
    @Expose
    private List<RouteDirec> routes = null;
    @SerializedName("status")
    @Expose
    private String status;
}
