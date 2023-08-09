package com.example.truckflow.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.truckflow.entities.Trucker;
import com.example.truckflow.entities.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

public class FireBaseUtils {

    public static User getCurrentUserDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");

        return new Gson().fromJson(userJson, User.class);
    }

    public static void getCurrentTruckerDetails(String email, FirestoreTruckerCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference truckerCollectionRef = db.collection("trucker");

        truckerCollectionRef.whereEqualTo("truckerEmail", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);
                    Trucker truckerData = document.toObject(Trucker.class);
                    callback.onTruckerReceived(truckerData);
                } else {
                    callback.onTruckerReceived(null);
                }
            } else {
                callback.onTruckerReceived(null);
            }
        });
    }

    public interface FirestoreTruckerCallback {
        void onTruckerReceived(Trucker trucker);
    }
}
