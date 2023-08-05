package com.example.truckflow.communication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truckflow.R;
import com.example.truckflow.entities.ChatMessage;
import com.example.truckflow.entities.ChatRoom;
import com.example.truckflow.entities.User;
import com.example.truckflow.utils.FireBaseUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private TextView username;
    private ImageButton backButton;
    private ImageButton sendMessage;
    private EditText messageText;

    private String loadId;
    private String senderId;
    private String receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize views
        username = findViewById(R.id.username);
        backButton = findViewById(R.id.chat_back_btn);
        sendMessage = findViewById(R.id.send_btn);
        messageText = findViewById(R.id.message_et);


        //fetch sender's id
        senderId = fetchSenderDetails();
        // Get receiver's email from intent
        Intent intent = getIntent();
        receiverId = intent.getStringExtra("shipperId");  //Ids are emails here
        loadId   = intent.getStringExtra("loadId");

        // Fetch receiver's details and set the username
        fetchReceiverDetails(receiverId);

        // Set click listener for the send message button
        sendMessage.setOnClickListener(view -> {
            String message = messageText.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessageToChatRoom(loadId, senderId, receiverId, message);
                // Clear the message input field
                messageText.setText("");
            }
        });

        // Set click listener for the back button
        backButton.setOnClickListener(view -> {
            // Handle back button action (e.g., finish the activity)
            finish();
        });
    }

    private String fetchSenderDetails() {

        User currentUser = FireBaseUtils.getCurrentUserDetails(this);

        if (currentUser != null) {
            // Now you can access current user details like email, name, role, shipperId, etc.
            String userEmail = currentUser.getEmail();
            return userEmail;
            // Use the current user details as needed
        } else {
            // Current user details not available or invalid
            // Handle the case gracefully
            Log.d(TAG, "fetchSenderDetails: current user can not found");
            return null;
        }
    }

    private void fetchReceiverDetails(String receiverEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("users").whereEqualTo("email", receiverEmail);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the receiver's name
                    String receiverName = document.getString("name");
                    receiverId = document.getId();  // Set the receiver's ID


                    // Update UI elements on the main thread
                    runOnUiThread(() -> {
                        // Set the receiver's name to the username TextView
                        username.setText(receiverName);
                    });


                    // Log the receiver's name
                    Log.d("ReceiverName", "Receiver Name: " + receiverName);

                }
            } else {
                Log.d("Firestore", "Error getting documents: " + task.getException());
            }
        });
    }

    private void sendMessageToChatRoom(String loadId, String senderId, String receiverId, String messageText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long timeStamp = System.currentTimeMillis();

        // Document reference for the chat room
        DocumentReference chatRoomRef = db.collection("load").document(loadId)
                .collection("chatrooms").document(senderId + "_" + receiverId);

        // Collection reference for chat messages within the chat room
        CollectionReference chatMessagesRef = chatRoomRef.collection("chatMessages");

        // Check if chat room document exists
        chatRoomRef.get().addOnCompleteListener(chatRoomTask -> {
            if (chatRoomTask.isSuccessful() && chatRoomTask.getResult().exists()) {
                // Chat room exists, add the message to chat messages collection
                ChatMessage newMessage = new ChatMessage(senderId, receiverId, messageText, timeStamp);
                chatMessagesRef.add(newMessage)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Message added to chat messages collection");
                            Toast.makeText(this, "chat added!", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.d(TAG, "Error adding message: " + e.getMessage());
                        });
            } else {
                // Chat room doesn't exist, create it and add the message
                createOrSendMessage(loadId, senderId, receiverId, messageText);
            }
        });
    }

    private void createOrSendMessage(String loadId, String senderId, String receiverId, String initialMessage) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long timeStamp = System.currentTimeMillis();

        // Document reference for the chat room
        DocumentReference chatRoomRef = db.collection("load").document(loadId)
                .collection("chatrooms").document(senderId + "_" + receiverId);

        // Collection reference for chat messages within the chat room
        CollectionReference chatMessagesRef = chatRoomRef.collection("chatMessages");

        // Check if chat room document exists
        chatRoomRef.get().addOnCompleteListener(chatRoomTask -> {
            if (!chatRoomTask.isSuccessful() || !chatRoomTask.getResult().exists()) {
                // Chat room doesn't exist, create it
                ChatRoom chatRoom = new ChatRoom(senderId, receiverId, timeStamp, chatRoomRef.getId());
                chatRoomRef.set(chatRoom)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Chat room created successfully");

                            // Add the initial message to the chat messages collection
                            ChatMessage firstMessage = new ChatMessage(senderId, receiverId, initialMessage, timeStamp);
                            chatMessagesRef.add(firstMessage)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d(TAG, "Initial message added to chat messages collection");
                                        Toast.makeText(this, "chat added!", Toast.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.d(TAG, "Error adding initial message: " + e.getMessage());
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Log.d(TAG, "Error creating chat room: " + e.getMessage());
                        });
            } else {
                // Chat room exists, directly add the message to chat messages collection
                ChatMessage newMessage = new ChatMessage(senderId, receiverId, initialMessage, timeStamp);
                chatMessagesRef.add(newMessage)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Message added to chat messages collection");
                            Toast.makeText(this, "chat added!", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.d(TAG, "Error adding message: " + e.getMessage());
                        });
            }
        });
    }
}
