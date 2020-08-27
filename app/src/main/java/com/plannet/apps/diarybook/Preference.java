package com.plannet.apps.diarybook;


import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private static SharedPreferences getSharedAppPrefs() {
        return AppController.getInstance().getSharedPreferences(AppController.getInstance().getString(R.string.app_name), Context.MODE_PRIVATE);
    }


    public static void setnotFirstStart(Context context) {
        getSharedAppPrefs().edit().putBoolean("IsFirstStart", false).commit();
    }

    public static boolean getnotFirstStart(Context context) {
        return getSharedAppPrefs().getBoolean("IsFirstStart", true);
    }

    public static String getPassword() {

        return getSharedAppPrefs().getString("Password", "1234");

    }

    public static String  getUserName( ) {

        return getSharedAppPrefs().getString("User_name", "admin");

    }

    public static int getCustomerId() {

        return getSharedAppPrefs().getInt("getCustomerId", 10000);

    }

    public static int  setCustomerId( ) {

        return getSharedAppPrefs().getInt("getCustomerId", 10000);

    }

    public static void setLoggedUserId( int userId) {

        getSharedAppPrefs().edit().putInt("userId", userId).apply();

    }

    public static int  getLoggedUserId( ) {

        return getSharedAppPrefs().getInt("userId", 0);

    }


}
