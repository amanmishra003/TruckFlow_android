package com.douglas.truckflow.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.douglas.truckflow.R;
import com.google.android.material.textfield.TextInputLayout;

public class TruckerRegistration extends AppCompatActivity {

    Button next;

    TextInputLayout company_name, company_phone, dot, mc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucker_registration);

        next = findViewById(R.id.nextButton);
        company_name = findViewById(R.id.company_name);
        company_phone = findViewById(R.id.company_phne);
        dot = findViewById(R.id.dot);
        mc = findViewById(R.id.mc);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    // If all fields are valid, proceed to the next activity
                    String userEmail = getIntent().getStringExtra("USER_EMAIL");

                    Intent i = new Intent(TruckerRegistration.this, TruckerRegistrationTwo.class);
                    i.putExtra("companyName", company_name.getEditText().getText().toString());
                    i.putExtra("companyPhone", company_phone.getEditText().getText().toString());
                    i.putExtra("dotValue", dot.getEditText().getText().toString());
                    i.putExtra("mcValue", mc.getEditText().getText().toString());
                    i.putExtra("Email", userEmail);

                    Log.d("email in trucker", userEmail);

                    if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("IMAGE_FILE_NAME")) {
                        String fileName = getIntent().getStringExtra("IMAGE_FILE_NAME");
                        i.putExtra("IMAGE_FILE_NAME", fileName);
                        Log.d("fileName trucker", fileName);
                    }

                    startActivity(i);
                }
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;

        // Check if company name is filled
        String companyName = company_name.getEditText().getText().toString().trim();
        if (companyName.isEmpty()) {
            company_name.setError("Company name is required");
            isValid = false;
        } else {
            company_name.setError(null);
        }

        // Check if company phone is filled and has a valid format (optional)
        String companyPhone = company_phone.getEditText().getText().toString().trim();
        if (companyPhone.isEmpty()) {
            company_phone.setError("Company phone is required");
            isValid = false;
        } else if (!isValidPhoneNumber(companyPhone)) {
            company_phone.setError("Invalid phone number");
            isValid = false;
        } else {
            company_phone.setError(null);
        }

        // Check if DOT field is filled
        String mcValue = mc.getEditText().getText().toString().trim();
        if (mcValue.length() != 8) {
            mc.setError("MC number must be exactly 8 digits");
            isValid = false;
        } else {
            mc.setError(null);
        }

        // Validate DOT number (6 to 8 characters)
        String dotValue = dot.getEditText().getText().toString().trim();
        if (dotValue.length() < 6 || dotValue.length() > 8) {
            dot.setError("DOT number must be between 6 and 8 characters");
            isValid = false;
        } else {
            dot.setError(null);
        }

        return isValid;
    }

    // Simple phone number validation for demonstration purposes
    private boolean isValidPhoneNumber(@NonNull String phoneNumber) {
        return phoneNumber.matches("\\d{10}"); // Assumes 10-digit phone numbers
    }
}
