package com.example.truckflow.load;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.truckflow.home.Home;
import com.example.truckflow.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Calendar;

public class LoadActivity extends AppCompatActivity {

    Button postLoad, datePicker;
    TextInputEditText loadName, pickupLoc, dropOffLoc, loadDesc, loadWeight, loadDim, localDate, contactInfo, additionalReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        postLoad = findViewById(R.id.postLoadButton);
        datePicker = findViewById(R.id.loadDateTime);
        Places.initialize(getApplicationContext(), "AIzaSyBbOq9E_iUhKtg6WiaqV2CXUxIG2qo9wjQ");
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.dropOffLocationLayout);

        // Specify the types of place data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        // Handle place selection
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                String address = place.getAddress();

                // Handle the selected place
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle any errors that occur during autocomplete
            }
        });

        postLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoadActivity.this, Home.class);
                startActivity(i);
            }
        });


    }
    private void showDatePicker () {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the initial date to the current date
        DatePickerDialog datePickerDialog = new DatePickerDialog(LoadActivity.this,
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Do something with the selected date
                        String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        datePicker.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Show the date picker dialog
        datePickerDialog.show();
    }

}