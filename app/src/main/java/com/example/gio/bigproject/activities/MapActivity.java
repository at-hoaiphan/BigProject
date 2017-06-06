package com.example.gio.bigproject.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gio.bigproject.R;
import com.example.gio.bigproject.adapters.ViewPagerMarkerAdapter;
import com.example.gio.bigproject.datas.ApiUtilsBus;
import com.example.gio.bigproject.datas.BusStopDatabase;
import com.example.gio.bigproject.datas.CarriagePolyline;
import com.example.gio.bigproject.interfaces.SOServiceDirection;
import com.example.gio.bigproject.interfaces.SettingsInterface_;
import com.example.gio.bigproject.models.bus_stops.PlaceStop;
import com.example.gio.bigproject.models.directions.RouteDirec;
import com.example.gio.bigproject.models.directions.SOPlacesDirectionResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@OptionsMenu(R.menu.menu_toolbar)
@EActivity(R.layout.activity_main)
public class MapActivity extends AppCompatActivity implements LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener, ViewPager.OnPageChangeListener {

    // Request for location (***).
    // value 8bit (value < 256).
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    public static final int SETTINGS = 99;
    public static final int LIST_PLACES = 88;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final String CHECK_CHOOSE_DESTINATION = "Please choose your destination!";
    public static final String CHECK_NETWORK = "No internet, please check your network!";
    public static final String LOAD_DATA_FAILED = "Load data failed!";
    public static final String PERMISSION_GRANTED = "Permission granted!";
    public static final String PERMISSION_DENIED = "Permission denied!";
    public static final String NO_LOCATION_PROVIDER = "No location provider enabled!";
    public static final String SHOW_LOCATION_ERROR = "Show your location error!";
    public static final String MY_LOCATION = "My Location!";
    public static final String BUS_HERE = "Bus here!";
    public static final String ERROR = "Error!";
    public static final String LOAD_ROUTES_FAILED = "Routes didn't load from API, please check internet and restart app again!";
    public static final String LOAD_DIRECTIONS_FAILED = "Load Direction PlaceStop failed from API, please check internet and restart app again!";
    public static final String REQUEST_DIRECTION_ERROR = "Request directions from Google error!";
    public static final String LOAD_TIME_REMAIN_FAILED = "Load remain time failed from API, please check internet and restart app again!";
    public static final String DEFAULT_CARRIAGE = "0";
    public static final String CARRIAGE_1 = "1";
    public static final String CARRIAGE_2 = "2";
    public static final String CARRIAGE_3 = "3";
    public static final String CAR = "car";
    public static final String WALKING = "walking";
    public static String positionCarriage;

    @Extra("Carriage")
    String mCarriage;
    @ViewById(R.id.viewpagerLocation)
    ViewPager mViewPager;

    @ViewById(R.id.spBusCarriage)
    Spinner mSpinnerBusCarriage;

    @Pref
    SettingsInterface_ mSettingsInterface;

    private SOServiceDirection mSoServiceDirection;
    private List<Marker> mListMarkers = new ArrayList<>();
    private List<RouteDirec> mRoutes = new ArrayList<>();
    private GoogleMap mMyMap;
    private ProgressDialog mMyProgress;
    private Marker mPreviousSelectedMarker;
    private Polyline mPolyline;
    private Polyline mCarriagePolyline;
    private Polyline mAllCarriagePolyline;
    private BusStopDatabase mBusStopDatabase;
    private Marker mCurrentMarker;
    private Marker mMovingMarker;
    private Marker mBusMarker;
    private ViewPagerMarkerAdapter mAdapter;
    private static boolean isViewpagerVisibility = false;
    private static boolean isDirected = false;
    private CountDownTimer mCountDownTimer;
    private List<PlaceStop> mPlaceStops = new ArrayList<>();

    @AfterViews
    void afterViews() {
//        // Request data from server
//        MockData.createData();

        positionCarriage = String.valueOf(mSpinnerBusCarriage.getSelectedItemPosition());
        mSoServiceDirection = ApiUtilsBus.getSOServiceDirection();
        // Create Progress Bar
        mMyProgress = new ProgressDialog(this);
        mMyProgress.setTitle("Map Loading ...");
        mMyProgress.setMessage("Please wait...");
        mMyProgress.setCancelable(true);

        // Display Progress Bar
        mMyProgress.show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);

        // Put event when GoogleMap is ready.
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                onMyMapReady(googleMap);
            }
        });


        // Set onPageChange
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setPageMargin(10);
        mViewPager.setAlpha(0.8f);
    }

    @OptionsItem(R.id.mnSettings)
    void selectSettingsItem() {
        SettingsActivity_.intent(this).startForResult(SETTINGS);
    }

    @OptionsItem(R.id.mnAboutUs)
    void selectAboutUsItem() {
        AboutUsActivity_.intent(this).startForResult(SETTINGS);
    }

    @OptionsItem(R.id.mnExit)
    void selectExitItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("Do you want to quit app?")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int id) {
                        finish();
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Click(R.id.fabFindDirec)
    void clickFabFindDirec() {
        try {
            if (isOnline()) {
                if (isViewpagerVisibility) {
                    isDirected = true;
                    // points: overview_polyline
                    List<LatLng> arrDecode = decodePoly(mRoutes.get(0).getOverViewPolyline().getPoints());
                    // Draw polylines
                    PolylineOptions polyOp = new PolylineOptions().geodesic(true).width(10);
                    if (Objects.equals(mSettingsInterface.mode().get().toLowerCase(), WALKING)) {
                        polyOp.color(Color.CYAN);
                    } else {
                        polyOp.color(Color.BLUE);
                    }
                    polyOp.add(new LatLng(mCurrentMarker.getPosition().latitude, mCurrentMarker.getPosition().longitude));
                    for (LatLng arrDecodeLatlng : arrDecode) {
                        polyOp.add(arrDecodeLatlng);
                    }
                    polyOp.add(new LatLng(mListMarkers.get(mViewPager.getCurrentItem()).getPosition().latitude,
                            mListMarkers.get(mViewPager.getCurrentItem()).getPosition().longitude));
                    // Clear old direction
                    if (mPolyline != null) {
                        mPolyline.remove();
                    }
                    mPolyline = mMyMap.addPolyline(polyOp);
                } else {
                    Toast.makeText(this, CHECK_CHOOSE_DESTINATION, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, CHECK_NETWORK, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.fabListBusStops)
    void clickListButton() {
        ListBusStopActivity_.intent(this).mCarriage(positionCarriage).startForResult(LIST_PLACES);
//        Intent intent = new Intent(this, ListBusStopActivity_.class);
//        intent.putExtra(CARRIAGE, positionCarriage);
//        startActivityForResult(intent, LIST_PLACES);
    }

    private void onMyMapReady(final GoogleMap googleMap) {
        // Get GoogleMap object:
        mMyMap = googleMap;

        mMyMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mViewPager.setVisibility(View.GONE);
                isViewpagerVisibility = false;

                // Dismiss Dialog Progress when downloading finished
                mMyProgress.dismiss();

                // Add Detail location
                // Add marker
//                mResults = MockData.getData();
//                if (mResults.size() > 0) {
//                    for (int i = 0; i < mResults.size(); i++) {
//                        MarkerOptions option = new MarkerOptions();
//                        option.title(mResults.get(i).getName());
//                        option.snippet(mResults.get(i).getGeometry().getLocation().getLat()
//                                + ";" + mResults.get(i).getGeometry().getLocation().getLng());
//                        option.position(new LatLng(mResults.get(i).getGeometry().getLocation().getLat(),
//                                mResults.get(i).getGeometry().getLocation().getLng()));
//                        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
//                        Marker marker = myMap.addMarker(option);
//                        mListMarkers.add(marker);
//                        marker.showInfoWindow();
//                    }
//
//                } else {
//                    Toast.makeText(getBaseContext(), "Load API failed, Please restart app again!", Toast.LENGTH_SHORT).show();
//                }

                // Get data from database
                mBusStopDatabase = new BusStopDatabase(getBaseContext());
                if (mSpinnerBusCarriage.getSelectedItemPosition() == 0) {
                    mPlaceStops.addAll(mBusStopDatabase.getAllPlaces());
                } else {
                    mPlaceStops.addAll(mBusStopDatabase.getPlacesByIdCarriage(String.valueOf(mSpinnerBusCarriage.getSelectedItemPosition())));
                }
                mAdapter = new ViewPagerMarkerAdapter(getBaseContext(), getSupportFragmentManager(), mPlaceStops);
                mViewPager.setAdapter(mAdapter);

                if (mPlaceStops.size() > 0) {
                    // Show default Bus Carriage
                    for (PlaceStop placeStop : mPlaceStops) {
                        MarkerOptions option = new MarkerOptions();
                        option.title(placeStop.getName());
                        option.snippet(placeStop.getLatitude() + ";" + placeStop.getLongitude());
                        option.position(new LatLng(placeStop.getLatitude(), placeStop.getLongitude()));
                        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                        Marker marker = mMyMap.addMarker(option);
                        mListMarkers.add(marker);
                    }

                } else {
                    Toast.makeText(getBaseContext(), LOAD_DATA_FAILED, Toast.LENGTH_SHORT).show();
                }

                // Draw all carriage
                drawAllCarriagePoly();

                mSpinnerBusCarriage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        mViewPager.setVisibility(View.GONE);
                        isViewpagerVisibility = false;

                        positionCarriage = String.valueOf(i);
                        // Reload map
                        mMyMap.clear();
                        isDirected = false;

                        // draw carriage
                        if (Objects.equals(positionCarriage, String.valueOf(0))) {
                            drawAllCarriagePoly();
                        } else {
                            drawCarriagePoly(positionCarriage);
                        }

                        // Remove previousSelectedMarker
                        if (mPreviousSelectedMarker != null) {
                            mPreviousSelectedMarker.remove();
                        }

                        showMyLocation();
                        mListMarkers.clear();
                        mPlaceStops.clear();
                        if (Objects.equals(positionCarriage, String.valueOf(0))) {
                            mPlaceStops.addAll(mBusStopDatabase.getAllPlaces());
                        } else {
                            mPlaceStops.addAll(mBusStopDatabase.getPlacesByIdCarriage(positionCarriage));
                        }

                        if (mPlaceStops.size() > 0) {
                            for (PlaceStop placeStop : mPlaceStops) {
                                MarkerOptions option = new MarkerOptions();
                                option.title(placeStop.getName());
                                option.snippet(placeStop.getLatitude() + ";" + placeStop.getLongitude());
                                option.position(new LatLng(placeStop.getLatitude(), placeStop.getLongitude()));
                                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                                Marker marker = mMyMap.addMarker(option);
                                mListMarkers.add(marker);
                            }
                        }
                        mViewPager.setAdapter(null);
                        mAdapter = new ViewPagerMarkerAdapter(getBaseContext(), getSupportFragmentManager(), mPlaceStops);
                        mViewPager.setAdapter(mAdapter);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                mMyMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        mViewPager.setVisibility(View.VISIBLE);
                        isViewpagerVisibility = true;
                        if (mPreviousSelectedMarker != null) {
                            try {
                                mPreviousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                            } catch (Exception ignored) {
                            }
                        }
                        for (int i = 0; i < mListMarkers.size(); i++) {
                            if (marker.equals(mListMarkers.get(i))) {
                                mListMarkers.get(i).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_selected));
                                mPreviousSelectedMarker = mListMarkers.get(i);
                                mViewPager.setCurrentItem(i, true);
                            }
                        }
                        return false;
                    }
                });
                // Show User's Location
                askPermissionsAndShowMyLocation();
            }
        });

        int MAP_TYPE = mSettingsInterface.type().get();
        if (MAP_TYPE == MAP_TYPE_NORMAL) {
            mMyMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if (MAP_TYPE == MAP_TYPE_SATELLITE) {
            mMyMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        mMyMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMyMap.setMyLocationEnabled(true);
    }

    private void askPermissionsAndShowMyLocation() {
        // Ask for permission with API >= 23.
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                // Permissions.
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION};

                // Dialog.
                ActivityCompat.requestPermissions(this, permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
                return;
            }
        }

        // Show Current location
        this.showMyLocation();
    }

    // User agree or ignore.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {
                // If ignore: array null.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, PERMISSION_GRANTED, Toast.LENGTH_LONG).show();

                    // Display current location.
                    this.showMyLocation();
                }
                // Cancel or refuse.
                else {
                    Toast.makeText(this, PERMISSION_DENIED, Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // Find a Location Provider
    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        // Find the best LocationProvider
        // ==> "gps", "network",...
        String bestProvider = locationManager.getBestProvider(criteria, true);
        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(this, NO_LOCATION_PROVIDER, Toast.LENGTH_LONG).show();
            return null;
        }
        return bestProvider;
    }

    // Call only when had location-permission
    private void showMyLocation() {
        final LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        String locationProvider = this.getEnabledLocationProvider();

        if (locationProvider == null) {
            return;
        }

        // Millisecond
        final long MIN_TIME_BW_UPDATES = 10000;
        // Met
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

        Location myLocation;
        try {
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            // Get location.
            myLocation = locationManager.getLastKnownLocation(locationProvider);
        }

        // Android API >= 23 catch SecurityException.
        catch (SecurityException e) {
            Toast.makeText(this, SHOW_LOCATION_ERROR, Toast.LENGTH_LONG).show();
            return;
        }

        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMyMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            // Add MyLocation on Map:
            MarkerOptions option = new MarkerOptions();
            option.title(MY_LOCATION);
            option.snippet(latLng.latitude + "+" + latLng.longitude);
            option.position(new LatLng(latLng.latitude, latLng.longitude));
            option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start_marker));
            mCurrentMarker = mMyMap.addMarker(option);
            mCurrentMarker.setDraggable(true);
            mCurrentMarker.showInfoWindow();

            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
//            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            mMyMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if (mPreviousSelectedMarker != null) {
                        mPreviousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                    }
                    if (mCurrentMarker != null) {
                        mCurrentMarker.remove();
                    }
                    showMyLocation();
//                    mPolyline.remove();
                    if (mViewPager.getVisibility() == View.VISIBLE) {
                        loadDirections(mViewPager.getCurrentItem());
                    }
                    mMyMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mCurrentMarker.showInfoWindow();

                    return true;
                }
            });

            mMyMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    mCurrentMarker.setSnippet(marker.getPosition().latitude + "; " + marker.getPosition().longitude);
                    mCurrentMarker.showInfoWindow();

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    mCurrentMarker.setSnippet(marker.getPosition().latitude + "; " + marker.getPosition().longitude);
                    Toast.makeText(MapActivity.this, "Set your loacation latitude-longitude: "
                            + marker.getPosition().latitude
                            + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
                    loadDirections(mViewPager.getCurrentItem());
                }
            });
        } else {
            Toast.makeText(this, "Load Location via GPS failed! Loading via network...", Toast.LENGTH_LONG).show();
            try {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                // Get Location.
                LatLng locationNet = new LatLng(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(),
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude());
                // Load Location via NetWork Provider
                MarkerOptions option = new MarkerOptions();
                option.title(MY_LOCATION);
                option.snippet(locationNet.latitude + "; " + locationNet.longitude);
                option.position(locationNet);
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start_marker));
                mCurrentMarker = mMyMap.addMarker(option);
                mCurrentMarker.setDraggable(true);
                mCurrentMarker.showInfoWindow();

                final CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(locationNet)             // Sets the center of the map to location user
                        .zoom(16)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMyMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (Exception ignored) {
            }
        }

        mMyMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (isDirected) {
                    LatLng previousLatLngMoving = new LatLng(0, 0);
                    if (mMovingMarker != null) {
                        previousLatLngMoving = new LatLng(mMovingMarker.getPosition().latitude, mMovingMarker.getPosition().longitude);
                        mMovingMarker.remove();
                    }
                    MarkerOptions option = new MarkerOptions();
                    option.title(MY_LOCATION);
                    option.position(new LatLng(location.getLatitude(), location.getLongitude()));
                    option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_placeholder));
                    mMovingMarker = mMyMap.addMarker(option);
                    mMovingMarker.showInfoWindow();

                    Double heading = SphericalUtil.computeHeading(previousLatLngMoving, new LatLng(location.getLatitude(), location.getLongitude()));

                    CameraPosition cameraBusPosition =
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .bearing(heading.floatValue())
                                    .tilt(40)
                                    .zoom(16)
                                    .build();
                    mMyMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraBusPosition));
                }
            }
        });
    }

    @Override
    public void onPageSelected(int position) {
        // Clear old direction
        if (mPolyline != null) {
            mPolyline.remove();
            isDirected = false;
        }
        loadDirections(position);
        loadTimeRemain(position);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mListMarkers.get(position).getPosition().latitude, mListMarkers.get(position).getPosition().longitude))             // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMyMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        try {
            if (mPreviousSelectedMarker != null) {
                mPreviousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
            }
        } catch (Exception ignored) {
        }
        mListMarkers.get(position).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_selected));
        mPreviousSelectedMarker = mListMarkers.get(position);
    }

    private void loadDirections(final int position) {
        try { mSoServiceDirection.getPlacesDirection(String.valueOf(mCurrentMarker.getPosition().latitude)
                        + "," + String.valueOf(mCurrentMarker.getPosition().longitude),
                mListMarkers.get(position).getPosition().latitude
                        + "," + mListMarkers.get(position).getPosition().longitude,
                mSettingsInterface.mode().get().toLowerCase(),
                ApiUtilsBus.KEY)
                .enqueue(new Callback<SOPlacesDirectionResponse>() {
                    @Override
                    public void onResponse(Call<SOPlacesDirectionResponse> call, Response<SOPlacesDirectionResponse> response) {

                        if (response.isSuccessful()) {
                            mRoutes.clear();
                            mRoutes.addAll(response.body().getRoutes());

                            // Display distance and duration of Destination
                            if (mRoutes.size() > 0) {
                                mListMarkers.get(position).setSnippet(mRoutes.get(0).getLegs().get(0).getDistance().getText()
                                        + "; " + mRoutes.get(0).getLegs().get(0).getDuration().getText());
                                mListMarkers.get(position).showInfoWindow();
                            }
                        } else {
                            Toast.makeText(MapActivity.this, LOAD_ROUTES_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SOPlacesDirectionResponse> call, Throwable t) {
                        Toast.makeText(MapActivity.this, LOAD_DIRECTIONS_FAILED, Toast.LENGTH_SHORT).show();
                    }
                });
        } catch (Exception e) {
            Toast.makeText(this, REQUEST_DIRECTION_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    // Load time remain from bus to bus-stop
    private void loadTimeRemain(final int position) {
        try {
            mSoServiceDirection.getPlacesDirection(String.valueOf(mBusMarker.getPosition().latitude)
                            + "," + String.valueOf(mBusMarker.getPosition().longitude),
                    mListMarkers.get(position).getPosition().latitude
                            + "," + mListMarkers.get(position).getPosition().longitude,
                    CAR,
                    ApiUtilsBus.KEY)
                    .enqueue(new Callback<SOPlacesDirectionResponse>() {
                        @Override
                        public void onResponse(Call<SOPlacesDirectionResponse> call, Response<SOPlacesDirectionResponse> response) {

                            if (response.isSuccessful()) {
                                // Display dduration of Remaining time
                                if (mRoutes.size() > 0) {
                                    String distanceDuration = mListMarkers.get(position).getSnippet();
                                    mListMarkers.get(position).setSnippet(distanceDuration
                                            + " - remain: " + response.body().getRoutes().get(0).getLegs().get(0).getDuration().getText());
                                    mListMarkers.get(position).showInfoWindow();
                                }
                            } else {
                                Toast.makeText(MapActivity.this, LOAD_TIME_REMAIN_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SOPlacesDirectionResponse> call, Throwable t) {
                            Toast.makeText(MapActivity.this, LOAD_TIME_REMAIN_FAILED, Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, REQUEST_DIRECTION_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMap() {
        mMyMap.clear();
        isDirected = false;
        if (mPreviousSelectedMarker != null) {
            mPreviousSelectedMarker.remove();
        }
        showMyLocation();
        mListMarkers.clear();
        mPlaceStops.clear();

        // Draw Carriage
        if (Objects.equals(positionCarriage, DEFAULT_CARRIAGE)) {
            mPlaceStops.addAll(mBusStopDatabase.getAllPlaces());
        } else {
            mPlaceStops.addAll(mBusStopDatabase.getPlacesByIdCarriage(positionCarriage));
        }
        if (mPlaceStops.size() > 0) {
            for (PlaceStop placeStop : mPlaceStops) {
                MarkerOptions option = new MarkerOptions();
                option.title(placeStop.getName());
                option.snippet(placeStop.getLatitude() + ";" + placeStop.getLongitude());
                option.position(new LatLng(placeStop.getLatitude(), placeStop.getLongitude()));
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                Marker marker = mMyMap.addMarker(option);
                mListMarkers.add(marker);
            }
        }
        mViewPager.setAdapter(null);
        mAdapter = new ViewPagerMarkerAdapter(getBaseContext(), getSupportFragmentManager(), mPlaceStops);
        mViewPager.setAdapter(mAdapter);
    }

    private void drawCarriagePoly(String carriage) {
        // points: overview_polyline
        PolylineOptions carriagePolyOption = new PolylineOptions().geodesic(true).width(25);
        final List<LatLng> arrCarriageDecode = new ArrayList<>();
        switch (carriage) {
            case CARRIAGE_1:
                // Bus Carriage 1
                arrCarriageDecode.addAll(CarriagePolyline.getCarriagePoly1());
                carriagePolyOption.color(Color.parseColor("#99FF373E"));
                break;
            case CARRIAGE_2:
                // Bus Carriage 2
                arrCarriageDecode.addAll(CarriagePolyline.getCarriagePoly2());
                carriagePolyOption.color(Color.parseColor("#88FFF837"));
                break;
            case CARRIAGE_3:
                // Bus Carriage 3
                arrCarriageDecode.addAll(CarriagePolyline.getCarriagePoly3());
                carriagePolyOption.color(Color.parseColor("#7337FF37"));
                break;
        }

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        MarkerOptions option = new MarkerOptions();
        option.title(BUS_HERE);
        option.position(new LatLng(arrCarriageDecode.get(0).latitude, arrCarriageDecode.get(0).longitude));
        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_marker));
        mBusMarker = mMyMap.addMarker(option);
//                busMarker.showInfoWindow();
        mCountDownTimer = new CountDownTimer((arrCarriageDecode.size()) * 5000, 5000) {
            int index = 0;
            LatLng busPosition = new LatLng(arrCarriageDecode.get(0).latitude, arrCarriageDecode.get(0).longitude);

            public void onTick(long millisUntilFinished) {
                if (index == arrCarriageDecode.size() - 1) {
                    cancel();
                }
                busPosition = new LatLng(arrCarriageDecode.get(++index).latitude, arrCarriageDecode.get(++index).longitude);
                mBusMarker.setPosition(busPosition);
            }

            public void onFinish() {
                mBusMarker.remove();
            }
        }.start();
        // Draw polylines
        for (LatLng arrCarriage : arrCarriageDecode) {
            carriagePolyOption.add(arrCarriage);
        }

        // Clear old Polyline
        if (mCarriagePolyline != null) {
            mCarriagePolyline.remove();
        }
        if (mAllCarriagePolyline != null) {
            mAllCarriagePolyline.remove();
        }
        mCarriagePolyline = mMyMap.addPolyline(carriagePolyOption);
    }

    private void drawAllCarriagePoly() {
        // points: overview_polyline
        List<LatLng> arrCarriageDecode1 = new ArrayList<>();
        List<LatLng> arrCarriageDecode2 = new ArrayList<>();
        List<LatLng> arrCarriageDecode3 = new ArrayList<>();

        arrCarriageDecode1.addAll(CarriagePolyline.getCarriagePoly1());
        arrCarriageDecode2.addAll(CarriagePolyline.getCarriagePoly2());
        arrCarriageDecode3.addAll(CarriagePolyline.getCarriagePoly3());
        // Draw polylines
        PolylineOptions carriagePolyOption1 = new PolylineOptions().geodesic(true).color(Color.parseColor("#99FF373E")).width(30);
        PolylineOptions carriagePolyOption2 = new PolylineOptions().geodesic(true).color(Color.parseColor("#88FFF837")).width(23);
        PolylineOptions carriagePolyOption3 = new PolylineOptions().geodesic(true).color(Color.parseColor("#7337FF37")).width(15);

        for (LatLng arrCarriage : arrCarriageDecode1) {
            carriagePolyOption1.add(arrCarriage);
        }
        for (LatLng arrCarriage : arrCarriageDecode2) {
            carriagePolyOption2.add(arrCarriage);
        }
        for (LatLng arrCarriage : arrCarriageDecode3) {
            carriagePolyOption3.add(arrCarriage);
        }

        // Clear old direction
        if (mCarriagePolyline != null) {
            mCarriagePolyline.remove();
        }
        mAllCarriagePolyline = mMyMap.addPolyline(carriagePolyOption1);
        mAllCarriagePolyline = mMyMap.addPolyline(carriagePolyOption2);
        mAllCarriagePolyline = mMyMap.addPolyline(carriagePolyOption3);
    }

    // Decode Polyline Array
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                try {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } catch (Exception ignored) {

                }
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @OnActivityResult(SETTINGS)
    void startActivityForResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            boolean needReload = data.getBooleanExtra(SettingsActivity.NEED_RELOAD, true);
            if (needReload) {
                finish();
                MapActivity_.intent(this).start();
            }
        }
    }

    @OnActivityResult(LIST_PLACES)
    void startActivityForResultList(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (!Objects.equals(positionCarriage, data.getStringExtra(ListBusStopActivity.ID_CARRIAGE))) {
                positionCarriage = data.getStringExtra(ListBusStopActivity.ID_CARRIAGE);
                loadMap();
                if (Objects.equals(positionCarriage, DEFAULT_CARRIAGE)) {
                    drawAllCarriagePoly();
                } else {
                    drawCarriagePoly(positionCarriage);
                }
            }
            int idPlace = data.getIntExtra(ListBusStopActivity.ID_PLACE, -1);
            if (idPlace != -1) {
                mViewPager.setVisibility(View.VISIBLE);
                isViewpagerVisibility = true;
                mViewPager.setCurrentItem(idPlace, true);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(mListMarkers.get(idPlace).getPosition().latitude, mListMarkers.get(idPlace).getPosition().longitude))             // Sets the center of the map to location user
                        .zoom(16)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMyMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mListMarkers.get(idPlace).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_selected));
                loadDirections(idPlace);
                mPreviousSelectedMarker = mListMarkers.get(idPlace);
            }
        }
    }

    // Unused implemented-function
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMyLocationButtonClick() {

        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
