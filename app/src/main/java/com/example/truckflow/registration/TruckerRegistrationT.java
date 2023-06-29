package com.example.truckflow.registration;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.truckflow.R;
import com.example.truckflow.entities.Trucker;
import com.example.truckflow.home.Home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class TruckerRegistrationT extends AppCompatActivity {

    Button registerTruck;

    TextView truck_name, truck_type, max_length, max_wt;

    DatabaseReference databaseRef;
    FirebaseFirestore db;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trucker_registration_t);

        registerTruck = findViewById(R.id.registerTruck);

        truck_name = findViewById(R.id.truck_name);
        truck_type = findViewById(R.id.truck_type);
        max_length = findViewById(R.id.max_length);
        max_wt = findViewById(R.id.max_wt);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        registerTruck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String truckName = truck_name.getText().toString();
                String truckType = truck_type.getText().toString();
                String maxLength = max_length.getText().toString();
                String maxWt = max_wt.getText().toString();

                Intent intent = getIntent();
                if (intent != null) {
                    String companyName = intent.getStringExtra("companyName");
                    String companyPhone = intent.getStringExtra("companyPhone");
                    String dotValue = intent.getStringExtra("dotValue");
                    String mcValue = intent.getStringExtra("mcValue");

                    Trucker tck = new Trucker(companyName, companyPhone, dotValue, mcValue, truckName, truckType, maxLength, maxWt);

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

                Intent i = new Intent(TruckerRegistrationT.this, Home.class);
                startActivity(i);
            }
        });
    }
}
