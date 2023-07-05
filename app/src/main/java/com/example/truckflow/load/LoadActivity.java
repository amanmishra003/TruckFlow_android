package com.example.truckflow.load;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.truckflow.entities.Load;
import com.example.truckflow.home.Home;
import com.example.truckflow.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

public class LoadActivity extends AppCompatActivity {

    Button postLoad, loadDatePicker,dropDatePicker ;

    TextInputLayout loadName, loadWeight, loadDim, contactInfo, additionalReq, loadDesc;

    TextInputEditText dropDateTimeLayout, loadDateTimeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load);

        //instance initiated
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        loadName = findViewById(R.id.loadName);
        loadDesc = findViewById(R.id.loadDescriptionLayout);
        loadWeight = findViewById(R.id.loadWeightLayout);
        loadDim = findViewById(R.id.loadDimensionsLayout);
        contactInfo = findViewById(R.id.contactInfoLayout);
        additionalReq = findViewById(R.id.additionalRequirementsLayout);





        postLoad = findViewById(R.id.postLoadButton);
        loadDatePicker = findViewById(R.id.loadDateTime);
        dropDatePicker = findViewById(R.id.dropDateTime);




        postLoad.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {



                                        }

                                    });


        Places.initialize(getApplicationContext(), "AIzaSyBbOq9E_iUhKtg6WiaqV2CXUxIG2qo9wjQ");
        loadDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadDatePicker();
            }
        });

        dropDatePicker = findViewById(R.id.dropDateTime);
        Places.initialize(getApplicationContext(), "AIzaSyBbOq9E_iUhKtg6WiaqV2CXUxIG2qo9wjQ");
        dropDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropDatePicker();
            }
        });

        AutocompleteSupportFragment dropOffLocationFrag = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.dropOffLocationLayout);

        // Specify the types of place data to return
        dropOffLocationFrag.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        // Handle place selection
        dropOffLocationFrag.setOnPlaceSelectedListener(new PlaceSelectionListener() {
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

        AutocompleteSupportFragment pickUpLocFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.pickupLocationLayout);

        // Specify the types of place data to return
        pickUpLocFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        // Handle place selection
        pickUpLocFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
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

                UUID uuid = UUID.randomUUID();

                String pickDate = showLoadDatePicker();
                String deliveryDate = showDropDatePicker();
                Load load = new Load(loadName.getEditText().getText().toString(),
                        loadDesc.getEditText().getText().toString(), loadWeight.getEditText().getText().toString()
                        , loadDim.getEditText().getText().toString(), pickDate, deliveryDate, contactInfo.getEditText().getText().toString(),
                        additionalReq.getEditText().getText().toString());

                load.toString();

                databaseRef.child("load").child(String.valueOf(uuid)).setValue(load);
                db.collection("load")
                        .add(load)
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
                Intent i = new Intent(LoadActivity.this, Home.class);
                startActivity(i);


            }
        });


    }
    private String showLoadDatePicker () {
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
                        loadDatePicker.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Show the date picker dialog
        datePickerDialog.show();
        return null;
    }
    private String showDropDatePicker () {
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
                        dropDatePicker.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);

        // Show the date picker dialog
        datePickerDialog.show();

        return null;

    }

}