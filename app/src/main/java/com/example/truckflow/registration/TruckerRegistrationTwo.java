package com.example.truckflow.registration;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.truckflow.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trucker_registration_two);

        submit = findViewById(R.id.postLoadButton);

        requestQueue = Volley.newRequestQueue(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TruckerRegistrationTwo.this, Home.class);

                Intent intent = getIntent();
                if (intent != null) {
                    String companyName = intent.getStringExtra("companyName");
                    String companyPhone = intent.getStringExtra("companyPhone");
                    String dotValue = intent.getStringExtra("dotValue");
                    String mcValue = intent.getStringExtra("mcValue");

                    Trucker tck = new Trucker(companyName, companyPhone, dotValue, mcValue);

                    UUID uuid = UUID.randomUUID();

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                    databaseRef.child("trucker").child(String.valueOf(uuid)).setValue(tck);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("trucker")
                            .add(tck)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }

                // Start the next activity
                startActivity(i);
            }
        });
    }
}
