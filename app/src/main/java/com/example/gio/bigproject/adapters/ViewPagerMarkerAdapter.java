package com.example.gio.bigproject.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.gio.bigproject.fragments.ViewPagerMarker;
import com.example.gio.bigproject.models.bus_stops.PlaceStop;

import java.util.List;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

public class ViewPagerMarkerAdapter extends FragmentStatePagerAdapter{
    private List<PlaceStop> mPlaceStops;
    private Context mContext;
    public ViewPagerMarkerAdapter(Context context, FragmentManager fm, List<PlaceStop> placeStops) {
        super(fm);
        this.mContext = context;
        this.mPlaceStops = placeStops;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("TAG", "getItem: " + mPlaceStops.get(position));
        return new ViewPagerMarker().newInstance(mPlaceStops.get(position));
    }

    @Override
    public int getCount() {
        return mPlaceStops.size();
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
