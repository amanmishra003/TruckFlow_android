package com.douglas.truckflow.communication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.douglas.truckflow.R;
import com.douglas.truckflow.adapters.ChatAdapter;
import com.douglas.truckflow.entities.ChatMessage;
import com.douglas.truckflow.entities.ChatRoom;
import com.douglas.truckflow.entities.User;
import com.douglas.truckflow.utils.FireBaseUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private String chatRoomId;
    private TextView username;
    private ImageButton backButton;
    private ImageButton sendMessage;
    private EditText messageText;

    private String loadId;
    private String senderId;
    private String receiverId;

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize views
        username = findViewById(R.id.username);
        backButton = findViewById(R.id.chat_back_btn);
        sendMessage = findViewById(R.id.send_btn);
        messageText = findViewById(R.id.message_et);

        // Fetch sender's id from shared preff
        senderId = fetchSenderDetails().toLowerCase();

        // Initialize RecyclerView and its layout manager
        recyclerView = findViewById(R.id.chats_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Create an empty list to hold chat messages
        chatMessages = new ArrayList<>();

        // Create a new ChatAdapter with the sender's ID and set it to the RecyclerView


        // Get receiver's email from intent
        Intent intent = getIntent();
        receiverId = intent.getStringExtra("receiverId").toLowerCase(); //Ids are emails here
        //loadId = intent.getStringExtra("loadId");

        Log.i("recieverID",receiverId);
        // Fetch receiver's details and set the username
        fetchReceiverDetails(receiverId);

        // Set click listener for the send message button
        sendMessage.setOnClickListener(view -> {
            String message = messageText.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessageToChatRoom( senderId, receiverId, message);
                // Clear the message input field
                messageText.setText("");
            }
        });

        // Fetch and display chat messages
        fetchChatMessages();

        // Set click listener for the back button
        backButton.setOnClickListener(view -> {
            // Handle back button action (e.g., finish the activity)
            finish();
        });
    }

    private void fetchChatMessages() {
        getChatMessages( senderId, receiverId, new FirestoreMessageCallback() {
            @Override
            public void onMessagesReceived(List<ChatMessage> messageData) {

                chatAdapter = new ChatAdapter(messageData, senderId);
                recyclerView.setAdapter(chatAdapter);
                // Scroll the RecyclerView to the last position
                recyclerView.scrollToPosition(messageData.size() - 1);

                Log.d(TAG, "Updated RecyclerView with chat messages");
            }
        });
    }


    private void getChatMessages( String senderId, String receiverId, FirestoreMessageCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference chatMessagesRef = db.collection("chatrooms")
                .document(getChatroomId(this.senderId,this.receiverId))
                .collection("chatMessages");

        chatMessagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error fetching chat messages: " + error.getMessage());
                        return;
                    }

                    List<ChatMessage> messageData = new ArrayList<>();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            ChatMessage chatMessage = doc.toObject(ChatMessage.class);
                            messageData.add(chatMessage);
                            Log.d(TAG, "Fetched Message: " + chatMessage.getMessageText());
                        }
                    }
                    callback.onMessagesReceived(messageData);
                });
    }


    private String fetchSenderDetails() {

        User currentUser = FireBaseUtils.getCurrentUserDetails(this);

        if (currentUser != null) {
            // Now you can access current user details like email, name, role, shipperId, etc.
            String userEmail = currentUser.getEmail();
            senderId = userEmail;
            Log.i("UserEMail",userEmail);
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
                    receiverId = document.getString("email");  // Set the receiver's ID


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

    private void sendMessageToChatRoom(String senderId, String receiverId, String messageText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long timeStamp = System.currentTimeMillis();

        // Document reference for the chat room
        DocumentReference chatRoomRef = db.collection("chatrooms").document(getChatroomId(senderId,receiverId));

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
                            //Toast.makeText(this, "chat added!", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.d(TAG, "Error adding message: " + e.getMessage());
                        });
            } else {
                // Chat room doesn't exist, create it and add the message
                createOrSendMessage( senderId, receiverId, messageText);
            }
        });
    }

    private void createOrSendMessage( String senderId, String receiverId, String initialMessage) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        long timeStamp = System.currentTimeMillis();

        // Document reference for the chat room
        DocumentReference chatRoomRef = db.collection("chatrooms").document(getChatroomId(senderId,receiverId));

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
                                        //Toast.makeText(this, "chat added!", Toast.LENGTH_LONG).show();
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
                            //Toast.makeText(this, "chat added!", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.d(TAG, "Error adding message: " + e.getMessage());
                        });
            }
        });
    }


    public String getChatroomId(String userId1, String userId2) {

        if (userId1 != null && userId2 != null) {
            if (userId1.compareTo(userId2) < 0) {
                Log.i("if::::",userId1 + "_" + userId2);
                return userId1 + "_" + userId2;
            } else {
                Log.i("else::::",userId2 + "_" + userId1);
                return userId2 + "_" + userId1;
            }
        } else {
            // Handle the case where either userId1 or userId2 is null
            Log.e(TAG, "One or both user IDs are null");
            return ""; // or any appropriate default value
        }

}
    public interface FirestoreMessageCallback {
        void onMessagesReceived(List<ChatMessage> messageData);
    }
}
