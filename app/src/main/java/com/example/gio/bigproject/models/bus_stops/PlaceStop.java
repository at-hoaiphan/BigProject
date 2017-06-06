package com.example.gio.bigproject.models.bus_stops;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import lombok.Data;

/**
 * Copyright by Gio.
 * Created on 4/24/2017.
 */
@Data
public class PlaceStop implements BaseColumns, Parcelable {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String carriage;
    private String address;

    private PlaceStop(Parcel in) {
        id = in.readInt();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        carriage = in.readString();
        address = in.readString();
    }

    public static final Creator<PlaceStop> CREATOR = new Creator<PlaceStop>() {
        @Override
        public PlaceStop createFromParcel(Parcel in) {
            return new PlaceStop(in);
        }

        @Override
        public PlaceStop[] newArray(int size) {
            return new PlaceStop[size];
        }
    };

    public PlaceStop(int id, String name, double latitude, double longitude, String carriage, String address) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.carriage = carriage;
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(carriage);
        parcel.writeString(address);
    }
}
