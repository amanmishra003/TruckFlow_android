package com.example.truckflow.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.truckflow.R;
import com.example.truckflow.load.LoadActivity;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;

    ImageView postLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);


        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        postLoad = findViewById(R.id.postImg);

        postLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, LoadActivity.class);
                startActivity(i);

            }
        });
        //Navigation Drawer
        navigationDrawer();

        //RecyclerView function calls

    }


    //nav drawer
    private void navigationDrawer() {
        navigationView.bringToFront(); //to interact with nav bar
        navigationView.setNavigationItemSelectedListener(this);//to click the items
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    else{
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                }
            });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
    //closing navigation drawer first not app on back press
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }
}