package com.example.truckflow.authentication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.truckflow.R;
import com.example.truckflow.entities.User;
import com.example.truckflow.imageUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

public class SignUp extends AppCompatActivity {

    TextInputLayout phone,email,password,f_name;
    Button submit_registration;

    RequestQueue requestQueue;

    String role = "";
    String regToken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //instance initiated
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        FirebaseMessaging meessagingIns = FirebaseMessaging.getInstance();
        meessagingIns.getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        regToken = token;
                        Log.d("FCM Token", "Token: " + token);
                        // You can store or use the token as needed
                    } else {
                        Log.e("FCM Token", "Failed to retrieve token: " + task.getException());
                    }
                });

        Log.d("FCM Token", "Token: " + regToken);

        RadioButton radioButtonShipper;
        RadioButton radioButtonTrucker;

        f_name = findViewById(R.id.full_name);
        phone = findViewById(R.id.dot);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        radioButtonShipper = findViewById(R.id.radioButtonShipper);
        radioButtonTrucker = findViewById(R.id.radioButtonTrucker);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Perform actions based on the selected RadioButton
                if (checkedId == R.id.radioButtonShipper) {
                    // Option 1 is selected
                    // Add your code here
                    role = "shipper";
                } else if (checkedId == R.id.radioButtonTrucker) {
                    // Option 2 is selected
                    // Add your code here
                    role = "trucker";
                }
                // Add more conditions for other RadioButtons as needed
            }
        });





        String url = getString(R.string.reg_api_url);

        requestQueue = Volley.newRequestQueue(this);

        submit_registration = findViewById(R.id.register_button);

        submit_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // If all fields are valid, proceed with the registration
                    String userEmail = email.getEditText().getText().toString().toLowerCase();
                    String userName = f_name.getEditText().getText().toString();
                    String userPhone = phone.getEditText().getText().toString();
                    String userPassword = password.getEditText().getText().toString();

                    // Create a new User object
                    User user = new User(userName, userEmail, userPhone, userPassword, role,false,regToken);

                    // Store the user object in the database
                    UUID uuid = UUID.randomUUID();
                    databaseRef.child("users").child(String.valueOf(uuid)).setValue(user);

                    // Alternatively, you can also store the user object in Firestore
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

                    // Start the next activity
                    Intent i = new Intent(SignUp.this, imageUser.class);
                    i.putExtra("USER_EMAIL", userEmail);
                    i.putExtra("Role", role);
                    startActivity(i);
                }
            }
        });

    }

    private void sendRegistrationTokenToServer(String registrationToken) {

    }

    private boolean validateInput() {
        boolean isValid = true;

        // Validate full name
        String fullName = f_name.getEditText().getText().toString().trim();
        if (fullName.isEmpty()) {
            f_name.setError("Full name is required");
            isValid = false;
        } else {
            f_name.setError(null);
        }

        // Validate email
        String userEmail = email.getEditText().getText().toString().trim();
        if (userEmail.isEmpty()) {
            email.setError("Email is required");
            isValid = false;
        } else if (!isValidEmail(userEmail)) {
            email.setError("Invalid email address");
            isValid = false;
        } else {
            email.setError(null);
        }

        // Validate phone number (optional)
        String userPhone = phone.getEditText().getText().toString().trim();
        if (!userPhone.isEmpty() && !isValidPhoneNumber(userPhone)) {
            phone.setError("Invalid phone number");
            isValid = false;
        } else {
            phone.setError(null);
        }

        // Validate password (optional, you may have specific password requirements)
        String userPassword = password.getEditText().getText().toString().trim();
        if (userPassword.isEmpty()) {
            password.setError("Password is required");
            isValid = false;
        } else {
            password.setError(null);
        }

        // Validate role selection
        if (role.isEmpty()) {
            // You may also show an error message using a Toast or Snackbar
            isValid = false;
        }

        return isValid;
    }

    // Simple email validation for demonstration purposes
    private boolean isValidEmail(@NonNull String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Simple phone number validation for demonstration purposes
    private boolean isValidPhoneNumber(@NonNull String phoneNumber) {
        return phoneNumber.matches("\\d{10}"); // Assumes 10-digit phone numbers
    }
}













