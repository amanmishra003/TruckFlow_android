package com.douglas.truckflow.authentication;


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

import androidx.appcompat.app.AppCompatActivity;

import com.douglas.truckflow.R;
import com.douglas.truckflow.entities.User;
import com.douglas.truckflow.home.Home;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    Button callSignUp;
    Button callToHome;
    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;

    ImageView image;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

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
                if (validateEmailAndPassword()) {
                    String email = emailInputLayout.getEditText().getText().toString().toLowerCase();
                    String password = passwordInputLayout.getEditText().getText().toString();

                    checkEmailAndPasswordExist(email, password);
                }
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

    private boolean validatePassword() {
        String password = passwordInputLayout.getEditText().getText().toString();
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

    private boolean validateEmail() {
        String email = emailInputLayout.getEditText().getText().toString();

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

    private boolean validateEmailAndPassword() {
        return validateEmail() && validatePassword();}

    private void checkEmailAndPasswordExist(String email, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        Query query = usersRef.whereEqualTo("email", email);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        User user = document.toObject(User.class);
                        if (user.getPassword().equals(password)) {
                            saveUserToPrefs(user);

                            // Set isLoggedIn to true
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("email", email);
                            editor.apply();

                            String role = user.getRole();
                            Log.d(TAG, "Role: " + role);
                            editor.putString("role",role);
                            if ("trucker".equals(role) || "shipper".equals(role)) {
                                Intent intent = new Intent(Login.this, Home.class);
                                intent.putExtra("EMAIL_KEY", email);
                                intent.putExtra("role", role);
                                startActivity(intent);
                                finish();
                            } else {
                                // Handle other roles
                            }
                            return;
                        }
                    }
                    Log.d(TAG, "Password does not match");
                    Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Email does not exist");
                    Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "Error checking email existence");
                Toast.makeText(Login.this, "Database error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserToPrefs(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        String userJson = new Gson().toJson(user);
        editor.putString("user", userJson);
        editor.apply();
    }
}
