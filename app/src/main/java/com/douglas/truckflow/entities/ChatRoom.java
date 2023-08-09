package com.douglas.truckflow.entities;

public class ChatRoom {
    private String senderId;
    private String receiverId;
    private long timestamp;
    private String chatRoomId;

    public ChatRoom() {
        // Default constructor required for Firestore
    }

    public ChatRoom(String senderId, String receiverId, long timestamp, String chatRoomId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
        this.chatRoomId = chatRoomId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }
}
