package com.example.gio.bigproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

@EFragment(R.layout.google_map_detail_marker)
public class ViewPagerMarker extends Fragment {

    @ViewById(R.id.tvMarkerTitle)
    TextView tvMarkerTitle;
    @ViewById(R.id.tvMarkerLongLat)
    TextView tvmarkerLongLat;

    private ArrayList<MyMarker> mMyMarkers = new ArrayList<>();

    public ViewPagerMarker() {
    }

    @AfterViews
    void afterViews() {
        int position = 0;

        if(getArguments()!=null){
            position = getArguments().getInt("positionFrag");
        }

        // Replace item on fragment
        mMyMarkers.addAll(MockMarker.getData());
        MyMarker mMyMarker = mMyMarkers.get(position);
        tvMarkerTitle.setText(mMyMarker.getMarkerTitle());
        tvmarkerLongLat.setText(String.valueOf("Lat: " + mMyMarker.getMarkerLatitude())
                + "; Long: " + String.valueOf(mMyMarker.getMarkerLongitude()));
    }

    public ViewPagerMarker getPosition(int position) {
        ViewPagerMarker fragment = new ViewPagerMarker_();
        Bundle bundle = new Bundle();
        bundle.putInt("positionFrag", position);
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.google_map_detail_marker, container, false);
//
//        TextView tvMarkerTitle = (TextView) view.findViewById(R.id.tvMarkerTitle);
//        TextView tvmarkerLongLat = (TextView) view.findViewById(R.id.tvMarkerLongLat);
//        int position = 0;
//
//        if(getArguments()!=null){
//            position = getArguments().getInt("positionFrag");
//        }
//
//        // Replace item on fragment
//        mMyMarkers.addAll(MockMarker.getData());
//        MyMarker mMyMarker = mMyMarkers.get(position);
//        tvMarkerTitle.setText(mMyMarker.getMarkerTitle());
//        tvmarkerLongLat.setText(String.valueOf("Lat: " + mMyMarker.getMarkerLatitude())
//                + "; Long: " + String.valueOf(mMyMarker.getMarkerLongitude()));
//
//        return view;
//    }
}
