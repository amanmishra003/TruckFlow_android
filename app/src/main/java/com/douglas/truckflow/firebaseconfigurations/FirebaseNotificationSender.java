package com.douglas.truckflow.firebaseconfigurations;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirebaseNotificationSender {

    private String fcmToken;
    private String title;
    private String body;
    private String toGo;
    private Activity mActivity;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey = "AIzaSyAtw3f2NBYcbNVz01pmZPfZnQlOwnoErNk"; // Replace with your FCM server key

    public FirebaseNotificationSender(String fcmToken, String title, String body, Activity mActivity) {
        this.fcmToken = fcmToken;
        this.title = title;
        this.body = body;
        this.mActivity = mActivity;
    }



    public FirebaseNotificationSender(String fcmToken, String title, String body, Activity mActivity, String toGo) {
        this.fcmToken = fcmToken;
        this.title = title;
        this.body = body;
        this.mActivity = mActivity;
        this.toGo = toGo;
    }

    public void sendNotification() {
        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", fcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", "ic_launcher_background"); // enter icon that exists in drawable only
            mainObj.put("notification", notiObject);
            JSONObject extraData = new JSONObject();
            extraData.put("activity", toGo);
            mainObj.put("data", extraData);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj,
                    response -> {
                        // Handle success response here
                    },
                    error -> {
                        // Handle error response here
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "key=" + fcmServerKey);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
