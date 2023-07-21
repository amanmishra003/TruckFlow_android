package com.example.truckflow.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.truckflow.R;
import com.example.truckflow.adpters.LoadAdapter;
import com.example.truckflow.entities.Load;
import com.example.truckflow.load.LoadActivity;

import com.example.truckflow.profile.UserProfile;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    ImageView postLoad;

    private RecyclerView recyclerView;
    private LoadAdapter loadAdapter;

    private List<Load> loadList;

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

        // Initialize the loadList with your load data

        recyclerView = findViewById(R.id.show_loads_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadAdapter = new LoadAdapter(loadList);
        // Get the shipper load data from Firestore or any other source
        List<Load> MyLoadList = getMyLoads();

        // Set the load data for the shipperLoadAdapter
        loadAdapter.updateLoads(MyLoadList);
        recyclerView.setAdapter(loadAdapter);



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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    // Handle Home item click here
                    Intent intent = new Intent(Home.this, UserProfile.class);

                    if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("EMAIL_KEY")) {
                        String email = getIntent().getStringExtra("EMAIL_KEY");
                        Log.d("UserProfile", "Email GOT IT HOME from intent: " + email);

                        intent.putExtra("EMAIL_KEY", email);


                    }



                    startActivity(intent);
                }

                // Close the drawer after handling the click event
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
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


    //dummy loads list for now
    private List<Load> getMyLoads() {
        List<Load> loadList = new ArrayList<>();

        // Load 1
            Load load1 = new Load();
            load1.setLoadDescription("Fragile Items");
            load1.setLoadWeight("200kg");
            load1.setLoadDimensions("200*200*200");
            load1.setPickDate("12-12-12");
            load1.setDeliverDate("32.21.12");
            load1.setContactInformation("James");
            load1.setRequirement("Plastic");



        Load load2 = new Load();
        load2.setLoadDescription("Fragile Items");
        load2.setLoadWeight("200kg");
        load2.setLoadDimensions("200*200*200");
        load2.setPickDate("12-12-12");
        load2.setDeliverDate("32.21.12");
        load2.setContactInformation("James");
        load2.setRequirement("Plastic");

// Add the loads to the list


        loadList.add(load1);
        loadList.add(load2);


        return loadList;
    }

}