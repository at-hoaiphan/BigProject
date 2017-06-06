package com.example.gio.bigproject.datas;

import com.example.gio.bigproject.interfaces.SOServiceBus;
import com.example.gio.bigproject.models.bus_stops.Result;
import com.example.gio.bigproject.models.bus_stops.SOStationsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

// Get data from service
class MockData {

    // Get Data from service
    private static final List<Result> mResults = new ArrayList<>();
    // Get data from Json
    private static SOServiceBus mService = ApiUtilsBus.getSOService();
    private static int RESULT_COUNT;

    public static void createData() {
        mService.getBusStop("tram xe buyt", "16.08,108.22", ApiUtilsBus.KEY)
                .enqueue(new Callback<SOStationsResponse>() {
                    @Override
                    public void onResponse(Call<SOStationsResponse> call, Response<SOStationsResponse> response) {

                        if (response.isSuccessful()) {
                            if (mResults.size() != 0) {
                                mResults.clear();
                            }
                            mResults.addAll(response.body().getResults());
                            RESULT_COUNT = mResults.size();
                        }
                    }

                    @Override
                    public void onFailure(Call<SOStationsResponse> call, Throwable t) {
                    }
                });

    }

    public static List<Result> getData() {
        return mResults;
    }
}
