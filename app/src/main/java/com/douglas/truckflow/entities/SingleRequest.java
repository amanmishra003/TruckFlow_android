package com.douglas.truckflow.entities;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class SingleRequest {
    private static SingleRequest instance;
    private RequestQueue requestQueue;
    private Context ctx;

    private SingleRequest(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }
    public static synchronized SingleRequest getInstance(Context context) {
        if (instance == null) {
            instance = new SingleRequest(context);
        }
        return instance;
    }
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }
    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }
}
