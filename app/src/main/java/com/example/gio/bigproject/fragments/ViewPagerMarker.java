package com.example.gio.bigproject.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gio.bigproject.R;
import com.example.gio.bigproject.SettingsInterface_;
import com.example.gio.bigproject.activities.MapActivity;
import com.example.gio.bigproject.models.bus_stops.PlaceStop;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Objects;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

@EFragment(R.layout.fragment_google_map_detail_marker)
public class ViewPagerMarker extends Fragment {
    private static final String OBJECT = "object";
    @ViewById(R.id.tvMarkerTitle)
    TextView mTextViewMarkerTitle;
    @ViewById(R.id.tvMarkerAddress)
    TextView mTextViewMarkerAddress;
    @ViewById(R.id.imgLocation)
    ImageView mImageLocaton;

    @Pref
    SettingsInterface_ mSettingsInterface;
//    private String referencePhotoLink = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";

    public ViewPagerMarker() {
    }

    @AfterViews
    void afterViews() {
        PlaceStop placeStop = getArguments().getParcelable(OBJECT);
        if (placeStop != null) {
            mTextViewMarkerTitle.setText(placeStop.getName());
            mTextViewMarkerAddress.setText(String.valueOf("Address: " + placeStop.getAddress()));
        }
        if (Objects.equals(mSettingsInterface.mode().get().toLowerCase(), MapActivity.WALKING)) {
            mImageLocaton.setImageResource(R.drawable.ic_walking);
        } else {
            mImageLocaton.setImageResource(R.drawable.ic_car);
        }
    }

    public ViewPagerMarker newInstance(PlaceStop placeStop) {
        ViewPagerMarker fragment = new ViewPagerMarker_();
        Bundle bundle = new Bundle();
        bundle.putParcelable(OBJECT, placeStop);
        fragment.setArguments(bundle);
        return fragment;
    }

    // Load data from server
//        ArrayList<Result> mResults = MockData.getData();

    // Load data from local database
//        BusStopDatabase mBusStopDatabase = new BusStopDatabase(getContext());
//        ArrayList<PlaceStop> mPlacePagers = mBusStopDatabase.getPlacesByIdCarriage(MapActivity.positionCarriage);
//        Log.d("ViewPagerMarker", "afterViews: " + mPlacePagers.size());
//        if (mPlacePagers.size() > 0) {
//            tvMarkerTitle.setText(mPlacePagers.get(position).getName());
//            tvmarkerLongLat.setText(String.valueOf("Lat-Long: " + mPlacePagers.get(position).getLatitude())
//                    + "; " + String.valueOf(mPlacePagers.get(position).getLongitude()));
//            try {
//                String string = mResults.get(position).getPhotos().get(0).getPhotoReference();
//                Picasso.with(this.getContext())
//                        .load(referencePhotoLink + string + "&key=" + ApiUtilsBus.KEY)
//                        .placeholder(R.drawable.ic_replay_white)
//                        .error(R.drawable.ic_error_outline_red)
//                        .into(imgLocaton);
//                Double rate = mResults.get(position).getRating();
//                String rateStr = "Rating: " + String.valueOf(mResults.get(position).getRating());
//                Log.d("rate", "afterViews: " + rate);
//                tvRating.setText(rateStr);
//                ratingBar.setRating(Float.parseFloat(String.valueOf(mResults.get(position).getRating())));
//            } catch (Exception e) {
//                tvRating.setText("Rating: Non");
//                ratingBar.setRating(0f);
//            }
//        }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_google_map_detail_marker, container, false);
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
//        mMyMarkers.addAll(MockData.getData());
//        MyMarker mMyMarker = mMyMarkers.get(position);
//        tvMarkerTitle.setText(mMyMarker.getMarkerTitle());
//        tvmarkerLongLat.setText(String.valueOf("Lat: " + mMyMarker.getMarkerLatitude())
//                + "; Long: " + String.valueOf(mMyMarker.getMarkerLongitude()));
//
//        return view;
//    }
}
