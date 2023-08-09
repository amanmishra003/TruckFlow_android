package com.douglas.truckflow.home;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.douglas.truckflow.R;
import com.douglas.truckflow.adapters.LoadAdapter;
import com.douglas.truckflow.adapters.TruckerAdapter;
import com.douglas.truckflow.authentication.Login;
import com.douglas.truckflow.booking.BookingRequests;
import com.douglas.truckflow.entities.Load;
import com.douglas.truckflow.entities.Trucker;
import com.douglas.truckflow.entities.User;
import com.douglas.truckflow.load.LoadActivityTwo;
import com.douglas.truckflow.profile.UserProfile;
import com.douglas.truckflow.utils.FireBaseUtils;
import com.douglas.truckflow.utils.MyNotificationHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // UI elements
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    ImageView postLoad;


    Spinner countrySpinner;
    private static final int NOTIFICATION_ID = 1;


    private RecyclerView recyclerView;

    // Adapters
    private LoadAdapter loadAdapter;
    private TruckerAdapter truckerAdapter;

    private SharedPreferences preferences;

    private List<Load> loadList;
    String regToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        FirebaseApp.initializeApp(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel for displaying notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = "Fcm notifications";
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // Initialize UI elements

        FirebaseMessaging meessagingIns = FirebaseMessaging.getInstance();

        meessagingIns.getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        regToken = token;
                        Log.d("FCM Token", "Token: " + token);
                        // You can store or use the token as needed
                    } else {
                        Log.e("FCM Token", "Failed to retrieve token: " + task.getException());
                    }
                });

        Log.d("FCM Token", "Token: " + regToken);


        //create notification channel
        MyNotificationHelper.createDefaultNotificationChannel(this);
        //subscribe
        FirebaseMessaging.getInstance().subscribeToTopic("PushNotifications");

        //Menu Hooks

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        postLoad = findViewById(R.id.postImg);


        User user = FireBaseUtils.getCurrentUserDetails(this);
        String role = user.getRole();

        countrySpinner = findViewById(R.id.countrySpinner);

        if ("trucker".equals(role)) {
            countrySpinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.countries,
                    android.R.layout.simple_spinner_item
            );
            countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countrySpinner.setAdapter(countryAdapter);

            countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedCountry = parent.getItemAtPosition(position).toString();
                    Log.d("selectedCountry", selectedCountry);

                    getMyL(new FirestoreLoadCallback() {
                        @Override
                        public void onLoadsReceived(List<Load> loadData) {

                            loadAdapter = new LoadAdapter(loadData);
                            recyclerView.setAdapter(loadAdapter); // Set loadAdapter for non-truckers
                        }
                    }, selectedCountry);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle the case where nothing is selected (optional)
                }

            });
        }
        else {
                countrySpinner.setVisibility(View.GONE);
            }
       // User user=FireBaseUtils.getCurrentUserDetails(this);
        // Handle post load/truck button click
        postLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, LoadActivityTwo.class);
                i.putExtra("shipperId",user.getEmail());
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

        role = user.getRole();
        /*if (extras != null) {
            role = extras.getString("role");
        }*/

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


                if (id == R.id.booking_Request) {

                    //Starting Booking Request acivit
                    Intent intent  = new Intent(Home.this, BookingRequests.class);
                    startActivity(intent);
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


    private void showNotification() {
        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.truck2)
                .setContentTitle("Notification Title")
                .setContentText("This is the notification content.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void getMyL(FirestoreLoadCallback callback, String selectedCountry) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference loadsCollectionRef = db.collection("load");

        loadsCollectionRef
                .whereEqualTo("provincePU", selectedCountry) // Add this line to filter by country
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Load> loadData = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Load load = new Load();

                                // testing shipper id from load
                                if (document.getString("shipperId") != null) {
                                    load.setShipperId(document.getString("shipperId"));
                                }

                                // Set the load ID in your Load object
                                load.setLoadId(document.getId());
                                load.setLoadName(document.getString("loadName"));
                                load.setLoadDescription(document.getString("loadDescription"));
                                load.setLoadWeight(document.getString("loadWeight"));
                                load.setLoadLength(document.getString("loadLength"));
                                load.setPickUpDate(document.getString("pickUpDate"));
                                load.setDeliveryDate(document.getString("deliveryDate"));
                                load.setTotalDistance(document.getString("totalDistance"));
                                load.setPickupAddress(document.getString("pickupAddress"));
                                load.setDeliveryAddress(document.getString("deliveryAddress"));
                                load.setExpectedPrice(document.getString("expectedPrice"));
                                load.setContactInformation(document.getString("contactInformation"));
                                load.setRequirement(document.getString("requirement"));
                                load.setLongitudePU(document.getString("longitudePU"));
                                load.setLatitudePU(document.getString("latitudePU"));
                                load.setLatitudeDel(document.getString("latitudeDel"));
                                load.setLongitudeDel(document.getString("longitudeDel"));
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


}

