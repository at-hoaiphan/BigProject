package com.example.gio.bigproject.model;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

class MyMarker {
    private int markerId;
    private String markerTitle;
    private double markerLongitude;
    private double markerLatitude;

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }

    public void setMarkerTitle(String markerTitle) {
        this.markerTitle = markerTitle;
    }

    public void setMarkerLongitude(double markerLongitude) {
        this.markerLongitude = markerLongitude;
    }

    public void setMarkerLatitude(double markerLatitude) {
        this.markerLatitude = markerLatitude;
    }

    public int getMarkerId() {

        return markerId;
    }

    public String getMarkerTitle() {
        return markerTitle;
    }

    public double getMarkerLongitude() {
        return markerLongitude;
    }

    public double getMarkerLatitude() {
        return markerLatitude;
    }

    public MyMarker(int markerId, String markerTitle, double markerLatitude, double markerLongitude) {

        this.markerId = markerId;
        this.markerTitle = markerTitle;
        this.markerLongitude = markerLongitude;
        this.markerLatitude = markerLatitude;
    }

}
