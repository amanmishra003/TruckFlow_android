package com.example.truckflow.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.truckflow.firebaseconfigurations.FirebaseStorageModelLoaderFactory;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import com.example.truckflow.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.InputStream;

public class UserProfile extends AppCompatActivity {

    private TextView fullNameTextView;
    private TextView emailTextView;
    private TextView phoneNoTextView;
    private TextView passwordTextView;
    private TextView permanentAddressTextView;

    private TextView full;

    private TextView add;

    private TextView email1;

    private Button update;

    private ImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        fullNameTextView = findViewById(R.id.fname);
        emailTextView = findViewById(R.id.email);
        phoneNoTextView = findViewById(R.id.phone);
        passwordTextView = findViewById(R.id.pwd);
        permanentAddressTextView = findViewById(R.id.add);
        full = findViewById(R.id.full_name);
        email1 = findViewById(R.id.email1);
        update = findViewById(R.id.updateBtn);
        add = findViewById(R.id.add);
        profilepic = findViewById(R.id.profilePicture);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("EMAIL_KEY")) {
            String email = getIntent().getStringExtra("EMAIL_KEY");

            // Use the email to construct the storage reference
            String fileName = "images/" + email ;
            StorageReference storageRef = FirebaseStorage.getInstance().getReference(fileName);

            Glide.get(this).getRegistry().append(StorageReference.class, InputStream.class, new FirebaseStorageModelLoaderFactory());

            // Use Glide to load the image into the ImageView
            Glide.with(this)
                    .load(storageRef)
                    .placeholder(R.drawable.logo) // Placeholder image while loading
                    .error(R.drawable.truck2) // Error image if loading fails
                    .into(profilepic);
        }




        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("EMAIL_KEY")) {
            String email = getIntent().getStringExtra("EMAIL_KEY");
            Log.d("UserProfile", "Email GOT IT from intent: " + email);
            emailTextView.setText(email);

            Query query = db.collection("users").whereEqualTo("email", email);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access the document data and set it to the corresponding TextViews
                        String fullName = document.getString("name");
                        String phoneNo = document.getString("phone");
                        String password = document.getString("password");
                        String role = document.getString("role");


                        fullNameTextView.setText(fullName);
                        phoneNoTextView.setText(phoneNo);
                        passwordTextView.setText(password);
                        add.setText(role);

                        full.setText(fullName);
                        email1.setText(email);

                        // Log the values
                        Log.d("UserProfile", "Full Name: " + fullName);
                        Log.d("UserProfile", "Phone No: " + phoneNo);
                        Log.d("UserProfile", "Password: " + password);
                        Log.d("UserProfile", "role: " + role);


                    }
                } else {
                    Log.d("UserProfile", "Error getting documents: ", task.getException());
                }
            });
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated values from the TextViews
                String fullName = fullNameTextView.getText().toString();
                String phoneNo = phoneNoTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                String role = add.getText().toString();
                

                // Update the database with the new values
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Query query = db.collection("users").whereEqualTo("email", emailTextView.getText().toString());
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Update the document with the new values
                            document.getReference().update(
                                    "name", fullName,
                                    "phone", phoneNo,
                                    "password", password,
                                    "role", role
                            );
                        }
                        // Show a toast message indicating the successful update
                        Toast.makeText(UserProfile.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("UserProfile", "Error getting documents: ", task.getException());
                    }
                });
            }
        });
    }
}