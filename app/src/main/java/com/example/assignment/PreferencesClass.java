package com.example.assignment;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesClass {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    // Constructor
    public PreferencesClass(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("assignment_app", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void setUserLocation(String userLocation) {
        editor.putString("user_location", userLocation);
        editor.apply();
        editor.commit();
    }

    public String getUserLocation() {
        return sharedPreferences.getString("user_location", "");
    }

}
