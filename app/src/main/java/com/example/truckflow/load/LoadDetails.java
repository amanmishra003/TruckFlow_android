package com.example.truckflow.load;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.truckflow.R;
import com.example.truckflow.communication.ChatActivity;
import com.example.truckflow.entities.BookingRequest;
import com.example.truckflow.entities.Trucker;
import com.example.truckflow.entities.User;
import com.example.truckflow.utils.FireBaseUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    private Button bookLoad,startChat;

    private Trucker truckerDetail;
    private RequestQueue requestQueue;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    String token;

    DatabaseReference databaseRef;
    FirebaseFirestore db;
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
        startChat = findViewById(R.id.send_btn);

        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoadDetails.this,ChatActivity.class);

                startActivity(i);

            }
        });

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
        contactInfo.setText(shipperId);

        // Initialize the MapView with the latest renderer
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            Log.e("LoadDetails", "Error initializing Google Maps: " + e.getMessage());
        }

        mapView = findViewById(R.id.load_dts_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        startChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoadDetails.this,ChatActivity.class);
                i.putExtra("receiverId",shipperId);
                startActivity(i);

            }
        });
        User user = FireBaseUtils.getCurrentUserDetails(this);

        FireBaseUtils.getCurrentTruckerDetails(user.email, new FireBaseUtils.FirestoreTruckerCallback() {
            @Override
            public void onTruckerReceived(Trucker trucker) {
                if (trucker != null) {
                    truckerDetail = trucker;
                } else {
                    // Handle the case where trucker details were not found
                }
            }
        });//Taking to chat activity
        bookLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = "TruckFlow";
                NOTIFICATION_MESSAGE = "You have received a booking request.";

                getTokenForUser(new FirestoreTokenCallBack() {
                    @Override
                    public void onTokenRecieved(String token) {
                        if (token != null) {
                            Log.i("RecievedToken", token);
                            sendNotification(NOTIFICATION_TITLE, NOTIFICATION_MESSAGE, token);
                        } else {
                            Log.e("RecievedToken", "Token not available");
                        }
                    }
                }, shipperId);


                BookingRequest bookingRequest = new BookingRequest();

                bookingRequest.setLoadId(loadId);
                bookingRequest.setShipperId(shipperId);
                bookingRequest.setLoadName(loadNameValue);
                bookingRequest.setLoadDescription(loadDescriptionValue);
                bookingRequest.setLoadWeight(loadWeightValue);
                bookingRequest.setLoadLength(loadLengthValue);
                bookingRequest.setPickUpDate(pickUpDateValue);
                bookingRequest.setDeliveryDate(deliveryDateValue);
                bookingRequest.setTotalDistance(totalDistanceValue);
                bookingRequest.setPickupAddress(pickupAddressValue);
                bookingRequest.setDeliveryAddress(deliveryAddressValue);
                bookingRequest.setExpectedPrice(expectedPriceValue);
                bookingRequest.setContactInformation(shipperId);
                bookingRequest.setDurationInHours("");

                bookingRequest.setCompany_name(truckerDetail.company_name);
                bookingRequest.setTruckerEmail(truckerDetail.truckerEmail);
                bookingRequest.setCompany_phone(truckerDetail.getCompany_phone());
                bookingRequest.setTruck_name(truckerDetail.truck_name);
                bookingRequest.setTruck_type(truckerDetail.getTruck_type());
                bookingRequest.setMax_length(truckerDetail.max_length);

                databaseRef = FirebaseDatabase.getInstance().getReference();
                db = FirebaseFirestore.getInstance();

                UUID uuid = UUID.randomUUID();
                databaseRef.child("bookingRequest").child(String.valueOf(uuid)).setValue(bookingRequest)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Load data saved to Firebase Realtime Database");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error saving Load data to Firebase Realtime Database", e);
                            }
                        });

                db.collection("bookingRequest")
                        .add(bookingRequest)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Trucker data saved to Firestore with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error saving trucker data to Firestore", e);
                            }
                        });
                Intent intent = new Intent(LoadDetails.this, ChatActivity.class);
                intent.putExtra("receiverId", shipperId);
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
            // Extract latitude value from encoded string
            do {
                b = encoded.charAt(index++) - 63; // Decode latitude value
                result |= (b & 0x1f) << shift; // Bitwise OR operation to combine bits
                shift += 5; // Increment shift by 5
            } while (b >= 0x20); // Check if the value is greater than or equal to 0x20

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat; // Calculate the latitude

            shift = 0;
            result = 0;
            // Extract longitude value from encoded string
            do {
                b = encoded.charAt(index++) - 63; // Decode longitude value
                result |= (b & 0x1f) << shift; // Bitwise OR operation to combine bits
                shift += 5; // Increment shift by 5
            } while (b >= 0x20); // Check if the value is greater than or equal to 0x20

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng; // Calculate the longitude

            // Create a LatLng object from decoded latitude and longitude
            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p); // Add the LatLng object to the list
        }
        return poly; // Return the list of LatLng objects representing the polyline
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
    private void sendNotification(String title, String body, String recipientToken) {
        String serverKey = "AAAAEoGhiow:APA91bG4upNFfSvy77aiH6_mQLrJGeIhugP0Pyk0XemIc8N59IoIR1KESjoeWXZi1SaThtcguf2JILO9ES8PBZ2zpnY4eiFf-pHlgtcrkNDtkqhJb0iX0ykUoVDp1ilA9hvSx0K_fv2c"; // Replace with your FCM server key
        String contentType = "application/json";
        String FCM_API = "https://fcm.googleapis.com/fcm/send";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        try {
            JSONObject data = new JSONObject();
            data.put("title", title);
            data.put("body", body);

            JSONArray registrationIds = new JSONArray();
            registrationIds.put(recipientToken);

            JSONObject notification = new JSONObject();
            notification.put("registration_ids", registrationIds);
            notification.put("notification", data);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                    response -> Log.i("Notification", "Notification sent successfully"),
                    error -> Log.e("Notification", "Error sending notification: " + error.toString())
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + serverKey);
                    headers.put("Content-Type", contentType);
                    return headers;
                }
            };
            Log.i("Request Send", jsonObjectRequest.toString());
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void getTokenForUser(FirestoreTokenCallBack callback, String shipperId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("users").whereEqualTo("email", shipperId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // ... (get token and log values)
                    String fullName = document.getString("name");
                    String phoneNo = document.getString("phone");
                    String password = document.getString("password");
                    String role = document.getString("role");
                    token = document.getString("token");
                    callback.onTokenRecieved(token);
                    return;
                }
                callback.onTokenRecieved(null); // Token not found
            } else {
                callback.onTokenRecieved(null); // Error occurred
                Log.d("UserProfile", "Error getting documents: ", task.getException());
            }
        });
    }
    public interface FirestoreTokenCallBack {
        void onTokenRecieved(String token);
    }

}
