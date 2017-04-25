package com.example.gio.bigproject;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.gio.bigproject.data.ApiUtilsBus;
import com.example.gio.bigproject.data.BusStopDatabase;
import com.example.gio.bigproject.data.SOServiceBus;
import com.example.gio.bigproject.model.bus_stop.Result;
import com.example.gio.bigproject.model.bus_stop.SOStationsResponse;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */
@EActivity(R.layout.activity_answer_api)
public class ListBusStopActivity extends AppCompatActivity {
    @ViewById(R.id.rv_answers)
    RecyclerView mRecyclerView;

    private ListBusStopAdapter mAdapter;
    private SOServiceBus mService;
    private BusStopDatabase busStopDatabase;

    private ArrayList<Result> mResults = new ArrayList<>();

    @AfterViews
    void afterViews() {
        mService = ApiUtilsBus.getSOService();
        mAdapter = new ListBusStopAdapter(mResults);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        loadBusStops();
    }

    public void loadBusStops() {
        mService.getBusStop("tram xe buyt", "16.08,108.22", ApiUtilsBus.KEY)
                .enqueue(new Callback<SOStationsResponse>() {
            @Override
            public void onResponse(Call<SOStationsResponse> call, Response<SOStationsResponse> response) {

                if (response.isSuccessful()) {
                    mAdapter.updateAnswers(response.body().getResults());
                    mResults.addAll(response.body().getResults());
                    Log.d("LisBusStopActivity", "Results loaded from API busStop" + mResults.size());
                } else {
//                    int statusCode  = response.code();
                    Log.d("LisBusStopActivity", "Results didn't load from API: ");
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<SOStationsResponse> call, Throwable t) {
//                showErrorMessage();

                Log.d("", "onFailure: " + call.request().url().toString());
                Log.d("LisBusStopActivity", "Load Bus Stop failed from API");

            }
        });
    }
}
