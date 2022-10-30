package com.hiba.remedy.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.hiba.remedy.activities.HomeActivity;
import com.hiba.remedy.activities.LoginActivity;

import java.util.HashMap;

public class RemedyHelper {
    Activity activity;
    // Shared Preferences
  private   SharedPreferences pref;
     // Editor for Shared preferences
    private SharedPreferences.Editor editor;
    // Context
    private    Context context;

    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "RemedySession";
//
//    // Shared pref mode
//   private int PRIVATE_MODE = 0;
//
//    // Sharedpref file name
//    private static final String PREF_NAME = "RemedyPref";
//
//    // All Shared Preferences Keys
//    private static final String IS_LOGIN = "IsLoggedIn";
//
//    // User name (make variable public to access from outside)
//    public static final String KEY_ID = "id";
//
//    // Email address (make variable public to access from outside)
//    public static final String KEY_NAME = "name";

    // Constructor
    public RemedyHelper(Activity context){
        this.activity = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
//    /**
//     * Create login session
//     * */
//    public void createLoginSession(String id, String name){
//        // Storing login value as TRUE
//        editor.putBoolean(IS_LOGIN, true);
//
//        // Storing name in pref
//        editor.putString(KEY_ID, id);
//
//        // Storing email in pref
//        editor.putString(KEY_NAME, name);
//
//        // commit changes
//        editor.commit();
//    }
//    /**
//     * Get stored session data
//     * */
//    public HashMap<String, String> getUserDetails(){
//        HashMap<String, String> user = new HashMap<String, String>();
//        // user  id
//        user.put(KEY_ID, pref.getString(KEY_ID, null));
//
//        // user name
//        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
//
//        // return user
//        return user;
//    }
//
//    /**
//     * Check login method wil check user login status
//     * If false it will redirect user to login page
//     * Else won't do anything
//     * */
//    public void checkLogin(){
//        // Check login status
//        if(!this.isLoggedIn()){
//            // user is not logged in redirect him to Login Activity
//            Intent i = new Intent(context, LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            context.startActivity(i);
//        }else {
//            Intent i = new Intent(context, HomeActivity.class);
//            context.startActivity(i);
//
//        }
//
//    }
//    /**
//     * Clear session details
//     * */
//    public void logoutUser(){
//        // Clearing all data from Shared Preferences
//        editor.clear();
//        editor.commit();
//
//        // After logout redirect user to Login Activity
//        Intent i = new Intent(context, LoginActivity.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Staring Login Activity
//        context.startActivity(i);
//    }
//
//    /**
//     * Quick check for login
//     * **/
//    // Get Login State
//    public boolean isLoggedIn(){
//        return pref.getBoolean(IS_LOGIN, false);
//    }
    public void log(String key,String value){
       Log.d(key,value);
       Log.i(key,value);
    }
    // save value in SharedPreferences
    public void setValueToKey(String key, String value) {
        editor.putString(key, value).apply();
        editor.commit();
    }

    // get value from SharedPreferences if found else return #
    public String getValueOfKey(String key){
        String value = "";
        value =  String.valueOf(pref.getString(key, ""));
        if(value.equals("")){
            value = "";
        }
        return value;
    }
}
