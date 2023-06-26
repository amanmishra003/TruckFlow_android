package com.example.truckflow.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.truckflow.R;

public class TruckerRegistration extends AppCompatActivity {

    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucker_registration);

        next = findViewById(R.id.nextButton);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TruckerRegistration.this, TruckerRegistrationTwo.class);
                startActivity(i);
            }
        });
    }
}