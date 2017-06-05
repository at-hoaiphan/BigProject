package com.example.gio.bigproject.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.gio.bigproject.R;
import com.example.gio.bigproject.adapters.ListBusStopAdapter;
import com.example.gio.bigproject.datas.BusStopDatabase;
import com.example.gio.bigproject.models.bus_stops.PlaceStop;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

@EActivity(R.layout.activity_list_places)
public class ListBusStopActivity extends AppCompatActivity  {
    static final String DEFAULT_CARRIAGE = "0";
    static final String ID_CARRIAGE = "idCarriage";
    static final String ID_PLACE = "idPlace";
    @ViewById(R.id.rvPlaces)
    RecyclerView mRecyclerView;

    @ViewById(R.id.fabBack)
    FloatingActionButton fabBack;

    @ViewById(R.id.spBusCarriage)
    Spinner spBusCarriage;

    private ListBusStopAdapter mAdapter;
    //    private SOServiceBus mService;
    private BusStopDatabase busStopDatabase;
    private ArrayList<PlaceStop> mPlaceStops;

//    private ArrayList<Result> mResults = new ArrayList<>();

    @AfterViews
    void afterViews() {
        String carriage = getIntent().getStringExtra("Carriage");
        if (carriage != null) {
            spBusCarriage.setSelection(Integer.parseInt(carriage));
        }
//        mAdapter = new ListBusStopAdapter(mResults);
        busStopDatabase = new BusStopDatabase(this);
        mPlaceStops = new ArrayList<>();
        if (Objects.equals(carriage, DEFAULT_CARRIAGE)) {
            mPlaceStops.addAll(busStopDatabase.getAllPlaces());
        } else {
            mPlaceStops.addAll(busStopDatabase.getPlacesByIdCarriage(carriage));
        }
        mAdapter = new ListBusStopAdapter(mPlaceStops);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        spBusCarriage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPlaceStops.clear();
                if (i == 0) {
                    mPlaceStops.addAll(busStopDatabase.getAllPlaces());
                } else {
                    mPlaceStops.addAll(busStopDatabase.getPlacesByIdCarriage(String.valueOf(spBusCarriage.getSelectedItemPosition())));
                }
                mAdapter.notifyDataSetChanged();
            }

            // Not use
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mAdapter.setPlaceOnClickListener(new ListBusStopAdapter.PlaceListener() {
            @Override
            public void onPlaceClick(int id) {
                Intent data = new Intent();
                data.putExtra(ID_CARRIAGE, String.valueOf(spBusCarriage.getSelectedItemPosition()));
                data.putExtra(ID_PLACE, id);
                setResult(RESULT_OK, data);
                finish();
            }
        });
//        loadBusStops();
    }

    @Click(R.id.fabBack)
    void clickBack() {
        finish();
    }



//    // Call Results by API
//    public void loadBusStops() {
//        mService.getBusStop("tram xe buyt", "16.08,108.22", ApiUtilsBus.KEY)
//                .enqueue(new Callback<SOStationsResponse>() {
//            @Override
//            public void onResponse(Call<SOStationsResponse> call, Response<SOStationsResponse> response) {
//
//                if (response.isSuccessful()) {
//                    mAdapter.updateAnswers(response.body().getResults());
////                    mResults.addAll(response.body().getResults());
////                    Log.d("LisBusStopActivity", "Results loaded from API busStop" + mResults.size());
//                } else {
////                    int statusCode  = response.code();
//                    Log.d("LisBusStopActivity", "Results didn't load from API: ");
//                    // handle request errors depending on status code
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SOStationsResponse> call, Throwable t) {
////                showErrorMessage();
//
//                Log.d("", "onFailure: " + call.request().url().toString());
//                Log.d("LisBusStopActivity", "Load Bus Stop failed from API");
//
//            }
//        });
//    }
}
