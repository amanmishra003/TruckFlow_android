package com.example.truckflow.authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.example.truckflow.R;
import com.example.truckflow.entities.User;
import com.example.truckflow.registration.SignUpChoice;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class SignUp extends AppCompatActivity {

    TextInputLayout phone,email,password,f_name;
    Button submit_registration;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //instance initiated
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        RadioButton radioButtonShipper;
        RadioButton radioButtonTrucker;

        f_name = findViewById(R.id.full_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        radioButtonShipper = findViewById(R.id.radioButtonShipper);
        radioButtonTrucker = findViewById(R.id.radioButtonTrucker);

        String selectedOption;

        if (radioButtonShipper.isChecked()) {
            selectedOption = "shipper";
        } else if (radioButtonTrucker.isChecked()) {
            selectedOption = "trucker";
        } else {
            // Handle the case when neither RadioButton is selected
            selectedOption = ""; // or any other default value
        }



        String url = getString(R.string.reg_api_url);

        requestQueue = Volley.newRequestQueue(this);

        submit_registration = findViewById(R.id.register_button);

        submit_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, SignUpChoice.class);
                startActivity(i);
                UUID uuid = UUID.randomUUID();



                User user = new User(f_name.getEditText().getText().toString(),phone
                        .getEditText().getText().toString(),email.getEditText().getText().toString(),
                        password.getEditText().getText().toString(), selectedOption);


                databaseRef.child("users").child(String.valueOf(uuid)).setValue(user);

                db.collection("users")
                        .add(user)
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
        });









    }
}