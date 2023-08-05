package com.example.truckflow.load;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.example.truckflow.R;
import com.example.truckflow.communication.ChatActivity;
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
    private TextView loadName;
    private TextView loadDescription;
    private TextView loadWeight;
    private TextView loadLength;
    private TextView pickUpDate;
    private TextView deliveryDate;
    private TextView totalDistance;
    private TextView pickupAddress;
    private TextView deliveryAddress;
    private TextView expectedPrice;
    private  TextView requirement;
    private TextView contactInfo;

    private Button bookLoad;

    private double longitudePU,longitudeDel,latitudeDel,latitudePU;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_dts);

        // Initialize TextViews
        loadName = findViewById(R.id.load_name);
        loadDescription = findViewById(R.id.load_desc_value);
        loadWeight = findViewById(R.id.load_dts_weight_value);
        loadLength = findViewById(R.id.load_dts_length_value);
        pickUpDate= findViewById(R.id.load_dts_pickDate_value);
        deliveryDate = findViewById(R.id.load_dts_dropDate_value);
        totalDistance = findViewById(R.id.load_dts_distance_value);
        pickupAddress= findViewById(R.id.load_pick_loc);
        deliveryAddress = findViewById(R.id.load_drop_loc);
        expectedPrice = findViewById(R.id.load_dts_price_value);
        requirement = findViewById(R.id.ad_req_value);
        contactInfo =findViewById(R.id.contact_info_value);
        bookLoad  =findViewById(R.id.button_bookLoad);


        // Retrieve data from the intent
        Intent intent = getIntent();
        String loadId = intent.getStringExtra("loadId");
        String loadNameValue = intent.getStringExtra("loadName");
        String loadDescriptionValue = intent.getStringExtra("loadDescription");
        String loadWeightValue = intent.getStringExtra("loadWeight");
        String loadLengthValue = intent.getStringExtra("loadLength");
        String pickUpDateValue = intent.getStringExtra("pickUpDate");
        String deliveryDateValue = intent.getStringExtra("deliveryDate");
        String totalDistanceValue = intent.getStringExtra("totalDistance");
        String pickupAddressValue = intent.getStringExtra("pickupAddress");
        String deliveryAddressValue = intent.getStringExtra("deliveryAddress");
        String expectedPriceValue = intent.getStringExtra("expectedPrice");
        String contactInformationValue = intent.getStringExtra("contactInformation");
        String requirementValue = intent.getStringExtra("requirement");
        String shipperId = intent.getStringExtra("shipperId");
        Log.i("Latitude::",intent.getStringExtra("latitudePU"));
        latitudePU = Double.parseDouble(intent.getStringExtra("latitudePU"));
        longitudePU = Double.parseDouble(intent.getStringExtra("longitudePU"));
        latitudeDel = Double.parseDouble(intent.getStringExtra("latitudeDel"));

        longitudeDel = Double.parseDouble(intent.getStringExtra("longitudeDel"));

        // Set the data to TextViews
        loadName.setText(loadNameValue);
        loadDescription.setText(loadDescriptionValue);
        loadWeight.setText(loadWeightValue+" lbs");
        loadLength.setText(loadLengthValue+" ft");
        pickUpDate.setText(pickUpDateValue);
        deliveryDate.setText(deliveryDateValue);
        totalDistance.setText(totalDistanceValue+" km");
        pickupAddress.setText(pickupAddressValue);
        deliveryAddress.setText(deliveryAddressValue);
        expectedPrice.setText(expectedPriceValue);
        requirement.setText(requirementValue);
        contactInfo.setText(contactInformationValue);

        // Initialize the MapView with the latest renderer
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            Log.e("LoadDetails", "Error initializing Google Maps: " + e.getMessage());
        }

        mapView = findViewById(R.id.load_dts_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //Taking to chat activity
        bookLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoadDetails.this, ChatActivity.class);
                intent.putExtra("shipperId", shipperId);
                intent.putExtra("loadId",loadId);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Customize the map settings
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        Intent intent = getIntent();

        double latitude1=49.17447560000001,latitude2=49.17447560000001;
        double longitude1=-122.7724726,longitude2=-122.7724726;
        //example
        // Define the latitude and longitude values for location1
        Log.i("Latitude PU",intent.getStringExtra("latitudePU")+"");
        /*if(intent.getStringExtra("latitudePU")!=null) {
             latitude1 = Double.parseDouble(intent.getStringExtra("latitudePU"));
             longitude1 = Double.parseDouble(intent.getStringExtra("longitudePU"));

        }

        // Define the latitude and longitude values for location2
        if(intent.getStringExtra("latitudeDel")!=null) {

            latitude2 = Double.parseDouble(intent.getStringExtra("latitudeDel"));
            ;
            longitude2 = Double.parseDouble(intent.getStringExtra("longitudeDel"));
            ;
        }*/
        // Add markers to the map
        LatLng pickupLatLng = new LatLng(latitudePU, longitudePU);
        Log.i("pickupLatlang.",pickupLatLng.toString());
        LatLng dropLatLng = new LatLng(latitudeDel, longitudeDel);
        Log.i("deliveryLatLang.",dropLatLng.toString());

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
                .zoom(10)
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