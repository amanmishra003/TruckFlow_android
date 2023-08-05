package com.example.truckflow.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.truckflow.entities.User;
import com.google.gson.Gson;

public class FireBaseUtils {

    public static User getCurrentUserDetails(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");

        // Convert the JSON string back to a User object
        return new Gson().fromJson(userJson, User.class);
    }

    /* how to use the above method
    User currentUser = FireBaseUtils.getCurrentUserDetails(this);

    if(currentUser !=null)
    {
        // Now you can access current user details like email, name, role, shipperId, etc.
        String userEmail = currentUser.getEmail();
        String userName = currentUser.getName();
        String userRole = currentUser.getRole();
        String userShipperId = currentUser.getShipperId();

        // Use the current user details as needed
    } else

    {
        // Current user details not available or invalid
        // Handle the case gracefully
    }*/

}
