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

import com.example.truckflow.load.LoadActivityTwo;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
                Intent i = new Intent(Home.this, LoadActivityTwo.class);
                startActivity(i);

            }
        });
        //Navigation Drawer
        navigationDrawer();

        //RecyclerView function calls

        // Initialize the loadList with your load data

        recyclerView = findViewById(R.id.show_loads_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the shipper load data from Firestore or any other source
        getMyLoads(new FirestoreLoadCallback() {
            @Override
            public void onLoadsReceived(List<Load> loadData) {
                // Set the load data for the shipperLoadAdapter
                loadAdapter = new LoadAdapter(loadData);
                recyclerView.setAdapter(loadAdapter);
            }
        });



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
    private void getMyLoads(FirestoreLoadCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference loadsCollectionRef = db.collection("load");

        loadsCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Load> loadData = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Load load = new Load();
                        load.setLoadName(document.getString("loadName"));
                        load.setLoadDescription(document.getString("loadDescription"));
                        load.setLoadWeight(document.getString("loadWeight"));
                        load.setPickUpDate(document.getString("pickUpDate"));
                        load.setDeliveryDate(document.getString("deliveryDate"));
                        load.setContactInformation(document.getString("contactInformation"));
                        load.setRequirement(document.getString("requirement"));
                        loadData.add(load);
                    }
                }
                callback.onLoadsReceived(loadData);
            } else {
                // Handle errors here
                callback.onLoadsReceived(new ArrayList<>()); // or pass null to indicate an error
            }
        });
    }

    public interface FirestoreLoadCallback {
        void onLoadsReceived(List<Load> loadData);
    }
}