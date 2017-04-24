package com.example.gio.bigproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.gio.bigproject.data.ApiUtilsBus;
import com.example.gio.bigproject.data.MockData;
import com.example.gio.bigproject.data.SOServiceDirection;
import com.example.gio.bigproject.model.bus_stop.Result;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@EActivity(R.layout.activity_main)
public class MapActivity extends AppCompatActivity implements LocationListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener, ViewPager.OnPageChangeListener {

    @ViewById(R.id.viewpager_location)
    ViewPager mViewPager;

    private SOServiceDirection mSoServiceDirection;
    private ArrayList<Marker> mListMarkers = new ArrayList<>();
    private ArrayList<RouteDirec> mRoutes = new ArrayList<>();
    private ArrayList<Result> mResults = new ArrayList<>();
    private GoogleMap myMap;
    private ProgressDialog myProgress;
    private Marker previousSelectedMarker;
    private Location myLocation;
    private Polyline mPolyline;

    // Request for location (***).
    // value 8bit (value < 256).
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;

    @AfterViews
    void afterViews() {

        // Request data from server
        MockData.createData();
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
    }

    private void onMyMapReady(GoogleMap googleMap) {
        // Get GoogleMap object:
        myMap = googleMap;

        // Map loaded
        myMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                // Đã tải thành công thì tắt Dialog Progress đi
                myProgress.dismiss();

                // Add Detail location
                FragmentManager fragmentManager = getSupportFragmentManager();
                ViewPagerMarkerAdapter mAdapter = new ViewPagerMarkerAdapter(fragmentManager);
                mViewPager.setAdapter(mAdapter);
                // Add marker
                mResults = MockData.getData();
                Log.d("MapActivity sizeResult", "onMyMapReady: " + mResults.size());
                if (mResults.size() > 0) {
                    for (int i = 0; i < mResults.size(); i++) {
                        MarkerOptions option = new MarkerOptions();
                        option.title(mResults.get(i).getName());
                        option.snippet(mResults.get(i).getGeometry().getLocation().getLat()
                                + ";" + mResults.get(i).getGeometry().getLocation().getLng());
                        option.position(new LatLng(mResults.get(i).getGeometry().getLocation().getLat(),
                                mResults.get(i).getGeometry().getLocation().getLng()));
                        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                        Marker marker = myMap.addMarker(option);
                        mListMarkers.add(marker);
                        marker.showInfoWindow();
                    }

                } else {
                    Toast.makeText(getBaseContext(), "Load API failed, Please restart app again!", Toast.LENGTH_SHORT).show();
                }

                myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        for (int i = 0; i < mListMarkers.size(); i++) {
                            if (marker.equals(mListMarkers.get(i))) {
                                mListMarkers.get(i).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_marker));
                                mViewPager.setCurrentItem(i, true);
                                if (previousSelectedMarker != null) {
                                    previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                                }
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_marker));
                                previousSelectedMarker = marker;
                            }
                        }
                        return false;
                    }
                });
                // Hiển thị vị trí người dùng.
                askPermissionsAndShowMyLocation();
            }
        });
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        String locationProvider = this.getEnabledLocationProvider();

        if (locationProvider == null) {
            return;
        }

        // Millisecond
        final long MIN_TIME_BW_UPDATES = 1000;
        // Met
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

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
            Toast.makeText(this, "Show My LocaBus Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        if (myLocation != null) {
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            final CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(16)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Thêm MyMarker cho Map:
            MarkerOptions option = new MarkerOptions();
            option.title("My LocaBus!");
            option.snippet(myLocation.getLatitude() + "+" + myLocation.getLongitude());
            option.position(latLng);
            option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location));
            final Marker currentMarker = myMap.addMarker(option);
            currentMarker.showInfoWindow();

            myMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    if (previousSelectedMarker != null) {
                        previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
                    }
                    myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    currentMarker.showInfoWindow();

                    return true;
                }
            });
        } else {
            Toast.makeText(this, "LocaBus not found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPageSelected(int position) {
        loadDirections(position);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mListMarkers.get(position).getPosition().latitude, mListMarkers.get(position).getPosition().longitude))             // Sets the center of the map to location user
                .zoom(16)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                .build();                   // Creates a CameraPosition from the builder
        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (previousSelectedMarker != null) {
            previousSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_stop24));
        }
        mListMarkers.get(position).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus_marker));

        previousSelectedMarker = mListMarkers.get(position);
    }

    private void loadDirections(final int position) {
        mSoServiceDirection.getPlacesDirection(String.valueOf(myLocation.getLatitude())
                        + "," + String.valueOf(myLocation.getLongitude()),
                mListMarkers.get(position).getPosition().latitude
                        + "," + mListMarkers.get(position).getPosition().longitude,
                "walking",
                ApiUtilsBus.KEY)
                .enqueue(new Callback<SOPlacesDirectionResponse>() {
                    @Override
                    public void onResponse(Call<SOPlacesDirectionResponse> call, Response<SOPlacesDirectionResponse> response) {

                        if (response.isSuccessful()) {
                            mRoutes.clear();
                            mRoutes.addAll(response.body().getRoutes());

                            // points: overview_polyline
                            ArrayList<LatLng> arrDecode = decodePoly(mRoutes.get(0).getOverViewPolyline().getPoints());
                            // Draw polylines
                            PolylineOptions polyOp = new PolylineOptions().geodesic(true).color(Color.BLUE).width(10);
                            for (int i = 0; i < arrDecode.size(); i++) {
                                polyOp.add(new LatLng(arrDecode.get(i).latitude, arrDecode.get(i).longitude));
                            }
                            // Clear old direction
                            if (mPolyline != null) {
                                mPolyline.remove();
                            }
                            mPolyline = myMap.addPolyline(polyOp);

                            // Display distance and duration of Destination
                            if (mRoutes.size() > 0) {
                                mListMarkers.get(position).setSnippet(mRoutes.get(0).getLegs().get(0).getDistance().getText()
                                        + "; " + mRoutes.get(0).getLegs().get(0).getDuration().getText());
                                mListMarkers.get(position).showInfoWindow();
                            }
                            Log.d("MapActivity", "Routes loaded from API placeDirec Steps = " + mRoutes.get(0).getLegs().get(0).getSteps().size());
                        } else {
//                            Log.d("MapActivity", "Routes didn't load from API: ");
                            Toast.makeText(MapActivity.this, "Routes didn't load from API, please check internet and restart app again!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SOPlacesDirectionResponse> call, Throwable t) {
//                        Log.d("", "onFailure: " + call.request().url().toString());
//                        Log.d("MapActivity", "Load Direc Places failed from API");
                        Toast.makeText(MapActivity.this, "Load Direction Places failed from API, please check internet and restart app again!", Toast.LENGTH_SHORT).show();
                    }
                });
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
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
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
