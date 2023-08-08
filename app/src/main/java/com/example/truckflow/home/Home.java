package com.example.truckflow.home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckflow.R;
import com.example.truckflow.adpters.LoadAdapter;
import com.example.truckflow.adpters.TruckerAdapter;
import com.example.truckflow.entities.Load;
import com.example.truckflow.entities.Trucker;
import com.example.truckflow.load.LoadActivityTwo;
import com.example.truckflow.profile.UserProfile;
import com.example.truckflow.utils.MyNotificationHelper;
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
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    ImageView postLoad;


    private static final int NOTIFICATION_ID = 1;


    private RecyclerView recyclerView;
    private LoadAdapter loadAdapter;

    private TruckerAdapter truckerAdapter;

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


        postLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //condition if shipper then take to load post
                Intent i = new Intent(Home.this, LoadActivityTwo.class);
                startActivity(i);

                //else trucker then take to post truck

            }
        });
        //Navigation Drawer
        navigationDrawer();

        recyclerView = findViewById(R.id.show_loads_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle extras = getIntent().getExtras();

        String role = "";
        if (extras != null) {
            role = extras.getString("role");
        }

        if(role.equals("shipper")) {

            getMyTruckers(new FirestoreTruckerCallBack() {
                @Override
                public void onTruckerReceived(List<Trucker> loadData) {
                    truckerAdapter = new TruckerAdapter(loadData);
                    recyclerView.setAdapter(truckerAdapter); // Set truckerAdapter for truckers
                }
            });

        } else {

            getMyLoads(new FirestoreLoadCallback() {
                @Override
                public void onLoadsReceived(List<Load> loadData) {
                    loadAdapter = new LoadAdapter(loadData);
                    recyclerView.setAdapter(loadAdapter); // Set loadAdapter for non-truckers
                }
            });
        }

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

    private void getMyTruckers(FirestoreTruckerCallBack callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference loadsCollectionRef = db.collection("trucker");

        loadsCollectionRef.whereEqualTo("availability", true).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Trucker> truckerData = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        Trucker trucker = new Trucker();
                        trucker.setTruck_name(document.getString("truck_name"));
                        trucker.setDot(document.getString("dot"));
                        trucker.setMc(document.getString("mc"));
                        trucker.setCompany_name(document.getString("company_name"));
                        trucker.setMax_length(document.getString("max_length"));
                        trucker.setTruck_type(document.getString("truck_type"));
                        trucker.setMax_weight(document.getString("max_weight"));
                        trucker.setTruckerEmail(document.getString("truckerEmail"));
                        truckerData.add(trucker);
                    }
                }
                callback.onTruckerReceived(truckerData);
            } else {
                // Handle errors here
                callback.onTruckerReceived(new ArrayList<>()); // or pass null to indicate an error
            }
        });

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

                        //testing shipper id from load
                        if(document.getString("shipperId")!=null){
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

                        load.setLatitudeDel(document.getString("latitudeDel"));
                        load.setLongitudeDel(document.getString("longitudeDel"));

                        load.setLatitudePU(document.getString("latitudePU"));
                        load.setLongitudePU(document.getString("longitudePU"));
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

}