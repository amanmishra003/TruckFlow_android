package com.example.truckflow.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.truckflow.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckflow.adapters.BookingRequestAdapter;
import com.example.truckflow.entities.BookingRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingRequests extends AppCompatActivity {

    private static final String TAG = "BookingRequests";

    private FirebaseFirestore db;
    private List<BookingRequest> bookingRequests;
    private BookingRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingRequests = new ArrayList<>();
        adapter = new BookingRequestAdapter(bookingRequests, new BookingRequestAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(BookingRequest bookingRequest) {
                // Handle accept button click
                // You can use the bookingRequest object to get the details of the clicked request
                // For example, bookingRequest.getLoadName() or bookingRequest.getPickupAddress()
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
