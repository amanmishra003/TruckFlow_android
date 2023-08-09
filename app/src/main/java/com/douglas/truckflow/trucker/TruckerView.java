package com.douglas.truckflow.trucker;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.douglas.truckflow.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class TruckerView extends AppCompatActivity {

    private TextView loadNameTextView;
    private TextView loadDescriptionTextView;
    private TextView loadWeightTextView;

    private TextView loadLengthTextView;

    private TextView pickUpDateTextView;

    private TextView deliveryDateTextView;

    private TextView totalDistanceTextView;

    private TextView pickupAddressTextView;

    private TextView deliveryAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucker_view);


        loadNameTextView = findViewById(R.id.loadNameTextView);
        loadDescriptionTextView = findViewById(R.id.loadDescriptionTextView);
        loadWeightTextView = findViewById(R.id.loadWeightTextView);
        pickUpDateTextView = findViewById(R.id.pickUpDateTextView);
        deliveryAddressTextView = findViewById(R.id.deliveryAddressTextView);
        deliveryDateTextView = findViewById(R.id.deliveryDateTextView);
        totalDistanceTextView = findViewById(R.id.totalDistanceTextView);
        pickupAddressTextView = findViewById(R.id.pickupAddressTextView);
        loadLengthTextView =findViewById(R.id.loadLengthTextView);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("EMAIL_KEY")) {
            String email = getIntent().getStringExtra("EMAIL_KEY");

            Log.d("email incoming", email);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference loadsCollectionRef = db.collection("trucker");



            loadsCollectionRef.whereEqualTo("truckerEmail", email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot loadSnapshot : task.getResult()) {
                        // Retrieve the data from the snapshot
                        // Assuming loadSnapshot is a DocumentSnapshot or similar object that contains the data

// Retrieve the data for the new fields
                        String company_name = loadSnapshot.getString("company_name");
                        String company_phone = loadSnapshot.getString("company_phone");
                        String dot = loadSnapshot.getString("dot");
                        String mc = loadSnapshot.getString("mc");
                        String truck_name = loadSnapshot.getString("truck_name");
                        String truck_type = loadSnapshot.getString("truck_type");
                        String max_length = loadSnapshot.getString("max_length");
                        String max_weight = loadSnapshot.getString("max_weight");



// Set the retrieved data in the TextViews or any other appropriate views
                        loadNameTextView.setText("Company Name: " + company_name);
                        loadDescriptionTextView.setText("Company Phone: " + company_phone);
                        loadWeightTextView.setText("DOT: " + dot);
                        loadLengthTextView.setText("MC: " + mc);
                        pickUpDateTextView.setText("Truck Name: " + truck_name);
                        deliveryDateTextView.setText("Truck Type: " + truck_type);
                        totalDistanceTextView.setText("Max Length: " + max_length);
                        pickupAddressTextView.setText("Max Weight: " + max_weight);
// And so on for the rest of the fields...



                        // Set other data for the remaining fields
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }


            });
        }
    }
}