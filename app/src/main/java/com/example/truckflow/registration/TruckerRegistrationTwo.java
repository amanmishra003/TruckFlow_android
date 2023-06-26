package com.example.truckflow.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.truckflow.Home.Home;
import com.example.truckflow.R;

public class TruckerRegistrationTwo extends AppCompatActivity {


    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucker_registration_two);

        submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TruckerRegistrationTwo.this, Home.class);
                startActivity(i);
            }
        });
    }
}