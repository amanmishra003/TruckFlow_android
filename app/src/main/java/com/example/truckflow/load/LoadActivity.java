package com.example.truckflow.load;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.truckflow.home.Home;
import com.example.truckflow.R;

public class LoadActivity extends AppCompatActivity {

    Button postLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        postLoad = findViewById(R.id.postLoadButton);
        postLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoadActivity.this, Home.class);
                startActivity(i);
            }
        });

    }
}