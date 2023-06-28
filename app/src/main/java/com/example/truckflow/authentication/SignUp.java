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

        f_name = findViewById(R.id.full_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);


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
                        .getEditText().getText().toString(),email.getEditText().getText().toString(),password.getEditText().getText().toString());
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


        /*submit_registration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String postUrl = url + "/postUser";

                Toast.makeText(getApplicationContext(), "Changes for message", Toast.LENGTH_SHORT).show();
                new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent intent = new Intent(SignUp.this, Home.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle the error
                                System.out.println(error.getMessage());
                            }
                        }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            String pass = password.toString();
                            String name = f_name.toString();
                            String phonenumber = phone.toString();
                            String emailStr = email.toString();

                            String requestBody = "pass=" + URLEncoder.encode(pass, "UTF-8") +
                                    "&name=" + URLEncoder.encode(name, "UTF-8") +
                                    "&phone=" + URLEncoder.encode(phonenumber, "UTF-8") +
                                    "&email=" + URLEncoder.encode(emailStr, "UTF-8");

                            return requestBody.getBytes("UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            System.out.println(e.getMessage());
                            return null;
                        }
                    }
                };

            }
        });
*/







    }
}