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
    private final Context context;

    public static final String DRAFT = "DRAFT";
    public static final String PENDING = "PENDING";
    public static final String PICKED = "PICKED";
    public static final String COMPLETED = "COMPLETED";
    public static final String APPROVED = "APPROVED";
    public static final String APPROVERETURN = "REJECTED";
    public static final String ALL = "ALL";
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        dbClientCount = new AtomicInteger(0);
        Log.d(TAG, "Database: '" + DATABASE_NAME + "' Version: '" + DATABASE_VERSION + "'");
        upgraded = false;

    }

    AtomicInteger dbClientCount;
    boolean upgraded;

    static DatabaseHandler instance;

    public static synchronized DatabaseHandler getInstance(Context context) {
        if (instance == null) {
            try {
                instance = new DatabaseHandler(context);
            } catch (Exception e) {
                ErrorMsg.showError(context,"Error While Creating Database. Contact Customer Service",e , "DB");
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
        try {
            db.beginTransaction();
            db.execSQL("CREATE TABLE IF NOT EXISTS Customer(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , customer_id INTEGER ," +
                    " customer_name TEXT , customer_code TEXT , country TEXT , city TEXT , address1 TEXT , address2 TEXT ," +
                    " email TEXT,phone_no TEXT,region TEXT,location TEXT,locatioId INTEGER,gst_no TEXT)");

            db.execSQL("CREATE TABLE IF NOT EXISTS Products(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , product_name TEXT,product_id INTEGER , " +
                    "product_category TEXT ,product_category_Id INTEGER , uom TEXT ,uomId TEXT, description TEXT , " +
                            "sale_price TEXT , cost_price TEXT, taxId INTEGER  )");

            db.execSQL("CREATE TABLE IF NOT EXISTS ProductCategory(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,category_name TEXT ,category_Id INTEGER ," +
                    "parentCategoryId INTEGER)");

            db.execSQL("CREATE TABLE IF NOT EXISTS CustomerDiary(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,diaryId INTEGER ,customerName TEXT ," +
                    "address TEXT,phone TEXT,customerId INTEGER ,date TEXT,time TEXT,salesman_name TEXT,salesmanId INTEGER,invoice_no TEXT,quotation_no TEXT,descripion TEXT,status TEXT," +
                    "isVisit INTEGER,isQuotation INTEGER,isInvoiced INTEGER,totalAmount TEXT)");

            db.execSQL("CREATE TABLE IF NOT EXISTS CustomerDiaryLines( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,headerId INTEGER ," +
                    "product_name TEXT,product_id INTEGER,qty INTEGER,price TEXT,details TEXT,category TEXT ,uomId INTEGER,categoryId INTEGER)");

            db.execSQL("CREATE TABLE IF NOT EXISTS Visit( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,customerName INTEGER ," +
                    "customerId INTEGER ,visit_date TEXT,visit_time TEXT,place TEXT)");

            db.execSQL("CREATE TABLE IF NOT EXISTS Role( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,role_name TEXT ," +
                    "role_id INTEGER,description TEXT,priority INTEGER,isActive INTEGER)");

            db.execSQL("CREATE TABLE IF NOT EXISTS User( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,name TEXT ," +
                    "role_name TEXT,role_id INTEGER,userName TEXT,password TEXT)");

            db.execSQL("CREATE TABLE IF NOT EXISTS Uom( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,name TEXT ," +
                    "searchKey TEXT,description TEXT,active INTEGER,remoteId INTEGER)");

            db.execSQL("CREATE TABLE IF NOT EXISTS ProductPrice( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,productId INTEGER ," +
                    "uomId INTEGER,purchasePrice TEXT,salesPrice TEXT,discntSalesPrice TEXT,productPriceId INTEGER)");

            db.setTransactionSuccessful();


        } catch (Exception e) {
            ErrorMsg.showError( context, "Internal Error", e,"Error");
            Log.e("DB", "Error while setting up database. " + e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        onUpgrade(db, 1, DATABASE_VERSION);


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
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        // TODO Handle incompatible upgrades here
        Log.w(TAG, "Downgrading database from version  " + oldVersion
                + " to " + newVersion + ", which remove all old records");
    }



    private void upgradeTo_version1(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            //ErrorMsg.showError(context, "Internal Error", "Error while upgrading database (v6). Contact Customer service", "DB");
            Log.e("DB", "Error while setting up database. " + ex.getMessage(), ex);
            throw ex;
        } finally {
            db.endTransaction();
        }
    }
}
