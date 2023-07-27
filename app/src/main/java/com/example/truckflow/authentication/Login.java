package com.example.truckflow.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.truckflow.entities.User;
import com.example.truckflow.home.Home;
import com.example.truckflow.R;
import com.example.truckflow.profile.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.truckflow.home.HomeShipper;

public class Login extends AppCompatActivity {
    Button callSignUp;
    Button callToHome;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;

    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        emailInputLayout = findViewById(R.id.userName);
        passwordInputLayout = findViewById(R.id.password);
        image = findViewById(R.id.logoImg);



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Home.class);
                startActivity(i);
            }
        });
        // Navigate to Home
        callToHome = findViewById(R.id.button_login);
        callToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference usersCollectionRef = db.collection("users");

                String collectionName = "users"; // Replace with your collection name
                String email = emailInputLayout.getEditText().getText().toString(); // Replace with your document ID

                db.collection(collectionName)
                        .document(email)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                // Document found, you can access its data
                                // For example, if you have a 'name' field in your document:
                                String role = documentSnapshot.getString("role");
                                Log.d("FirebaseData", "role: " + role);
                            } else {
                                // Document not found
                                Log.d("FirebaseData", "Document not found");
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Handle any errors that occurred while retrieving the document
                            Log.e("FirebaseData", "Error getting document: " + e);
                        });

                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("email", emailInputLayout.getEditText().getText().toString());
                editor.apply();

                if (validateEmail(emailInputLayout.getEditText().getText().toString()) && validatePassword(passwordInputLayout.getEditText().getText().toString())) {
                    checkEmailAndPasswordExist(emailInputLayout.getEditText().getText().toString(), passwordInputLayout.getEditText().getText().toString());
                }
                checkRolesByEmail(emailInputLayout.getEditText().getText().toString(),passwordInputLayout.getEditText().getText().toString());

            }
        });
        // Navigate to Registration
        callSignUp = findViewById(R.id.button_gotoRegister);
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("Password is required");
            return false;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("Password must be at least 6 characters long");
            return false;
        } else {
            passwordInputLayout.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String email) {

        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("Email is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Please enter a valid email address");
            return false;
        } else {
            emailInputLayout.setError(null);
            return true;
        }
    }

    private void checkEmailAndPasswordExist(String email, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        Query query = usersRef.whereEqualTo("email", email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Email exists, check for password match
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            User user = document.toObject(User.class);
                            if (user.getPassword().equals(password)) {
                                // Email and password match
                                Log.d("Login", "Email and password exist");
                                // Proceed with login or show toast message
                                Intent intent = new Intent(Login.this, Home.class);


                                // Pass the email as an extra to the intent
                                intent.putExtra("EMAIL_KEY", emailInputLayout.getEditText().getText().toString());

                                // Start the UserProfile activity with the intent
                                startActivity(intent);


                                return;
                            }
                        }
                        // Password doesn't match
                        Log.d("Login", "Password does not match");
                        Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email does not exist
                        Log.d("Login", "Email does not exist");
                        Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error occurred while checking email existence
                    Log.d("Login", "Error checking email existence");
                    Toast.makeText(Login.this, "Database error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkRolesByEmail(String email, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        Query query = usersRef.whereEqualTo("email", email);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    // Email exists, check for password match
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        User user = document.toObject(User.class);
                        if (user.getPassword().equals(password)) {
                            // Email and password match, retrieve the user's role
                            String role = user.getRole();
                            Log.d("Login", "Role: " + role);

                            // Now you have the role, you can use it for further logic
                            // For example, you can navigate to different activities based on the role
                            if ("trucker".equals(role)) {
                                // User has a trucker role, navigate to trucker activity
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.putExtra("EMAIL_KEY", email);
                                intent.putExtra("role", role);
                                startActivity(intent);
                            } else if ("shipper".equals(role)) {
                                // User has a shipper role, navigate to shipper activity
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.putExtra("EMAIL_KEY", email);
                                intent.putExtra("role", role);
                                startActivity(intent);
                            }
                            // Add more cases for other roles if needed

                            return;
                        }
                    }
                    // Password doesn't match
                    Log.d("Login", "Password does not match");
                    Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                } else {
                    // Email does not exist
                    Log.d("Login", "Email does not exist");
                    Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error occurred while checking email existence
                Log.d("Login", "Error checking email existence");
                Toast.makeText(Login.this, "Database error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

