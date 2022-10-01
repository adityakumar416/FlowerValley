package com.example.flowersvalley;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    public static final String PREF_NAME = "flower-valley";

    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String PHONE = "PHONE";

    private final SharedPreferences  sharedPreferences;

    public SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setName(String name) {
        getPreferencesEditor().putString(NAME, name).commit();
    }

    public String getName() {
        return sharedPreferences.getString(NAME, null);
    }

    public void setEmail(String email) {
        getPreferencesEditor().putString(EMAIL, email).commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public void setPhone(String mobile) {
        getPreferencesEditor().putString(PHONE, mobile).commit();
    }

    public String getPhone() {
        return sharedPreferences.getString(PHONE, null);
    }

    private SharedPreferences.Editor getPreferencesEditor() {
        return sharedPreferences.edit();
    }



}
