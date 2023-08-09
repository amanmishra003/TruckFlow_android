package com.example.truckflow.load;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truckflow.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoadView extends AppCompatActivity {

    private TextView loadNameTextView;
    private TextView loadDescriptionTextView;
    private TextView loadWeightTextView;

    private TextView loadLengthTextView;

    private TextView pickUpDateTextView;

    private TextView deliveryDateTextView;

    private TextView totalDistanceTextView;

    private TextView pickupAddressTextView;

    private TextView deliveryAddressTextView;

    private TextView expectedPriceTextView;

    private TextView contactInformationTextView;

    private TextView durationInHoursTextView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_view);

        loadNameTextView = findViewById(R.id.loadNameTextView);
        loadDescriptionTextView = findViewById(R.id.loadDescriptionTextView);
        loadWeightTextView = findViewById(R.id.loadWeightTextView);
        pickUpDateTextView = findViewById(R.id.pickUpDateTextView);
        contactInformationTextView = findViewById(R.id.contactInformationTextView);
        expectedPriceTextView = findViewById(R.id.expectedPriceTextView);
        deliveryAddressTextView = findViewById(R.id.deliveryAddressTextView);
        deliveryDateTextView = findViewById(R.id.deliveryDateTextView);
        totalDistanceTextView = findViewById(R.id.totalDistanceTextView);
        pickupAddressTextView = findViewById(R.id.pickupAddressTextView);
        loadLengthTextView =findViewById(R.id.loadLengthTextView);
        durationInHoursTextView = findViewById(R.id.durationInHoursTextView);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("EMAIL_KEY")) {
            String email = getIntent().getStringExtra("EMAIL_KEY");

            Log.d("email incoming", email);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference loadsCollectionRef = db.collection("load");



            loadsCollectionRef.whereEqualTo("contactInformation", email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot loadSnapshot : task.getResult()) {
                        // Retrieve the data from the snapshot
                        String loadName = loadSnapshot.getString("loadName");
                        String loadDescription = loadSnapshot.getString("loadDescription");
                        String loadWeight = loadSnapshot.getString("loadWeight");
                        String loadLength = loadSnapshot.getString("loadLength");
                        String pickUpDate = loadSnapshot.getString("pickUpDate");
                        String deliveryDate = loadSnapshot.getString("deliveryDate");
                        String totalDistance = loadSnapshot.getString("totalDistance");
                        String pickupAddress = loadSnapshot.getString("pickupAddress");
                        String deliveryAddress = loadSnapshot.getString("deliveryAddress");
                        String expectedPrice = loadSnapshot.getString("expectedPrice");
                        String contactInformation = loadSnapshot.getString("contactInformation");
                        String durationInHours = loadSnapshot.getString("durationInHours");

                        Log.d("db value", loadName);
                        // Retrieve other data for the remaining fields

                        // Set the retrieved data in the TextViews
                        loadNameTextView.setText("Load Name: " + loadName);
                        loadDescriptionTextView.setText("Load Description: " + loadDescription);
                        loadWeightTextView.setText("Load Weight: " + loadWeight);
                        loadLengthTextView.setText("Load Length: " + loadLength);
                        pickUpDateTextView.setText("Pickup Date: " + pickUpDate);
                        deliveryDateTextView.setText("Delivery Date: " + deliveryDate);
                        totalDistanceTextView.setText("Total Distance: " + totalDistance);
                        pickupAddressTextView.setText("Pickup Address: " + pickupAddress);
                        deliveryAddressTextView.setText("Delivery Address: " + deliveryAddress);
                        expectedPriceTextView.setText("Expected Price: " + expectedPrice);
                        contactInformationTextView.setText("Contact Information: " + contactInformation);
                        durationInHoursTextView.setText("Duration (Hours): " + durationInHours);

                        // Set other data for the remaining fields
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }


            });
        }
    }
}