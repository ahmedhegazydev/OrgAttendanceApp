package com.example.ahmed.orgattendanceapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.arsy.maps_library.MapRipple;
import com.example.ahmed.orgattendanceapp.R;
import com.example.ahmed.orgattendanceapp.activities.EmployerActivity;
import com.example.ahmed.orgattendanceapp.common.Common;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapEmployer extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback, GoogleMap.OnInfoWindowCloseListener, GoogleMap.OnInfoWindowLongClickListener {
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1233;
    final static int PLAY_SERVICES_REF_REQUEST = 12345;
    final static int DISTANCE = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 10;
    MapView mMapView = null;
    GoogleMap googleMap = null;
    Context context = null;
    LatLng latLngCurrentLoc = null;
    MapRipple mapRippleCurrent = null;
    LocationRequest locationRequest = null;
    int withStrokewidth = 6, withRippleDuration = 5000;
    int withNumberOfRipples = 10;
    int withDistance = 20;
    @BindView(R.id.btnGetMyLocationNow)
    ImageButton ibGetCurrentLoc;
    int PLACE_PICKER_REQUEST = 1;
    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    Marker markerRegisteredPlace = null;
    @BindView(R.id.btnRegisterThis)
    Button btnRegisterThisPlace;
    @BindView(R.id.btnSelectPlaceForCompany)
    Button btnSelectThisPlaceForComp;
    Common common = null;
    boolean b = true;
    Animation animSlideUp, animSlideDown, animSlideUp2, animSlideDown2;
    private Location lastLocation = null;
    private GoogleApiClient googleApiClient = null;
    private Marker markerCurrentLocation = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_employer, container, false);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        getLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);


    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null)
            stopLocationUpdates();

        if (mapRippleCurrent != null) {
            if (mapRippleCurrent.isAnimationRunning()) mapRippleCurrent.stopRippleMapAnimation();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (googleApiClient != null)
            stopLocationUpdates();
    }

    @OnClick(R.id.btnSelectPlaceForCompany)
    public void registerPlaceForComp(View v) {
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //checkLocationPermission();
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                startLocationUpdates();
                getLastLocation();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (googleApiClient != null)
            stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        //Log.i(TAG, "stopLocationUpdates");
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            return;
        }
        if (googleApiClient != null) {
            if (googleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        this.lastLocation = location;
        setCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    @Override
    public void onCameraIdle() {

        ibGetCurrentLoc.setVisibility(View.VISIBLE);
        ibGetCurrentLoc.startAnimation(animSlideUp);

        btnRegisterThisPlace.setVisibility(View.VISIBLE);
        btnRegisterThisPlace.startAnimation(animSlideUp);


        //btnSelectThisPlaceForComp.setVisibility(View.VISIBLE);
        btnSelectThisPlaceForComp.startAnimation(common.slideToBottom(btnSelectThisPlaceForComp));

        b = true;


    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {
        if (ibGetCurrentLoc.getVisibility() == View.GONE) {
            ibGetCurrentLoc.setVisibility(View.VISIBLE);
        }

        if (b) {
            ibGetCurrentLoc.startAnimation(animSlideDown);
            ibGetCurrentLoc.setVisibility(View.GONE);

            btnRegisterThisPlace.startAnimation(animSlideDown);
            btnRegisterThisPlace.setVisibility(View.GONE);

            //btnSelectThisPlaceForComp.startAnimation(animSlideUp2);
            //btnSelectThisPlaceForComp.setVisibility(View.GONE);
            btnSelectThisPlaceForComp.startAnimation(common.slideToTop(btnSelectThisPlaceForComp));


            b = false;
        }
    }

    @Override
    public void onCameraMoveStarted(int i) {


    }

    @Override
    public void onMapClick(LatLng latLng) {
        onMapCliclAction(latLng);
    }

    private void onMapCliclAction(LatLng latLng) {
        if (markerRegisteredPlace != null) markerRegisteredPlace.remove();
        if (markerCurrentLocation != null) markerCurrentLocation.remove();
        markerCurrentLocation = googleMap.addMarker(new MarkerOptions().title("My Company is here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.company)).position(latLng));

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVars(view);
        initAnimations();
        checkLocationPermission();
        initMap(view, savedInstanceState);

    }

    private void initAnimations() {
        animSlideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        animSlideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);

        animSlideDown2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down2);
        animSlideUp2 = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up2);
    }

    private void initVars(View view) {
        context = MapEmployer.this.getActivity();

        ButterKnife.bind(this, view);

        ibGetCurrentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentLocation(null);
                ibGetCurrentLoc.setVisibility(View.GONE);
            }
        });

        common = new Common(context);


    }

    private void initMap(View rootView, Bundle savedInstanceState) {

        mMapView = rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mMapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap mMap) {
//                googleMap = mMap;
//                decoreMyMapView();
//                checkLocationPermission();
//
//            }
//        });
        mMapView.getMapAsync(MapEmployer.this);


    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            setCurrentLocation(null);
        }

    }

    private void setCurrentLocation(LatLng latLngCurrentLoc) {

        if (latLngCurrentLoc == null) {
            latLngCurrentLoc = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }

        if (markerCurrentLocation != null) {
            markerCurrentLocation.remove();
        }
        if (markerRegisteredPlace != null) {
            markerRegisteredPlace.remove();
        }
        markerCurrentLocation = googleMap.addMarker(new MarkerOptions()
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))//by default
                        //.icon(createAppropIconForThisDriver(userDriver.getCarCatName()))
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                        .title("My Location")
                        //.snippet(setCarCatName(userDriver.getCarCatName()))
                        //.snippet(userDriver.getCarModelNumber() + "  " + userDriver.getCarCatName())
                        .position(latLngCurrentLoc)
                        .flat(false)
                //.draggable(false)
        );

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrentLoc, 15.0f));
        if (mapRippleCurrent != null) {
            if (mapRippleCurrent.isAnimationRunning())
                mapRippleCurrent.stopRippleMapAnimation();
        }
        //addRippleToMap();
    }

    private void addRippleToMap() {

        mapRippleCurrent = new MapRipple(googleMap, latLngCurrentLoc, context);
        mapRippleCurrent.withNumberOfRipples(withNumberOfRipples);
//            mapRippleCurrent.withFillColor(Color.parseColor("#FFA3D2E4"));
        mapRippleCurrent.withStrokeColor(Color.BLACK);
        mapRippleCurrent.withStrokewidth(withStrokewidth);      // 10dp
        mapRippleCurrent.withDistance(withDistance);      // 2000 metres radius
        mapRippleCurrent.withRippleDuration(withRippleDuration);    //12000ms
//            mapRippleCurrent.withTransparency(0.5f);
        mapRippleCurrent.startRippleMapAnimation();

    }

    private void decoreMyMapView() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.setTrafficEnabled(true);

        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        //or myMap.getUiSettings().setAllGesturesEnabled(true);


        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnCameraMoveStartedListener(this);
        googleMap.setOnCameraMoveCanceledListener(this);
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnInfoWindowCloseListener(this);
        googleMap.setOnInfoWindowLongClickListener(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        getLastLocation();

                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();

        if (mapRippleCurrent != null) {
            if (mapRippleCurrent.isAnimationRunning()) mapRippleCurrent.stopRippleMapAnimation();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        if (mapRippleCurrent != null) {
            if (mapRippleCurrent.isAnimationRunning()) mapRippleCurrent.stopRippleMapAnimation();
        }


    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                getLastLocation();
            }
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_REF_REQUEST).show();
            } else {
                Toast.makeText(getActivity(), "Google Play Services are not available", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }

            return false;
        }

        return true;

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISTANCE);
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(MapEmployer.this)
                .addOnConnectionFailedListener(MapEmployer.this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        googleApiClient.connect();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == EmployerActivity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                //String toastMsg = String.format("Place: %s", place.getName());
                goToSelectedPlaceOnMap(place.getLatLng());
            }
        }

    }

    private void goToSelectedPlaceOnMap(LatLng latLng) {

        if (markerCurrentLocation != null) {
            markerCurrentLocation.remove();
        }

        if (markerRegisteredPlace != null) {
            markerRegisteredPlace.remove();
        }
        markerRegisteredPlace = googleMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.company))
                        .title("Company is here")
                        .snippet("")
                        .position(latLng)
                        .flat(false)
                //.draggable(false)
        );

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngCurrentLoc, 15.0f));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        decoreMyMapView();
        //checkLocationPermission();
        //getLastLocation();

    }

    @Override
    public void onInfoWindowClick(Marker marker) {


    }

    @Override
    public void onInfoWindowClose(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }
}