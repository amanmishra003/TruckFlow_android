package com.example.truckflow.load;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.truckflow.R;
import com.google.android.gms.maps.MapView;

public class LoadActivity extends AppCompatActivity {

    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

    }
}