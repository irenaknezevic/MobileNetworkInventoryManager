package com.knezevic.mobilenetworkinventorymanager_pis.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String SHARED_PREFS_NAME = "mobile_network_inventory_prefs";
    private Context ctx;
    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesHelper(Context ctx) {
        this.ctx = ctx;
        sharedPref = ctx.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public String getValueString(String key) {
        return sharedPref.getString(key, "empty_string");
    }

    public void setValueString(String key, String value) {
        editor.putString(key, value).apply();
    }

    public void clearSharedPreferences() {
        editor.clear().apply();
    }
}
