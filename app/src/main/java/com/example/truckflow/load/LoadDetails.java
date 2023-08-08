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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.FirebaseApp;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private RequestQueue requestQueue;


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

        requestQueue = Volley.newRequestQueue(this);
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

        // Add markers to the map
        LatLng pickupLatLng = new LatLng(latitudePU, longitudePU);
        Log.i("pickupLatlang.",pickupLatLng.toString());
        LatLng dropLatLng = new LatLng(latitudeDel, longitudeDel);
        Log.i("deliveryLatLang.",dropLatLng.toString());

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
       /* PolylineOptions polylineOptions = new PolylineOptions()
                .add(pickupLatLng, dropLatLng)
                .color(Color.BLUE)
                .width(5);
        googleMap.addPolyline(polylineOptions);
        */


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pickupLatLng)
                .zoom(10)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        drawRoutePath(googleMap, pickupLatLng, dropLatLng);

    }

    private void drawRoutePath(GoogleMap googleMap, LatLng pickupLatLng, LatLng dropLatLng) {
        String baseUrl = "https://maps.googleapis.com/maps/api/directions/json";
        String apiKey = "AIzaSyAtw3f2NBYcbNVz01pmZPfZnQlOwnoErNk";

        String url = baseUrl + "?origin=" + pickupLatLng.latitude + "," + pickupLatLng.longitude
                + "&destination=" + dropLatLng.latitude + "," + dropLatLng.longitude
                + "&mode=driving&key=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON response and draw the route on the map
                        try {
                            JSONArray routesArray = response.getJSONArray("routes");
                            if (routesArray.length() > 0) {
                                JSONObject routeObject = routesArray.getJSONObject(0);
                                JSONObject overviewPolylineObject = routeObject.getJSONObject("overview_polyline");
                                String points = overviewPolylineObject.getString("points");

                                // Decode the polyline points
                                List<LatLng> latLngPoints = decodePolyline(points);

                                // Draw the polyline on the map
                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .addAll(latLngPoints)
                                        .color(Color.BLUE)
                                        .width(5);
                                Polyline polyline = googleMap.addPolyline(polylineOptions);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private List<LatLng> decodePolyline(String encoded) {
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
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }
        return poly;
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