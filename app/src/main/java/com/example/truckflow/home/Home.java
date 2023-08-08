package com.example.truckflow.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.truckflow.R;
import com.example.truckflow.adapters.LoadAdapter;
import com.example.truckflow.adapters.TruckerAdapter;
import com.example.truckflow.authentication.Login;
import com.example.truckflow.entities.Load;
import com.example.truckflow.entities.Trucker;
import com.example.truckflow.load.LoadActivityTwo;
import com.example.truckflow.profile.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // UI elements
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    ImageView postLoad;
    private RecyclerView recyclerView;

    // Adapters
    private LoadAdapter loadAdapter;
    private TruckerAdapter truckerAdapter;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        // Initialize UI elements
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        postLoad = findViewById(R.id.postImg);

        // Handle post load/truck button click
        postLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, LoadActivityTwo.class);
                startActivity(i);
            }
        });

        // Set up navigation drawer
        navigationDrawer();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.show_loads_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get user role from intent extras
        Bundle extras = getIntent().getExtras();
        String role = "";
        if (extras != null) {
            role = extras.getString("role");
        }

        // Display appropriate data based on user role
        if (role.equals("shipper")) {
            // Load trucker data if the user is a shipper
            getMyTruckers(new FirestoreTruckerCallBack() {
                @Override
                public void onTruckerReceived(List<Trucker> loadData) {
                    truckerAdapter = new TruckerAdapter(loadData);
                    recyclerView.setAdapter(truckerAdapter);
                }
            });
        } else {
            // Load load data if the user is not a shipper
            getMyLoads(new FirestoreLoadCallback() {
                @Override
                public void onLoadsReceived(List<Load> loadData) {
                    loadAdapter = new LoadAdapter(loadData);
                    recyclerView.setAdapter(loadAdapter);
                }
            });
        }
    }

    // Set up navigation drawer
    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    // Handle Profile item click
                    Intent intent = new Intent(Home.this, UserProfile.class);

                    if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("EMAIL_KEY")) {
                        String email = getIntent().getStringExtra("EMAIL_KEY");
                        intent.putExtra("EMAIL_KEY", email);
                    }

                    startActivity(intent);
                }


                if (id == R.id.nav_logout) {
                    // Clear shared preferences
                    preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear(); // or editor.remove("user"); if you want to keep other preferences
                    editor.apply();

                    // Start Login activity
                    Intent intent = new Intent(Home.this, Login.class);
                    startActivity(intent);
                    finish();
                    return true;
                }


                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Retrieve trucker data
    private void getMyTruckers(FirestoreTruckerCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference loadsCollectionRef = db.collection("trucker");

        loadsCollectionRef.whereEqualTo("availability", true).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Trucker> truckerData = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Trucker trucker = document.toObject(Trucker.class);
                        truckerData.add(trucker);
                    }
                }
                callback.onTruckerReceived(truckerData);
            } else {
                callback.onTruckerReceived(new ArrayList<>());
            }
        });
    }

    // Retrieve load data
    private void getMyLoads(FirestoreLoadCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference loadsCollectionRef = db.collection("load");

        loadsCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Load> loadData = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Load load = document.toObject(Load.class);
                        loadData.add(load);
                    }
                }
                callback.onLoadsReceived(loadData);
            } else {
                callback.onLoadsReceived(new ArrayList<>());
            }
        });
    }

    public interface FirestoreLoadCallback {
        void onLoadsReceived(List<Load> loadData);
    }

    public interface FirestoreTruckerCallBack {
        void onTruckerReceived(List<Trucker> loadData);
    }
}
