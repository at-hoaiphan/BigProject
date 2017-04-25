package com.example.gio.bigproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.gio.bigproject.data.BusStopDatabase;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

class ViewPagerMarkerAdapter extends FragmentStatePagerAdapter{
    private int count;
    private Context context;

    public ViewPagerMarkerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        count = new BusStopDatabase(context).getCountPlaces();
    }

    @Override
    public Fragment getItem(int position) {
        return new ViewPagerMarker().getPosition(position);
    }

    @Override
    public int getCount() { return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return String.valueOf(position);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
