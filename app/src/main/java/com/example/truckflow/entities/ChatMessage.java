package com.example.truckflow.entities;

public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String messageText;
    private long timestamp;

    public ChatMessage() {
        // Default constructor required for Firestore
    }

    public ChatMessage(String senderId, String receiverId, String messageText, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
