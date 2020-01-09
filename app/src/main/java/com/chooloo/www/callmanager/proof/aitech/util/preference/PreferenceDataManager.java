package com.chooloo.www.callmanager.proof.aitech.util.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.chooloo.www.callmanager.proof.aitech.activity.CallLogActivity;
import com.chooloo.www.callmanager.proof.aitech.activity.LoginActivity;

import java.util.Objects;


public class PreferenceDataManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "mealthyPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";


    // Constructor
    public PreferenceDataManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Storing UserData to pref
     */
    public void storeUserData(String key, String value) {
        // Storing value in pref
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, CallLogActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */
    public String getUserData(String key) {
        return pref.getString(key, "");
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        //IS_USER_LOGGED_IN =false;
        // After logout redirect user to Loing Activity
        _context.startActivity(new Intent(_context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        if (Objects.requireNonNull(pref.getString(IS_LOGIN, null)).equalsIgnoreCase("true")){
            return true;
        }
        return false;
    }

    public String getData(String Key) {
        return pref.getString(Key, null);
    }


    public String getGroceryData(String Key) {
        return pref.getString(Key, null);
    }
}
