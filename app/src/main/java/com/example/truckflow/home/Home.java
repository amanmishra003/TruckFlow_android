package com.example.truckflow.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.truckflow.R;
import com.example.truckflow.adpters.LoadAdapter;
import com.example.truckflow.entities.Load;
import com.example.truckflow.load.LoadActivity;
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
        load1.setId("1");
        load1.setAdditionalReq("Handle with care");
        load1.setContactInfo("John Doe");
        load1.setCurrentLoc("654 Oak Ave, Surrey, BC");
        load1.setDropLoc("321 Maple Road, Surrey, BC");
        load1.setEstArrivalTime(new Date());
        load1.setLoadDesc("Fragile items");
        load1.setLoadDims("20x20x10");
        load1.setLoadName("Online Fright Services, Inc.");
        load1.setLoadWeight("100 lbs");
        load1.setPickupLoc("987 Elm Street, Surrey, BC");
        load1.setDistance(50.0f);
        load1.setPickUpDateTime(new Date());
        load1.setDropDateTime(new Date());
        load1.setTruckerId("123456");
        load1.setShipperId("789012");

        // Load 2
        Load load2 = new Load();
        load2.setId("2");
        load2.setAdditionalReq("Handle with caution");
        load2.setContactInfo("Jane Smith");
        load2.setCurrentLoc("City X");
        load2.setDropLoc("City Y");
        load2.setEstArrivalTime(new Date());
        load2.setLoadDesc("Heavy machinery");
        load2.setLoadDims("10x10x10");
        load2.setLoadName("Sureway Transportation Company");
        load2.setLoadWeight("500 lbs");
        load2.setPickupLoc("City X");
        load2.setDistance(100.0f);
        load2.setPickUpDateTime(new Date());
        load2.setDropDateTime(new Date());
        load2.setTruckerId("987654");
        load2.setShipperId("210987");
// Add the loads to the list
        loadList.add(load1);
        loadList.add(load2);


        return loadList;
    }

}