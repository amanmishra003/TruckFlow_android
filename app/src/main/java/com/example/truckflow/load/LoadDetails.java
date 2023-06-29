package com.example.truckflow.load;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.truckflow.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class LoadDetails extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_dts);

        // Initialize the MapView with the latest renderer
        MapsInitializer.initialize(getApplicationContext());

        mapView = findViewById(R.id.load_dts_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Customize the map settings
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        //example
        // Define the latitude and longitude values for location1
        double latitude1 = 37.7749;
        double longitude1 = -122.4194;

        // Define the latitude and longitude values for location2
        double latitude2 = 34.0522;
        double longitude2 = -118.2437;

        // Add markers to the map
        LatLng pickupLatLng = new LatLng(latitude1, longitude1);
        LatLng dropLatLng = new LatLng(latitude2, longitude2);
        // Adding markers to the map
        // Adding markers to the map
        MarkerOptions pickupMarkerOptions = new MarkerOptions()
                .position(pickupLatLng)
                .title("Pickup");
        googleMap.addMarker(pickupMarkerOptions);

        MarkerOptions dropMarkerOptions = new MarkerOptions()
                .position(dropLatLng)
                .title("Drop");
        googleMap.addMarker(dropMarkerOptions);


        // Adding polyline
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(pickupLatLng, dropLatLng)
                .color(Color.BLUE)
                .width(5);
        googleMap.addPolyline(polylineOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pickupLatLng)
                .zoom(5)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();}
}