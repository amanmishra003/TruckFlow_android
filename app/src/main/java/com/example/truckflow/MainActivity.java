package com.example.truckflow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.truckflow.authentication.Login;
import com.example.truckflow.home.Home;

public class MainActivity extends AppCompatActivity {


    private static final int SPLASH_SCREEN = 1000;
    private SharedPreferences preferences;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoggedIn()) {
                    startHomeActivity();
                } else {
                    startLoginActivity();
                }
            }

        }, SPLASH_SCREEN);
    }

    private boolean isLoggedIn() {
        return preferences.getBoolean("isLoggedIn", false);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(MainActivity.this, Home.class);
        startActivity(intent);
        finish();

    }
}

