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
import com.example.truckflow.entities.Trucker;
import com.example.truckflow.load.LoadDetails;
import com.example.truckflow.trucker.TruckerDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TruckerAdapter extends RecyclerView.Adapter<TruckerAdapter.TruckerViewHolder> {

    private List<Trucker> truckerList;

    public TruckerAdapter(List<Trucker> truckerList) {
        this.truckerList = truckerList;
    }

    //method for updating the list  according to requirement
    public void updateTrucker(List<Trucker> truckerList) {
        this.truckerList = truckerList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TruckerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.truck_card, parent, false);
        return new TruckerAdapter.TruckerViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull TruckerViewHolder holder, int position) {
        Trucker trucker = truckerList.get(position);
        holder.bind(trucker);
    }

    @Override
    public int getItemCount() {
        return truckerList.size();
    }

    public class TruckerViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        private TextView truckerName,companyName,truckerType,
                truckerWeight,truckerLength,
                loadDistance,loadWeight;


        public TruckerViewHolder(@NonNull View itemView) {
            super(itemView);
            truckerName = itemView.findViewById(R.id.truckerName);
            companyName = itemView.findViewById(R.id.truck_type);
            truckerType = itemView.findViewById(R.id.company_name);
            truckerWeight = itemView.findViewById(R.id.truck_weight);
            truckerLength = itemView.findViewById(R.id.truck_length);
            // Set the click listener on the item view
            itemView.setOnClickListener(this);
        }

        public void bind(Trucker trucker) {

            truckerName.setText("   "+trucker.getTruck_name());
            companyName.setText(trucker.getCompany_name());
            truckerLength.setText("Length:"+trucker.getMax_length());
            truckerWeight.setText("Weight:"+trucker.getMax_weight());
            truckerType.setText(trucker.getTruck_type());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // Handle the click event, e.g., start a new activity with load details
            if (position != RecyclerView.NO_POSITION) {
                Trucker clickedLoad = truckerList.get(position);
                // Start a new activity and pass the load details to it
                Intent intent = new Intent(itemView.getContext(), TruckerDetails.class);
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
