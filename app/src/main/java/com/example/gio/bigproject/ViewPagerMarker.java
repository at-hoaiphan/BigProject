package com.example.gio.bigproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.example.gio.bigproject.data.model.Result;

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

    public ViewPagerMarker() {
    }

    @AfterViews
    void afterViews() {
        int position = 0;

        if (getArguments() != null) {
            position = getArguments().getInt("positionFrag");
        }

//        // Get data from Json
//        SOServiceBus mService = ApiUtilsBus.getSOService();
//        final int finalPosition = position;
//        mService.getAnswers().enqueue(new Callback<SOStationsResponse>() {
//            @Override
//            public void onResponse(Call<SOStationsResponse> call, Response<SOStationsResponse> response) {
//
//                if (response.isSuccessful()) {
//                    mResults = (ArrayList<Result>) response.body().getResults();
//                    Log.d("ViewPage sizeResult", "loaded API" + response.body().getResults().size());
//
//                } else {
////                    int statusCode  = response.code();
//                    Log.d("MainActivity", "posts didn't load from API: ");
//                    // handle request errors depending on status code
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SOStationsResponse> call, Throwable t) {
////                showErrorMessage();
//                Log.d("", "onFailure: " +call.request().url().toString());
//                Log.d("MainActivity", "error loading from API");
//
//            }
//        });
//        if (mResults.size() > 0) {
//            Result mResult = mResults.get(finalPosition);
//            tvMarkerTitle.setText(mResult.getName());
//            tvmarkerLongLat.setText(String.valueOf("Lat: " + mResult.getGeometry().getLocation().getLat())
//                    + "; Long: " + String.valueOf(mResult.getGeometry().getLocation().getLng()));
//        }

        ArrayList<Result> mResults = MockMarker.getData();
        Log.d("ViewPagerMarker", "afterViews: " + MockMarker.getData().size());
        if (mResults.size() > 0) {
            tvMarkerTitle.setText(mResults.get(position).getName());
            tvmarkerLongLat.setText(String.valueOf("Lat: " + mResults.get(position).getGeometry().getLocation().getLat())
                    + "; Long: " + String.valueOf(mResults.get(position).getGeometry().getLocation().getLng()));
        }
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
