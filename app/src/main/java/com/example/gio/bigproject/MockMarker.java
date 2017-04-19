package com.example.gio.bigproject;

import android.util.Log;

import com.example.gio.bigproject.data.ApiUtilsBus;
import com.example.gio.bigproject.data.SOServiceBus;
import com.example.gio.bigproject.data.model.Result;
import com.example.gio.bigproject.data.model.SOStationsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright by Gio.
 * Created on 4/14/2017.
 */

class MockMarker {

    private static ArrayList<Result> mResults = new ArrayList<>();
    // Get data from Json
    private static SOServiceBus mService = ApiUtilsBus.getSOService();

    public static ArrayList<MyMarker> getData() {

        final ArrayList<MyMarker> list = new ArrayList<>();

        mService.getAnswers().enqueue(new Callback<SOStationsResponse>() {
            @Override
            public void onResponse(Call<SOStationsResponse> call, Response<SOStationsResponse> response) {

                if (response.isSuccessful()) {
                    mResults = (ArrayList<Result>) response.body().getResults();

                    for (int i = 0; i < mResults.size(); i++) {
                        list.add(new MyMarker(i, mResults.get(i).getName(), mResults.get(i).getGeometry().getLocation().getLat(),
                                mResults.get(i).getGeometry().getLocation().getLng()));
                    }

                    Log.d("MockMarker sizeResult", "posts loaded from API" + mResults.size());
                } else {
//                    int statusCode  = response.code();
                    Log.d("MockMarker", "posts didn't load from API: ");
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<SOStationsResponse> call, Throwable t) {
//                showErrorMessage();

                Log.d("", "onFailure: " +call.request().url().toString());
                Log.d("MockMarker", "error loading from API");

            }
        });

        Log.d("MockMarker sizeResult", "getData: " + list.size());
        return list;
    }
}
