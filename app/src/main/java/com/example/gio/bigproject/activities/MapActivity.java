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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gio.bigproject.R;
import com.example.gio.bigproject.SettingsInterface_;
import com.example.gio.bigproject.adapter.ViewPagerMarkerAdapter;
import com.example.gio.bigproject.data.ApiUtilsBus;
import com.example.gio.bigproject.data.BusStopDatabase;
import com.example.gio.bigproject.data.CarriagePolyline;
import com.example.gio.bigproject.data.SOServiceDirection;
import com.example.gio.bigproject.model.bus_stop.PlaceStop;
import com.example.gio.bigproject.model.direction.RouteDirec;
import com.example.gio.bigproject.model.direction.SOPlacesDirectionResponse;
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
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@OptionsMenu(R.menu.menu_toolbar)
@EActivity(R.layout.activity_main)
public class MapActivity extends AppCompatActivity implements LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener, ViewPager.OnPageChangeListener {

    @ViewById(R.id.viewpager_location)
    ViewPager mViewPager;

    @ViewById(R.id.fabFindDirec)
    FloatingActionButton fabFindDirec;

    @ViewById(R.id.fabListBusStops)
    FloatingActionButton fabListBusStops;

    @ViewById(R.id.spBusCarriage)
    Spinner spinnerBusCarriage;

    @Pref
    SettingsInterface_ settingsInterface;

    private SOServiceDirection mSoServiceDirection;
    private ArrayList<Marker> mListMarkers = new ArrayList<>();
    private ArrayList<RouteDirec> mRoutes = new ArrayList<>();
    private GoogleMap myMap;
    private ProgressDialog myProgress;
    private Marker previousSelectedMarker;
    private Polyline mPolyline;
    private Polyline mCarriagePolyline;
    private Polyline mAllCarriagePolyline;
    private BusStopDatabase mBusStopDatabase;
    private Marker currentMarker;
    private Marker movingMarker;
    private Marker busMarker;
    private ViewPagerMarkerAdapter mAdapter;
    private static boolean isViewpagerVisibility = false;
    private CountDownTimer mCountDownTimer;

    // Request for location (***).
    // value 8bit (value < 256).
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    public static final int SETTINGS = 99;
    public static final int LIST_PLACES = 88;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static String positionCarriage;
    private ArrayList<PlaceStop> mPlaceStops = new ArrayList<>();

    @AfterViews
    void afterViews() {
//        // Request data from server
//        MockData.createData();
//        Log.d("Map", "afterViews: " + MockData.getData().size());

        positionCarriage = String.valueOf(spinnerBusCarriage.getSelectedItemPosition());
        mSoServiceDirection = ApiUtilsBus.getSOServiceDirection();
        // Create Progress Bar
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Map Loading ...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(true);

        // Display Progress Bar
        myProgress.show();

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
        AboutUs_.intent(this).startForResult(SETTINGS);
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
                    // points: overview_polyline
                    ArrayList<LatLng> arrDecode = decodePoly(mRoutes.get(0).getOverViewPolyline().getPoints());
                    // Draw polylines
                    PolylineOptions polyOp = new PolylineOptions().geodesic(true).width(10);
                    if (Objects.equals(settingsInterface.mode().get().toLowerCase(), "walking")) {
                        polyOp.color(Color.CYAN);
                    } else {
                        polyOp.color(Color.BLUE);
                    }
                    polyOp.add(new LatLng(currentMarker.getPosition().latitude, currentMarker.getPosition().longitude));
                    for (int i = 0; i < arrDecode.size(); i++) {
                        polyOp.add(new LatLng(arrDecode.get(i).latitude, arrDecode.get(i).longitude));
                    }
                    polyOp.add(new LatLng(mListMarkers.get(mViewPager.getCurrentItem()).getPosition().latitude,
                            mListMarkers.get(mViewPager.getCurrentItem()).getPosition().longitude));
                    // Clear old direction
                    if (mPolyline != null) {
                        mPolyline.remove();
                    }
                    mPolyline = myMap.addPolyline(polyOp);
                } else {
                    Toast.makeText(this, "Please choose your destination!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No internet, please check your network!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.fabListBusStops)
    void clickListButton() {
//        ListBusStopActivity_.intent(this).startForResult(LIST_PLACES);
        Intent intent = new Intent(this, ListBusStopActivity_.class);
        intent.putExtra("Carriage", positionCarriage);
        startActivityForResult(intent, LIST_PLACES);
    }

    private void onMyMapReady(final GoogleMap googleMap) {
        // Get GoogleMap object:
        myMap = googleMap;

        myMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mViewPager.setVisibility(View.GONE);
                isViewpagerVisibility = false;

                // Đã tải thành công thì tắt Dialog Progress đi
                myProgress.dismiss();

                // Add Detail location
                // Add marker
//                mResults = MockData.getData();
//                Log.d("MapActivity sizeResult", "onMyMapReady: " + mResults.size());
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
                if (spinnerBusCarriage.getSelectedItemPosition() == 0) {
                    mPlaceStops.addAll(mBusStopDatabase.getAllPlaces());
                } else {
                    mPlaceStops.addAll(mBusStopDatabase.getPlacesByIdCarriage(String.valueOf(spinnerBusCarriage.getSelectedItemPosition())));
                }
                mAdapter = new ViewPagerMarkerAdapter(getBaseContext(), getSupportFragmentManager(), mPlaceStops);
                mViewPager.setAdapter(mAdapter);

                if (mPlaceStops.size() > 0) {
                    // Show default Bus Carriage
                    for (int i = 0; i < mPlaceStops.size(); i++) {
                        MarkerOptions option = new MarkerOptions();
                        option.title(mPlaceStops.get(i).getName());
                        option.snippet(mPlaceStops.get(i).getLatitude()
                                + ";" + mPlaceStops.get(i).getLongitude());
                        option.position(new LatLng(mPlaceStops.get(i).getLatitude(),
                                mPlaceStops.get(i).getLongitude()));
                        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                        Marker marker = myMap.addMarker(option);
                        mListMarkers.add(marker);
                    }

                } else {
                    Toast.makeText(getBaseContext(), "Load data failed!", Toast.LENGTH_SHORT).show();
                }

                // draw all carriage
                drawAllCarriagePoly();

                spinnerBusCarriage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        mViewPager.setVisibility(View.GONE);
                        isViewpagerVisibility = false;

                        positionCarriage = String.valueOf(i);
                        // Reload map
                        myMap.clear();

                        // draw carriage
                        if (Objects.equals(positionCarriage, String.valueOf(0))) {
                            drawAllCarriagePoly();
                        } else {
                            drawCarriagePoly(positionCarriage);
                        }

                        // Remove previousSelectedMarker
                        if (previousSelectedMarker != null) {
                            previousSelectedMarker.remove();
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
                            for (int j = 0; j < mPlaceStops.size(); j++) {
                                MarkerOptions option = new MarkerOptions();
                                option.title(mPlaceStops.get(j).getName());
                                option.snippet(mPlaceStops.get(j).getLatitude()
                                        + ";" + mPlaceStops.get(j).getLongitude());
                                option.position(new LatLng(mPlaceStops.get(j).getLatitude(),
                                        mPlaceStops.get(j).getLongitude()));
                                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                                Marker marker = myMap.addMarker(option);
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


                myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        mViewPager.setVisibility(View.VISIBLE);
                        isViewpagerVisibility = true;
                        if (previousSelectedMarker != null) {
                            try {
                                previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                            } catch (Exception ignored) {

                            }
                        }
                        for (int i = 0; i < mListMarkers.size(); i++) {
                            if (marker.equals(mListMarkers.get(i))) {
                                mListMarkers.get(i).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_selected));
                                previousSelectedMarker = mListMarkers.get(i);
                                mViewPager.setCurrentItem(i, true);
                            }
                        }
                        return false;
                    }
                });
                // Hiển thị vị trí người dùng.
                askPermissionsAndShowMyLocation();
            }
        });

        int MAP_TYPE = settingsInterface.type().get();
        if (MAP_TYPE == MAP_TYPE_NORMAL) {
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if (MAP_TYPE == MAP_TYPE_SATELLITE) {
            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }

        myMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myMap.setMyLocationEnabled(true);
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

                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    // Display current location.
                    this.showMyLocation();
                }
                // Cancel or refuse.
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    // Tìm một nhà cung cấp vị trị hiện thời đang được mở.
    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Tiêu chí để tìm một nhà cung cấp vị trí.
        Criteria criteria = new Criteria();

        // Tìm một nhà cung vị trí hiện thời tốt nhất theo tiêu chí trên.
        // ==> "gps", "network",...
        String bestProvider = locationManager.getBestProvider(criteria, true);
        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(this, "No location provider enabled!", Toast.LENGTH_LONG).show();
            return null;
        }
        return bestProvider;
    }

    // Chỉ gọi phương thức này khi đã có quyền xem vị trí người dùng.
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
            // Đoạn code nay cần người dùng cho phép (Hỏi ở trên ***).
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

            // Lấy ra vị trí.
            myLocation = locationManager.getLastKnownLocation(locationProvider);
        }

        // Với Android API >= 23 phải catch SecurityException.
        catch (SecurityException e) {
            Toast.makeText(this, "Show your location error!", Toast.LENGTH_LONG).show();
            return;
        }

        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
            // Add MyLocation on Map:
            MarkerOptions option = new MarkerOptions();
            option.title("My Location!");
            option.snippet(latLng.latitude + "+" + latLng.longitude);
            option.position(new LatLng(latLng.latitude, latLng.longitude));
            option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start_marker));
            currentMarker = myMap.addMarker(option);
            currentMarker.setDraggable(true);
            currentMarker.showInfoWindow();

            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
//            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            myMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if (previousSelectedMarker != null) {
                        previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                    }
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    showMyLocation();
//                    mPolyline.remove();
                    if (mViewPager.getVisibility() == View.VISIBLE) {
                        loadDirections(mViewPager.getCurrentItem());
                    }
                    myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    currentMarker.showInfoWindow();

                    return true;
                }
            });

            myMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    currentMarker.setSnippet(marker.getPosition().latitude + "; " + marker.getPosition().longitude);
                    currentMarker.showInfoWindow();

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    currentMarker.setSnippet(marker.getPosition().latitude + "; " + marker.getPosition().longitude);
                    Toast.makeText(MapActivity.this, "Set your loacation latitude-longitude: "
                            + marker.getPosition().latitude
                            + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
                    loadDirections(mViewPager.getCurrentItem());
                }
            });
        } else {
            Toast.makeText(this, "Load Location via GPS failed! Loading via network...", Toast.LENGTH_LONG).show();
            try {
                // Đoạn code nay cần người dùng cho phép (Hỏi ở trên ***).
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

                // Lấy ra vị trí.
                LatLng locationNet = new LatLng(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(),
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude());
                // Load Location via NetWork Provider
                MarkerOptions option = new MarkerOptions();
                option.title("My Location!");
                option.snippet(locationNet.latitude + "; " + locationNet.longitude);
                option.position(locationNet);
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start_marker));
                currentMarker = myMap.addMarker(option);
                currentMarker.setDraggable(true);
                currentMarker.showInfoWindow();

                final CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(locationNet)             // Sets the center of the map to location user
                        .zoom(16)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (Exception ignored) {

            }
        }

        myMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (movingMarker != null) {
                    movingMarker.remove();
                }
                MarkerOptions option = new MarkerOptions();
                option.title("My Location!");
                option.position(new LatLng(location.getLatitude(), location.getLongitude()));
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_placeholder));
                movingMarker = myMap.addMarker(option);
                movingMarker.showInfoWindow();
            }
        });
    }

    @Override
    public void onPageSelected(int position) {
        // Clear old direction
        if (mPolyline != null) {
            mPolyline.remove();
        }
        loadDirections(position);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mListMarkers.get(position).getPosition().latitude, mListMarkers.get(position).getPosition().longitude))             // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                .build();                   // Creates a CameraPosition from the builder
        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        try {
            if (previousSelectedMarker != null) {
                previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
            }
        } catch (Exception ignored) {

        }
        mListMarkers.get(position).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_selected));
        previousSelectedMarker = mListMarkers.get(position);
    }

    private void loadDirections(final int position) {
        try {
            mSoServiceDirection.getPlacesDirection(String.valueOf(currentMarker.getPosition().latitude)
                            + "," + String.valueOf(currentMarker.getPosition().longitude),
                    mListMarkers.get(position).getPosition().latitude
                            + "," + mListMarkers.get(position).getPosition().longitude,
                    settingsInterface.mode().get().toLowerCase(),
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
                                Toast.makeText(MapActivity.this, "Routes didn't load from API, please check internet and restart app again!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SOPlacesDirectionResponse> call, Throwable t) {
                            Toast.makeText(MapActivity.this, "Load Direction PlaceStop failed from API, please check internet and restart app again!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Request directions from Google error!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMap() {
        myMap.clear();
        if (previousSelectedMarker != null) {
            previousSelectedMarker.remove();
        }
        showMyLocation();
        mListMarkers.clear();
        mPlaceStops.clear();
        if (Objects.equals(positionCarriage, "0")) {
            mPlaceStops.addAll(mBusStopDatabase.getAllPlaces());
        } else {
            mPlaceStops.addAll(mBusStopDatabase.getPlacesByIdCarriage(positionCarriage));
        }
        if (mPlaceStops.size() > 0) {
            for (int j = 0; j < mPlaceStops.size(); j++) {
                MarkerOptions option = new MarkerOptions();
                option.title(mPlaceStops.get(j).getName());
                option.snippet(mPlaceStops.get(j).getLatitude()
                        + ";" + mPlaceStops.get(j).getLongitude());
                option.position(new LatLng(mPlaceStops.get(j).getLatitude(),
                        mPlaceStops.get(j).getLongitude()));
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                Marker marker = myMap.addMarker(option);
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
        final ArrayList<LatLng> arrCarriageDecode = new ArrayList<>();
        switch (carriage) {
            case "1":
                mCountDownTimer.cancel();
                arrCarriageDecode.addAll(CarriagePolyline.getCarriagePoly1());
                carriagePolyOption.color(Color.parseColor("#99FF373E"));
                break;
            case "2":
                mCountDownTimer.cancel();
                arrCarriageDecode.addAll(CarriagePolyline.getCarriagePoly2());
                carriagePolyOption.color(Color.parseColor("#88FFF837"));
                break;
            case "3":
                arrCarriageDecode.addAll(CarriagePolyline.getCarriagePoly3());
                carriagePolyOption.color(Color.parseColor("#7337FF37"));

                MarkerOptions option = new MarkerOptions();
                option.title("Bus here!");
                option.position(new LatLng(arrCarriageDecode.get(0).latitude, arrCarriageDecode.get(0).longitude));
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_marker));
                busMarker = myMap.addMarker(option);
//                busMarker.showInfoWindow();
                Log.d("bla", "drawCarriagePoly: " + arrCarriageDecode.size());
                mCountDownTimer = new CountDownTimer((arrCarriageDecode.size()) * 1000, 1000) {
                    int index = 0;
                    Double heading;
                    LatLng busPosition = new LatLng(arrCarriageDecode.get(0).latitude, arrCarriageDecode.get(0).longitude);
                    public void onTick(long millisUntilFinished) {
                        busPosition = new LatLng(arrCarriageDecode.get(index).latitude, arrCarriageDecode.get(index).longitude);
                        busMarker.setPosition(busPosition);
                        heading = SphericalUtil.computeHeading(new LatLng(arrCarriageDecode.get(index).latitude, arrCarriageDecode.get(index).longitude),
                                new LatLng(arrCarriageDecode.get(++index).latitude, arrCarriageDecode.get(++index).longitude));
                        if (index == arrCarriageDecode.size() - 1) {
                            cancel();
                        }
                        CameraPosition cameraBusPosition =
                                new CameraPosition.Builder()
                                        .target(busPosition)
                                        .bearing(heading.floatValue())
                                        .tilt(40)
                                        .zoom(16)
                                        .build();
                        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraBusPosition));
                    }

                    public void onFinish() {
                        busMarker.remove();
                    }
                }.start();
                break;
        }
        // Draw polylines

        for (int k = 0; k < arrCarriageDecode.size(); k++) {
            carriagePolyOption.add(new LatLng(arrCarriageDecode.get(k).latitude, arrCarriageDecode.get(k).longitude));
        }

        // Clear old Polyline
        if (mCarriagePolyline != null) {
            mCarriagePolyline.remove();
        }
        if (mAllCarriagePolyline != null) {
            mAllCarriagePolyline.remove();
        }
        mCarriagePolyline = myMap.addPolyline(carriagePolyOption);
    }

    private void drawAllCarriagePoly() {
        // points: overview_polyline
        ArrayList<LatLng> arrCarriageDecode1 = new ArrayList<>();
        ArrayList<LatLng> arrCarriageDecode2 = new ArrayList<>();
        ArrayList<LatLng> arrCarriageDecode3 = new ArrayList<>();

        arrCarriageDecode1.addAll(CarriagePolyline.getCarriagePoly1());
        arrCarriageDecode2.addAll(CarriagePolyline.getCarriagePoly2());
        arrCarriageDecode3.addAll(CarriagePolyline.getCarriagePoly3());
        // Draw polylines
        PolylineOptions carriagePolyOption1 = new PolylineOptions().geodesic(true).color(Color.parseColor("#99FF373E")).width(30);
        PolylineOptions carriagePolyOption2 = new PolylineOptions().geodesic(true).color(Color.parseColor("#88FFF837")).width(23);
        PolylineOptions carriagePolyOption3 = new PolylineOptions().geodesic(true).color(Color.parseColor("#7337FF37")).width(15);

        for (int k = 0; k < arrCarriageDecode1.size(); k++) {
            carriagePolyOption1.add(new LatLng(arrCarriageDecode1.get(k).latitude, arrCarriageDecode1.get(k).longitude));
        }
        for (int k = 0; k < arrCarriageDecode2.size(); k++) {
            carriagePolyOption2.add(new LatLng(arrCarriageDecode2.get(k).latitude, arrCarriageDecode2.get(k).longitude));
        }
        for (int k = 0; k < arrCarriageDecode3.size(); k++) {
            carriagePolyOption3.add(new LatLng(arrCarriageDecode3.get(k).latitude, arrCarriageDecode3.get(k).longitude));
        }

        // Clear old direction
        if (mCarriagePolyline != null) {
            mCarriagePolyline.remove();
        }
        mAllCarriagePolyline = myMap.addPolyline(carriagePolyOption1);
        mAllCarriagePolyline = myMap.addPolyline(carriagePolyOption2);
        mAllCarriagePolyline = myMap.addPolyline(carriagePolyOption3);
    }

    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<>();
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
            boolean needReload = data.getBooleanExtra("needReload", true);
            if (needReload) {
                finish();
                MapActivity_.intent(this).start();
            }
        }
    }

    @OnActivityResult(LIST_PLACES)
    void startActivityForResultList(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (!Objects.equals(positionCarriage, data.getStringExtra("idCarriage"))) {
                positionCarriage = data.getStringExtra("idCarriage");
                loadMap();
                if (Objects.equals(positionCarriage, "0")) {
                    drawAllCarriagePoly();
                } else {
                    drawCarriagePoly(positionCarriage);
                }
            }
            int idPlace = data.getIntExtra("idPlace", -1);
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
                myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mListMarkers.get(idPlace).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop_selected));
                loadDirections(idPlace);
                previousSelectedMarker = mListMarkers.get(idPlace);
            }
        }
    }

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
