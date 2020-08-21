package com.plannet.apps.diarybook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "diaryBook.db";
    private static final int DATABASE_VERSION = 1;
    private final String TAG = "DatabaseHandler";
    private final Context mContext;


    // Use singleton with Client Count.
    // Otherwise concurrent access may give database locked exception
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        dbClientCount = new AtomicInteger(0);
        Log.d(TAG, "Database: '" + DATABASE_NAME + "' Version: '" + DATABASE_VERSION + "'");
        upgraded = false;

    }

    AtomicInteger dbClientCount;
    boolean upgraded;

    public boolean isUpgraded() {
        return upgraded;
    }

    public void resetUpgraded() {
        this.upgraded = false;
    }

    static DatabaseHandler instance;

    // Synchronized on class object
    public static synchronized DatabaseHandler getInstance(Context context) {
        if (instance == null) {
            try {
                instance = new DatabaseHandler(context);
            } catch (Exception e) {
                //ErrorMsg.showError(context,"Error While Setting Up Database. Contact Customer Service","" , "DB");
                Log.e("DB", "Error while setting up database. " + e.getMessage(), e);
            }
        }
        instance.registerConnection();
        return instance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    public static synchronized void reload() {
        if (instance != null) {
            try {
                instance.close();
            } catch (Exception ex) {
                Log.e("DB", "Unable to close database. " + ex.getMessage(), ex);
            }
            instance = null;
        } // new instance will be created on next call
    }

    @Override
    public void close() {
        unregisterConnection();
    }

    // Synchronization should be done same object for register & unregister. Hence use instance methods
    private synchronized void registerConnection() {
            int count = instance.dbClientCount.incrementAndGet();
            if (count > 49) // Print only when it goes out of control
                Log.w("DB", "New database connection request. Connection Count=" + count);

    }

    // Synchronization should be done same object for register & unregister. Hence use instance methods
    private synchronized void unregisterConnection() {
        int count = dbClientCount.decrementAndGet();
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating new Database: '" + DATABASE_NAME + "' Version: '" + DATABASE_VERSION + "'");
        // TODO Freez this with version 5. on 1.3.x version. All further changes shall be managed thr #onUpgrade()
        // TODO and Increment version number with every change
        try {
            db.beginTransaction();
            db.execSQL("CREATE TABLE IF NOT EXISTS Customer(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , customer_id INTEGER ," +
                    " customer_name TEXT , customer_code TEXT , country TEXT , city TEXT , address1 TEXT , address2 TEXT ," +
                    " email TEXT,phone_no TEXT,region TEXT,location TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Products(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , product_id INTEGER , " +
                    "product_category_Id INTEGER , product_category TEXT , product_name TEXT ,uom TEXT ,uomId TEXT, description TEXT , " +
                    "sale_price TEXT , cost_price TEXT, taxCategoryId INTEGER  )");
            db.execSQL("CREATE TABLE IF NOT EXISTS Category(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,category_Id INTEGER ," +
                    "category_name TEXT ,parentCategoryId INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Diary(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,customerName INTEGER ," +
                    "customerId INTEGER ,date TEXT,time TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS DiaryLines( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,customerName INTEGER ," +
                    "customerId INTEGER ,date TEXT,time TEXT)");
            db.setTransactionSuccessful();


        } catch (Exception e) {
            //ErrorMsg.showError(mContext, "Internal Error", "Error while setting up database. Contact Customer service", "DB");
            Log.e("DB", "Error while setting up database. " + e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        onUpgrade(db, 5, DATABASE_VERSION);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO All further changes beyond version 5 shall be managed thr this
        // TODO and Increment version number with every change
        Log.w(TAG, "Upgrading database from version  " + oldVersion
                + " to " + newVersion + "");
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            Log.w(TAG, "Upgrading database to version: " + upgradeTo);
            switch (upgradeTo) {
                case 1:
                    onCreate(db);
                    break;
            }
            Log.w(TAG, "Upgraded database to version: " + upgradeTo);
            upgradeTo++;
        }
        // Views cannot be altered. Hence drop and create again // This should be done last
        upgraded = true;
        //reCreateViews(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        // TODO Handle incompatible upgrades here
        Log.w(TAG, "Downgrading database from version  " + oldVersion
                + " to " + newVersion + ", which remove all old records");
    }



    private void upgradeTo_version1(SQLiteDatabase db) {
        // Version 6 - For stock take
        try {
            db.beginTransaction();
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            //ErrorMsg.showError(mContext, "Internal Error", "Error while upgrading database (v6). Contact Customer service", "DB");
            Log.e("DB", "Error while setting up database. " + ex.getMessage(), ex);
            throw ex;
        } finally {
            db.endTransaction();
        }
    }
}
