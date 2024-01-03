package com.example.pawrescue;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_LOGGED_IN = "user_logged_in";

    private SharedPreferences preferences;

    public PreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveCredentials(String username, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public String getSavedUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public String getSavedPassword() {
        return preferences.getString(KEY_PASSWORD, "");
    }

    public void clearCredentials() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.apply();
    }

    public void setUserLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_USER_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return preferences.getBoolean(KEY_USER_LOGGED_IN, false);
    }
}


