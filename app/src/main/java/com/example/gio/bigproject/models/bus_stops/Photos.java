package com.example.gio.bigproject.models.bus_stops;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright by Gio.
 * Created on 4/19/2017.
 */

public class Photos {
    @SerializedName("photo_reference")
    @Expose
    private String photoReference;

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getPhotoReference() {

        return photoReference;
    }
}
