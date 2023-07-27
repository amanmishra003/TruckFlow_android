package com.example.truckflow.adpters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.truckflow.R;

import com.example.truckflow.entities.Load;
import com.example.truckflow.load.LoadDetails;


import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
        private TextView textViewLoadName,pickUpLocation,
                loadDropLocation,loadPickUpMonth,loadPickUpDate,
        loadDistance,loadWeight;


        public LoadViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLoadName = itemView.findViewById(R.id.load_name);
            pickUpLocation = itemView.findViewById(R.id.load_pick_loc);
            loadDropLocation = itemView.findViewById(R.id.load_drop_loc);
            loadPickUpDate = itemView.findViewById(R.id.load_pick_date);
            loadPickUpMonth = itemView.findViewById(R.id.load_pick_month);
            loadDistance = itemView.findViewById(R.id.load_distance);
            loadWeight = itemView.findViewById(R.id.load_weight);
            // Set the click listener on the item view
            itemView.setOnClickListener(this);
        }

        public void bind(Load load) {
            textViewLoadName.setText(load.getLoadName());
            Date date = parseDate(load.pickUpDate);
            int day=0;
            String month="";
            if (date != null) {
                day = getDay(date);
                month = getMonthInAlphabetical(date);
                int year = getYear(date);
            } else {
                System.out.println("Invalid date format.");
            }

            loadPickUpDate.setText(String.valueOf(day));
            loadPickUpMonth.setText(String.valueOf(month));
            loadDistance.setText(load.getTotalDistance());
            loadWeight.setText(load.getLoadWeight());

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
//                intent.putExtra("load", clickedLoad);
                itemView.getContext().startActivity(intent);
            }
        }
    }

    private Date parseDate(String dateStr) {
        try {
            if (dateStr != null && !dateStr.isEmpty()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
                return formatter.parse(dateStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getMonthInAlphabetical(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthNumber = calendar.get(Calendar.MONTH);
        String[] monthNames = new String[] {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        return monthNames[monthNumber];
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}

