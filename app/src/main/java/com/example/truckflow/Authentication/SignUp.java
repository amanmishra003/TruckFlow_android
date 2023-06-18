package com.example.truckflow.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.truckflow.Home.Home;
import com.example.truckflow.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SignUp extends AppCompatActivity {

    TextInputLayout phone,email,password,f_name;
    Button submit_registration;

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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








    }
}