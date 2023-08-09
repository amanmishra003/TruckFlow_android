package com.example.truckflow.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckflow.R;
import com.example.truckflow.adapters.BookingRequestAdapter;
import com.example.truckflow.communication.ChatActivity;
import com.example.truckflow.entities.BookingRequest;
import com.example.truckflow.entities.User;
import com.example.truckflow.utils.FireBaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingRequests extends AppCompatActivity {

    private static final String TAG = "BookingRequests";

    private FirebaseFirestore db;
    private List<BookingRequest> bookingRequests;
    private BookingRequestAdapter adapter;

    DatabaseReference databaseRef;
    private User userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);

        // Initialize Firebase Firestore
        databaseRef = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingRequests = new ArrayList<>();
        adapter = new BookingRequestAdapter(bookingRequests, new BookingRequestAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(BookingRequest bookingRequest) {
                UUID uuid = UUID.randomUUID();
                //if user press on accept,then we will send the confirmation to the trucker
                databaseRef.child("bookings").child(String.valueOf(uuid)).setValue(bookingRequest)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "booking data saved to Firebase Realtime Database");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error saving booking data to Firebase Realtime Database", e);
                            }
                        });

                db.collection("bookings")
                        .add(bookingRequest)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "Trucker data saved to Firestore with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error saving trucker data to Firestore", e);
                            }
                        });

                FireBaseUtils.getUserDetails(bookingRequest.getTruckerEmail(), new FireBaseUtils.FirestoreUserCallback() {
                    @Override
                    public void onUserReceived(User user) {
                        if (user != null) {
                            userDetail = user;
                            String token =  userDetail.token;
                            FireBaseUtils.sendNotification("TruckFlow","Your booking requeest is accepted",token,BookingRequests.this);

                        } else {
                            // Handle the case where trucker details were not found
                        }
                    }
                });
                Toast.makeText(BookingRequests.this, "Accepted: " + bookingRequest.getLoadName(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRejectClick(BookingRequest bookingRequest) {
                // Handle reject button click
                Toast.makeText(BookingRequests.this, "Rejected: " + bookingRequest.getLoadName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChatClick(BookingRequest bookingRequest) {
                // Handle chat button click
                String recipientId = bookingRequest.getTruckerEmail();
                Intent i = new Intent(BookingRequests.this, ChatActivity.class);
                i.putExtra("receiverId",recipientId);
                startActivity(i);
                Toast.makeText(BookingRequests.this, "Chat: " + bookingRequest.getTruckerEmail(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        // Fetch booking requests from Firebase Firestore
        db.collection("bookingRequest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                BookingRequest bookingRequest = document.toObject(BookingRequest.class);
                                if (bookingRequest != null) {
                                    bookingRequests.add(bookingRequest);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
