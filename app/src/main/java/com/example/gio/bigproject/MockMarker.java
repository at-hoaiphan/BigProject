package com.example.gio.bigproject;

import java.util.ArrayList;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

public class MockMarker {

    public static ArrayList<MyMarker> getData() {
        ArrayList<MyMarker> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new MyMarker(i, "MyMarker " + i, 16.0747 + (double) i*i/2500, 108.23 + (double) i/500));
        }

        return list;
    }

//    public static MyMarker getMarkerById(int id, ArrayList<MyMarker> list) {
//        return list.get(id);
//    }
}
