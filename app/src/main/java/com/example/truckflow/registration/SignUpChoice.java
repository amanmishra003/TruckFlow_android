package com.example.truckflow.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truckflow.R;
import com.google.android.material.snackbar.Snackbar;

public class SignUpChoice extends AppCompatActivity {

    RadioButton radioButtonShipper;
    RadioButton radioButtonTrucker;
    Button buttonRegister;
    TextView warnText;

    String userProfile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_choice);


        radioButtonShipper = findViewById(R.id.radioButtonShipper);
        radioButtonTrucker = findViewById(R.id.radioButtonTrucker);
        buttonRegister = findViewById(R.id.buttonRegisterChoice);
        warnText = findViewById(R.id.warningText);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
              public void onClick(View v) {
                    // Check which radio button is selected
                    if (radioButtonShipper.isChecked()) {
                        // Shipper radio button is selected
                        userProfile = "Shipper";

                        warnText.setText("");
                        // Perform shipper registration logic
                    } else if (radioButtonTrucker.isChecked()) {
                        // Trucker radio button is selected
                        userProfile = "Trucker";
                        warnText.setText("");

                        // Perform trucker registration logic
                    } else {
                        // No radio button is selected
                         warnText.setText("Please select one of the choices");
                 //       Snackbar.make(v, "Please select one of the choices", Snackbar.LENGTH_LONG).show();

                    }

                    if(userProfile == "")
                    {

                        Intent i = new Intent(SignUpChoice.this, TruckerRegistration.class);
                        startActivity(i);
                    }
                    else if (userProfile =="Shipper")
                    {

                        Intent i = new Intent(SignUpChoice.this, TruckerRegistration.class);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(SignUpChoice.this, TruckerRegistration.class);
                        startActivity(i);
                    }
                }
            });
            }

}