package com.example.truckflow.adpters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.truckflow.R;
import com.example.truckflow.load.LoadDetails;
import com.example.truckflow.models.Load;

import java.util.List;

public class LoadAdapter extends RecyclerView.Adapter<LoadAdapter.LoadViewHolder> {
    private List<Load> loadList;

    public LoadAdapter(List<Load> loadList) {
        this.loadList = loadList;
    }

    //method for updating the list  according to requirement
    public void updateLoads(List<Load> loadList) {
        this.loadList = loadList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LoadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load, parent, false);
        return new LoadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LoadViewHolder holder, int position) {
        Load load = loadList.get(position);
        holder.bind(load);
    }

    @Override
    public int getItemCount() {
        return loadList.size();
    }


    public class LoadViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        private TextView textViewLoadName;


        public LoadViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLoadName = itemView.findViewById(R.id.load_name);

            // Set the click listener on the item view
            itemView.setOnClickListener(this);
        }

        public void bind(Load load) {
            textViewLoadName.setText(load.getLoadName());

        }

        @Override
        public void onClick(View v) {
            // Get the clicked item position
            int position = getAdapterPosition();
            // Handle the click event, e.g., start a new activity with load details
            if (position != RecyclerView.NO_POSITION) {
                Load clickedLoad = loadList.get(position);
                // Start a new activity and pass the load details to it
                Intent intent = new Intent(itemView.getContext(), LoadDetails.class);
                intent.putExtra("load", clickedLoad);
                itemView.getContext().startActivity(intent);
            }
        }
    }
}

