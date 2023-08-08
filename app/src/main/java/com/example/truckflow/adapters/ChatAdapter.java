package com.example.truckflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckflow.R;
import com.example.truckflow.entities.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    // View types to differentiate sender and receiver views
    private static final int VIEW_TYPE_SENDER = 1;
    private static final int VIEW_TYPE_RECEIVER = 2;

    // List of chat messages and the current user's ID
    private List<ChatMessage> messages;
    private String currentUserId;

    public ChatAdapter(List<ChatMessage> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        // Inflate the chat_message_row.xml layout for both sender and receiver
        itemView = inflater.inflate(R.layout.chat_message_row, parent, false);

        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        ChatMessage message = messages.get(position);

        if(message.getSenderId().equals(currentUserId)){
            holder.leftChatLayout.setVisibility(View.GONE); // Hide receiver's text view
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatTextView.setText(message.getMessageText());
        }

        // Set the text of the appropriate TextView based on the message sender

        else {
            holder.rightChatLayout.setVisibility(View.GONE); // Hide receiver's text view
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextView.setText(message.getMessageText());

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENDER;
        } else {
            return VIEW_TYPE_RECEIVER;
        }
    }

    // ViewHolder class for chat item view
    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leftChatLayout, rightChatLayout;
        TextView leftChatTextView;    // TextView for sender's message
        TextView rightChatTextView;  // TextView for receiver's message

        public ChatViewHolder(View itemView) {
            super(itemView);
            leftChatTextView = itemView.findViewById(R.id.left_chat_textview);
            rightChatTextView = itemView.findViewById(R.id.right_chat_textview);
            leftChatLayout = itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout = itemView.findViewById(R.id.right_chat_layout);
        }
    }
}
