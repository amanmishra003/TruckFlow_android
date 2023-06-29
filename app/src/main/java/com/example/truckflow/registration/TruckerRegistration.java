package com.example.truckflow.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.truckflow.R;
import com.google.android.material.textfield.TextInputLayout;

public class TruckerRegistration extends AppCompatActivity {

    Button next;

    TextInputLayout company_name, company_phone, dot, mc;

//    TextView company_name;
//    TextView company_phone;
//    TextView dot;
//    TextView mc;

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
                String companyName = company_name.getEditText().getText().toString();
                String companyPhone = company_phone.getEditText().getText().toString();
                String dotValue = dot.getEditText().getText().toString();
                String mcValue = mc.getEditText().getText().toString();

                Intent i = new Intent(TruckerRegistration.this, TruckerRegistrationT.class);
                i.putExtra("companyName", companyName);
                i.putExtra("companyPhone", companyPhone);
                i.putExtra("dotValue", dotValue);
                i.putExtra("mcValue", mcValue);
                startActivity(i);
            }
        });
    }
}
