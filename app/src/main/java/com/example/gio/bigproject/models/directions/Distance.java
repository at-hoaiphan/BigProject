package com.example.gio.bigproject.models.directions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright by Gio.
 * Created on 4/20/2017.
 */

public class Distance {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("value")
    @Expose
    private Double value;

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getText() {

        return text;
    }

    public Double getValue() {
        return value;
    }
}
