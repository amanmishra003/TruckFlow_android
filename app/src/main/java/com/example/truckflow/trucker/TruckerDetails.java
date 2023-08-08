package com.example.truckflow.trucker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.truckflow.R;
import com.example.truckflow.communication.ChatActivity;
import com.example.truckflow.load.LoadDetails;

public class TruckerDetails extends AppCompatActivity {
    ImageButton chatWithTrucker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trucker_details);


        //Initilizing the views
        chatWithTrucker = findViewById(R.id.chat_with_trucker);


        // Retrieve data from the intent
        Intent intent = getIntent();
        String truckerId = intent.getStringExtra("email");
        Log.i("TruckerID",truckerId+"");

        //Taking to chat activity
        chatWithTrucker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TruckerDetails.this, ChatActivity.class);
                intent.putExtra("receiverId", truckerId);
                startActivity(intent);
            }
        });

    }
}