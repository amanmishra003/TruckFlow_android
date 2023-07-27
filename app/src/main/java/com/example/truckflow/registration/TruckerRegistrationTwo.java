package com.example.truckflow.registration;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.truckflow.R;
import com.example.truckflow.authentication.Login;
import com.example.truckflow.entities.Trucker;
import com.example.truckflow.home.Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class TruckerRegistrationTwo extends AppCompatActivity {

    Button submit;

    TextInputLayout truck_name, truck_type, max_length, max_wt;

    RequestQueue requestQueue;

    Button registerTruck;
    DatabaseReference databaseRef;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trucker_registration_two);

//        requestQueue = Volley.newRequestQueue(this);
        registerTruck = findViewById(R.id.finalRegButton);

        truck_name = findViewById(R.id.truck_name);
        truck_type = findViewById(R.id.truck_type);

        max_length = findViewById(R.id.max_length);
        max_wt = findViewById(R.id.maxweight);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();


        registerTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String truckName = truck_name.getEditText().getText().toString();
                String truckType = truck_type.getEditText().getText().toString();
                String maxLength = max_length.getEditText().getText().toString();
                String maxWt = max_wt.getEditText().getText().toString();

                if (truckName.isEmpty()) {
                    truck_name.setError("Truck name is required");
                    return;
                } else {
                    truck_name.setError(null);
                }

                if (truckType.isEmpty()) {
                    truck_type.setError("Truck type is required");
                    return;
                } else {
                    truck_type.setError(null);
                }

                if (maxLength.isEmpty()) {
                    max_length.setError("Maximum length is required");
                    return;
                } else {
                    max_length.setError(null);
                }

                if (maxWt.isEmpty()) {
                    max_wt.setError("Maximum weight is required");
                    return;
                } else {
                    max_wt.setError(null);
                }

                Intent intent = getIntent();
                if (intent != null) {
                    String companyName = intent.getStringExtra("companyName");
                    String companyPhone = intent.getStringExtra("companyPhone");
                    String dotValue = intent.getStringExtra("dotValue");
                    String mcValue = intent.getStringExtra("mcValue");
                    String email = intent.getStringExtra("Email");

                    Trucker tck = new Trucker(companyName, companyPhone, dotValue, mcValue, truckName, truckType, maxLength, maxWt, email);

                    UUID uuid = UUID.randomUUID();

                    databaseRef.child("trucker").child(String.valueOf(uuid)).setValue(tck)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Trucker data saved to Firebase Realtime Database");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Error saving trucker data to Firebase Realtime Database", e);
                                }
                            });

                    db.collection("trucker")
                            .add(tck)
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
                }

                Toast.makeText(TruckerRegistrationTwo.this, "User successfully created! You can 4 Now", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TruckerRegistrationTwo.this, Login.class);

                if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("IMAGE_FILE_NAME")) {
                    String fileName = getIntent().getStringExtra("IMAGE_FILE_NAME");
                    i.putExtra("IMAGE_FILE_NAME", fileName);
                    Log.d("fileName trucker reg 2", fileName);
                    startActivity(i);
                }
                startActivity(i);
            }
        });
    }
}
