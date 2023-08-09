package com.example.truckflow.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckflow.R;
import com.example.truckflow.entities.BookingRequest;

import java.util.List;

public class BookingRequestAdapter extends RecyclerView.Adapter<BookingRequestAdapter.ViewHolder> {

    private List<BookingRequest> bookingRequests;
    private OnItemClickListener onItemClickListener;

    public BookingRequestAdapter(List<BookingRequest> bookingRequests, OnItemClickListener listener) {
        this.bookingRequests = bookingRequests;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_request_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingRequest bookingRequest = bookingRequests.get(position);

        holder.loadNameTextView.setText(bookingRequest.getLoadName());
        holder.pickupLocationTextView.setText(bookingRequest.getPickupAddress());
        holder.deliveryLocationTextView.setText(bookingRequest.getDeliveryAddress());
        holder.dateTextView.setText(bookingRequest.getPickUpDate());
        holder.truckerNameTextView.setText(bookingRequest.getCompany_name());
        holder.truckerEmailTextView.setText(bookingRequest.getTruckerEmail());

        holder.acceptButton.setOnClickListener(v -> onItemClickListener.onAcceptClick(bookingRequest));
        holder.rejectButton.setOnClickListener(v -> onItemClickListener.onRejectClick(bookingRequest));
        holder.chatButton.setOnClickListener(v -> onItemClickListener.onChatClick(bookingRequest));
    }

    @Override
    public int getItemCount() {
        return bookingRequests.size();
    }

    public interface OnItemClickListener {
        void onAcceptClick(BookingRequest bookingRequest);

        void onRejectClick(BookingRequest bookingRequest);

        void onChatClick(BookingRequest bookingRequest);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView loadNameTextView;
        TextView pickupLocationTextView;
        TextView deliveryLocationTextView;
        TextView dateTextView;
        TextView truckerNameTextView;
        TextView truckerEmailTextView;
        Button acceptButton;
        Button rejectButton;
        Button chatButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            loadNameTextView = itemView.findViewById(R.id.br_load_name);
            pickupLocationTextView = itemView.findViewById(R.id.br_pickloc);
            deliveryLocationTextView = itemView.findViewById(R.id.br_droploc);
            dateTextView = itemView.findViewById(R.id.br_date);
            truckerNameTextView = itemView.findViewById(R.id.br_trucker_name);
            truckerEmailTextView = itemView.findViewById(R.id.br_trucker_email);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            chatButton = itemView.findViewById(R.id.br_chat_btn);
        }
    }
}
