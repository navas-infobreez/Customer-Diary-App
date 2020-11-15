package com.plannet.apps.diarybook;


import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private static final String APP_SETTINGS = "APP_SETTINGS";

    private static SharedPreferences getSharedAppPrefs() {
        return AppController.getInstance().getSharedPreferences(AppController.getInstance().getString(R.string.app_name), Context.MODE_PRIVATE);
    }
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
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

    public static int getNextProductId(Context context) {
        return getSharedPreferences(context).getInt("NextProductId" , -1000);
    }

    public static void setNextProductId(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("NextProductId" , newValue);
        editor.commit();
    }
    public static int NextProductId(Context context){
        int no= getNextProductId(context)-1;
        setNextProductId(context,no);
        return no;

    }

    public static int getNextProductPriceId(Context context) {
        return getSharedPreferences(context).getInt("NextProductPriceId" , -1000);
    }

    public static void setNextProductPriceId(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("NextProductPriceId" , newValue);
        editor.commit();
    }
    public static int NextProductPriceId(Context context){
        int no= getNextProductPriceId(context)-1;
        setNextProductPriceId(context,no);
        return no;
    }

    public static int getNextUomId(Context context) {
        return getSharedPreferences(context).getInt("NextUomId" , -1000);
    }

    public static void setNextUomId(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("NextUomId" , newValue);
        editor.commit();
    }
    public static int NextUomId(Context context){
        int no= getNextUomId(context)-1;
        setNextUomId(context,no);
        return no;
    }

    public static int getNextCategoryId(Context context) {
        return getSharedPreferences(context).getInt("NextCategoryId" , -1000);
    }

    public static void setNextCategoryId(Context context, int newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("NextCategoryId" , newValue);
        editor.commit();
    }
    public static int NextCategoryId(Context context){
        int no= getNextCategoryId(context)-1;
        setNextCategoryId(context,no);
        return no;
    }

}
