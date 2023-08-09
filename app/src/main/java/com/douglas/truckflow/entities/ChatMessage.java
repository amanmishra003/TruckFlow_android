package com.douglas.truckflow.entities;

public class ChatMessage {
    private String messageId;
    private String senderId;
    private String receiverId;
    private String messageText;
    private long timestamp;

    public ChatMessage() {
        // Default constructor required for Firestore
    }
    //for creating messages from user side
    public ChatMessage(String senderId, String receiverId, String messageText, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    //for retrieving messages from firebse
    public ChatMessage(String senderId, String messageId, String receiverId, String messageText, long timestamp) {
        this.senderId = senderId;
        this.messageId = messageId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }




    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
