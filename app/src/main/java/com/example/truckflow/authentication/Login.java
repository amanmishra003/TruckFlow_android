package com.example.truckflow.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.truckflow.authentication.SignUp;
import com.example.truckflow.entities.User;
import com.example.truckflow.home.Home;
import com.example.truckflow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {
    Button callSignUp;
    Button callToHome;

    TextInputLayout emailInputLayout;
    TextInputLayout passwordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        emailInputLayout = findViewById(R.id.userName);
        passwordInputLayout = findViewById(R.id.password);

        // Navigate to Home
        callToHome = findViewById(R.id.button_login);
        callToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateEmail(emailInputLayout.getEditText().getText().toString()) &&
                        validatePassword(passwordInputLayout.getEditText().getText().toString())) {


                    checkEmailAndPasswordExist(emailInputLayout.getEditText().getText().toString(),
                            passwordInputLayout.getEditText().getText().toString());


                }





//                checkEmailAndPasswordExist(emailInputLayout.getEditText().getText().toString(),
//                        passwordInputLayout.getEditText().getText().toString());
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

}


///////////////////////


//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.truckflow.R;
//import com.example.truckflow.entities.User;
//import com.example.truckflow.home.Home;
//import com.google.android.material.textfield.TextInputLayout;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class Login extends AppCompatActivity {
//    Button callSignUp;
//    Button callToHome;
//
//    Button button_login;
//    TextInputLayout emailInputLayout;
//    TextInputLayout passwordInputLayout;
//    DatabaseReference usersRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_login);
//
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
//        usersRef = databaseRef.child("users"); // Assuming "users" is the table name in Firebase
//
//        emailInputLayout = findViewById(R.id.userName);
//        passwordInputLayout = findViewById(R.id.password);
//
//        callToHome = findViewById(R.id.button_login);
//        callToHome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (validateEmail() && validatePassword()) {
//                    String email = emailInputLayout.getEditText().getText().toString().trim();
//                    String password = passwordInputLayout.getEditText().getText().toString().trim();
//
//                    usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            boolean emailExists = false;
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                User user = snapshot.getValue(User.class);
//                                if (user != null && user.getPassword().equals(password)) {
//                                    emailExists = true;
//                                    break;
//                                }
//                            }
//                            if (emailExists) {
//                                // Email and password are correct, navigate to Home activity
//                                Intent intent = new Intent(Login.this, Home.class);
//                                startActivity(intent);
//                            } else {
//                                // Invalid email or password
//                                Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            // Handle database errors if any
//                            Toast.makeText(Login.this, "Database error occurred", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//
//            }
//        });
//
//        callSignUp = findViewById(R.id.button_gotoRegister);
//        callSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Login.this, SignUp.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private boolean validateEmail() {
//        String email = emailInputLayout.getEditText().getText().toString().trim();
//        if (TextUtils.isEmpty(email)) {
//            emailInputLayout.setError("Email is required");
//            return false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailInputLayout.setError("Please enter a valid email address");
//            return false;
//        } else {
//            emailInputLayout.setError(null);
//            return true;
//        }
//    }
//
//    private boolean validatePassword() {
//        String password = passwordInputLayout.getEditText().getText().toString().trim();
//        if (TextUtils.isEmpty(password)) {
//            passwordInputLayout.setError("Password is required");
//            return false;
//        } else if (password.length() < 6) {
//            passwordInputLayout.setError("Password must be at least 6 characters long");
//            return false;
//        } else {
//            passwordInputLayout.setError(null);
//            return true;
//        }
//    }
//}

