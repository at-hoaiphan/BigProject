package com.example.gio.bigproject.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.gio.bigproject.ViewPagerMarker;
import com.example.gio.bigproject.model.bus_stop.PlaceStop;

import java.util.ArrayList;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

public class ViewPagerMarkerAdapter extends FragmentStatePagerAdapter{
    private ArrayList<PlaceStop> mPlaceStops;
    private Context context;

    public ViewPagerMarkerAdapter(Context context, FragmentManager fm, ArrayList<PlaceStop> placeStops) {
        super(fm);
        this.context = context;
        this.mPlaceStops = placeStops;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("TAG", "getItem: " + mPlaceStops.get(position));
        return new ViewPagerMarker().newInstance(mPlaceStops.get(position));
    }

    @Override
    public int getCount() { return mPlaceStops.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return String.valueOf(position);
    }

    @Override
    public int getItemPosition(Object object) {
        Log.d("ViewPager check ", "getItemPosition: " + mPlaceStops.size());
        return POSITION_NONE;
    }
}
