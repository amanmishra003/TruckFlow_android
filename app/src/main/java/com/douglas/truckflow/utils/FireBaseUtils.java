package com.douglas.truckflow.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.douglas.truckflow.entities.Trucker;
import com.douglas.truckflow.entities.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    public static void getUserDetails(String email, FirestoreUserCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference truckerCollectionRef = db.collection("users");

        truckerCollectionRef.whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);
                    User userData = document.toObject(User.class);
                    callback.onUserReceived(userData);
                } else {
                    callback.onUserReceived(null);
                }
            } else {
                callback.onUserReceived(null);
            }
        });
    }

    public interface FirestoreUserCallback {
        void onUserReceived(User user);
    }


    public static void sendNotification(String title, String body, String recipientToken, Context context) {
        String serverKey = "AAAAEoGhiow:APA91bG4upNFfSvy77aiH6_mQLrJGeIhugP0Pyk0XemIc8N59IoIR1KESjoeWXZi1SaThtcguf2JILO9ES8PBZ2zpnY4eiFf-pHlgtcrkNDtkqhJb0iX0ykUoVDp1ilA9hvSx0K_fv2c"; // Replace with your FCM server key
        String contentType = "application/json";
        String FCM_API = "https://fcm.googleapis.com/fcm/send";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        try {
            JSONObject data = new JSONObject();
            data.put("title", title);
            data.put("body", body);

            JSONArray registrationIds = new JSONArray();
            registrationIds.put(recipientToken);

            JSONObject notification = new JSONObject();
            notification.put("registration_ids", registrationIds);
            notification.put("notification", data);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                    response -> Log.i("Notification", "Notification sent successfully"),
                    error -> Log.e("Notification", "Error sending notification: " + error.toString())
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + serverKey);
                    headers.put("Content-Type", contentType);
                    return headers;
                }
            };
            Log.i("Request Send", jsonObjectRequest.toString());
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
