package com.example.gio.bigproject.models;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

class MyMarker {
    private int mMarkerId;
    private String mMarkerTitle;
    private double mMarkerLongitude;
    private double mMarkerLatitude;

    public void setMarkerId(int markerId) {
        this.mMarkerId = markerId;
    }

    public void setMarkerTitle(String markerTitle) {
        this.mMarkerTitle = markerTitle;
    }

    public void setMarkerLongitude(double markerLongitude) {
        this.mMarkerLongitude = markerLongitude;
    }

    public void setMarkerLatitude(double markerLatitude) {
        this.mMarkerLatitude = markerLatitude;
    }

    public int getMarkerId() {

        return mMarkerId;
    }

    public String getMarkerTitle() {
        return mMarkerTitle;
    }

    public double getMarkerLongitude() {
        return mMarkerLongitude;
    }

    public double getMarkerLatitude() {
        return mMarkerLatitude;
    }

    public MyMarker(int markerId, String markerTitle, double markerLatitude, double markerLongitude) {

        this.mMarkerId = markerId;
        this.mMarkerTitle = markerTitle;
        this.mMarkerLongitude = markerLongitude;
        this.mMarkerLatitude = markerLatitude;
    }

}
