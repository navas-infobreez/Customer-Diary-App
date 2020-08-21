package com.posibolt.apps.diarybook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.posibolt.apps.diarybook.ErrorMsg;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "posiboltPDA.db";

    private static final int DATABASE_VERSION = 1;
    public static final String ARCHIVE_DATABASE_NAME = "diaryBook.db";


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



    // TODO Need to decide whether to restore entireDB or only part of it
//    public static void restoreFromBackup(Context context, File inFile) {
//        try {
//            Log.i("restore", "Restoring db from :" + inFile.getPath());
//            String outFileName = context.getDatabasePath(DatabaseHandler.DATABASE_NAME).getPath();
//
//            if (inFile.exists()) {
//                FileUtils.copyFile(inFile, new File(outFileName));
//                // Added for Android 9+ to delete shm and wal file if they exist
//                File dbshm = new File(inFile.getPath() + "-shm");
//                File dbwal = new File(inFile.getPath() + "-wal");
//                if (dbshm.exists()) {
//                    dbshm.delete();
//                }
//                if (dbwal.exists()) {
//                    dbwal.delete();
//                }
//            }
//            DatabaseHandler.reload();
//            // When restoring database, ReportRecordManager cache should be reset to avoid dangling reference
////			ReportRecordManager.reset();
//            Preference.setSelectedTripplan(0);
//        } catch (Exception e) {
//            ErrorMsg.showError(context, "Error While Importing Backup DB", e, "DbRestore");
//        }
//    }

    public static void restoreFromArchiveBackup(Context context, File inFile) {
        try {
//			DatabaseHandler archiveDbhelper=DatabaseHandler.getArchiveInstance(context,inFile.getName());
//			SQLiteDatabase archiveDatabase=archiveDbhelper.getWritableDatabase();
//
//			archiveDatabase.beginTransaction();
            Log.i("restore", "Restoring db archive from :" + inFile.getPath());
            String outFileName = context.getDatabasePath(inFile.getName()).getPath();

            if (inFile.exists()) {
               // FileUtils.copyFile(inFile, new File(outFileName));
            }
            //archiveDbhelper.reload();
            // When restoring database, ReportRecordManager cache should be reset to avoid dangling reference
            //ReportRecordManager.reset();
        } catch (Exception e) {
           // ErrorMsg.showError(context, "Error while importing backup DB", e, "DbRestore");
        }
    }


//    public static String backupDb(boolean isShowDbName) {
//        try {
//
//            // OS may rename the file, in case of dups, as xx.db1.backup - mismatching pattern. Hence use .db extension
//            // TODO better give user option to select folder and file name
//            ProfileModel selectedProfile = AppController.getInstance().getSelectedProfile();
//            final String outFileName = Environment.getExternalStorageDirectory() + "/" + AppController.getInstance().getString(R.string.app_name) + "/" +
//                    "DBBkp." + selectedProfile.getUserName().replace(' ', '_').replace('@', '_') + "." + CommonUtils.getCurrentDateAndTime() + ".db";
//            String inFileName = AppController.getInstance().getDatabasePath(DatabaseHandler.DATABASE_NAME).getPath();
//            File inFile = new File(inFileName);
//
//            if (inFile.exists()) {
//                FileUtils.copyFile(inFileName, outFileName);
//                if (isShowDbName)
//                    Popup.show(AppController.getInstance(), "Backup Db Created At :" + outFileName);
//                return outFileName;
//            }
//
//        } catch (Exception e) {
//            ErrorMsg.showError(AppController.getInstance(), "Error While Doing Backup Of Database", e, "DbBackup");
//        }
//
//        return null;
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating new Database: '" + DATABASE_NAME + "' Version: '" + DATABASE_VERSION + "'");
        // TODO Freez this with version 5. on 1.3.x version. All further changes shall be managed thr #onUpgrade()
        // TODO and Increment version number with every change
        try {
            db.beginTransaction();
            db.execSQL("CREATE TABLE IF NOT EXISTS Tbl_po (	`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	`order_id`	TEXT NOT NULL,	`bpartner_id`	TEXT,	`bpartner_name`	TEXT,	`price_list_id`	TEXT,	`order_no`	TEXT,	`description`	TEXT,	`payment_roule`	TEXT,	`grant_total`	TEXT,	`date_ordered`	TEXT,	`date_promised`	TEXT,	`reference_no`	TEXT, `rg_id`  INTEGER )");
            db.execSQL("CREATE TABLE IF NOT EXISTS Tbl_po_lines(	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	`order_id`	TEXT NOT NULL, `order_no`	TEXT,	`product_id`	TEXT,	`upc`	TEXT,	`sku`	TEXT,	`product_name`	TEXT,	`uom`	TEXT,	`product_description`	TEXT,	`unitprice`	TEXT,	`total_qty`	TEXT,	`discount`	TEXT,	`line_total`	TEXT, `rg_id` INTEGER, description TEXT )");
            db.execSQL("CREATE TABLE IF NOT EXISTS received_goods(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , user TEXT , vender TEXT ,bpartner_id INTEGER, order_no TEXT , shipment_no TEXT , created TIMESTAMP DEFAULT CURRENT_TIMESTAMP , status TEXT,profile_id INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS received_goods_lines(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , rg_id INTEGER REFERENCES received_goods(id) ON DELETE CASCADE , order_id INTEGER , product_id INTEGER , `upc`	TEXT,	`sku`	TEXT,	`product_name`	TEXT,	uom TEXT, order_qty TEXT, confirm_qty TEXT, description TEXT, order_line_id INTEGER  )");
            db.execSQL("CREATE TABLE IF NOT EXISTS sync_status(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , type TEXT , status TEXT , remort_record_id INTEGER )");
            db.execSQL("CREATE TABLE IF NOT EXISTS auth_token(auth_token TEXT , access_token TEXT , token_type TEXT , refresh_token TEXT , expires_in TEXT , scope TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS profile(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , name TEXT , base_url TEXT , user_name TEXT, password TEXT,pin TEXT,client_id INTEGER, client_secret TEXT, terminal_name TEXT,is_active TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS RouteShipmentRecord(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , route_name TEXT , route_code TEXT,route_id INTEGER,profile_id INTEGER,status TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Customer(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , customer_id INTEGER , customer_name TEXT , customer_code TEXT , country TEXT , city TEXT , address1 TEXT , address2 TEXT , route_id INTEGER, seaquence_no INTEGER, record_id INTEGER, sales_rep_id INTEGER, email TEXT, postal_code TEXT, profile_id INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Orders(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , sales_rep text , customer_id INTEGER , customer_code TEXT , customer_name TEXT , payment_roule TEXT , date_ordered TEXT , grant_total TEXT , order_number TEXT , profile_id INTEGER , order_id INTEGER , record_id INTEGER,is_selected TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS OrderLines(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , tag TEXT , lotnumber TEXT , upc TEXT , qty_delivered TEXT , exp_date TEXT , line_total TEXT , qty_ordered TEXT , product_id INTEGER , price TEXT , uom TEXT , product_name TEXT , record_id INTEGER , order_id INTEGER,profile_id INTEGER, sku TEXT ,description TEXT, order_line_id INTEGER )");
            db.execSQL("CREATE TABLE IF NOT EXISTS Shipments(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , shipment_no TEXT , order_no TEXT , customer_id INTEGER , dateShipped TEXT , dateFormat TEXT , description TEXT , record_id INTEGER , profile_id INTEGER,status TEXT )");
            db.execSQL("CREATE TABLE IF NOT EXISTS ShipmentLines(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , deliver_qty TEXT , product_id INTEGER ,ref_qty TEXT, upc TEXT , uom TEXT , lot_no TEXT , serail_no TEXT , date_format TEXT , order_id INTEGER ,record_id INTEGER, profile_id INTEGER , exp_date TEXT,product_name TEXT, customer_id INTEGER, sku TEXT, description TEXT, order_line_id INTEGER )");
            db.execSQL("CREATE TABLE IF NOT EXISTS UomConversion(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , profile_id INTEGER , product_id INTEGER , conversion_id INTEGER , multiple_rate INTEGER , divide_rate INTEGER , from_uom_id INTEGER , to_uom_id INTEGER , from_uom_name TEXT , to_uom_name TEXT , to_uom_symbol TEXT , is_active BOOLEAN , weight TEXT , thickness TEXT , length TEXT , volume TEXT , breadth TEXT , upc TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS StockTransferRecordActivity(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE , stock_transfer_id INTEGER , stock_transfer_number TEXT , profile_id INTEGER , status TEXT , fromWarehouse TEXT, to_warehouse TEXT, document_type TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS StockTransferLines(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE , serialNo TEXT , qty TEXT , batchNo TEXT , uom TEXT , productId INTEGER , productName TEXT , record_id INTEGER, entered_qty TEXT , sku TEXT ,upc TEXT,expiry_date TEXT, date_format TEXT,request_id INTEGER, description TEXT )");
            db.execSQL("CREATE TABLE IF NOT EXISTS Products(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , product_id INTEGER , product_category_Id INTEGER , product_category TEXT , product_name TEXT , sku TEXT , uom TEXT , upc TEXT , stock_qty TEXT , description TEXT , sale_price TEXT , cost_price TEXT, is_favorite TEXT , profile_id INTEGER, taxCategoryId INTEGER  )");
            db.execSQL("CREATE TABLE IF NOT EXISTS SalesRecord(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , customer_name TEXT , customer_code TEXT,customer_id INTEGER, sales_rep TEXT,invoice_date TEXT,profile_id INTEGER,status TEXT, grandTotal TEXT, discountAmt TEXT,totalLineDiscount TEXT,taxAmt TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS SalesLines(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , product_id INTEGER , product_name TEXT , sales_qty TEXT ,unit_price TEXT,line_total TEXT, tax TEXT , discount_amt TEXT, invoice_id INTEGER , uom TEXT, description TEXT, sku TEXT , upc TEXT, taxCategoryId INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Category(  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,category_Id INTEGER ,category_name TEXT ,parentCategoryId INTEGER,remote_imgurl TEXT,local_imgurl TEXT, isActive TEXT,profile_id INTEGER )");
            db.execSQL("CREATE TABLE IF NOT EXISTS TaxMaster(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, taxId INTEGER, parentTaxId INTEGER, taxName TEXT, taxRate TEXT, description TEXT, isTaxExempted BOOLEAN, validFrom DATETIME, isDefault BOOLEAN, taxCategoryId INTEGER, taxCategoryName TEXT, isDefaultCategory BOOLEAN, countryId INTEGER, profileId INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS InvoiceLineTax(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, invoiceId INETGER, invoiceLineId INTEGER, taxId INTEGER, profileId INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS Payments(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "invoiceId INETGER," + "discountAmt TEXT," + "amount TEXT," + "writeoffAmt TEXT," + "unallocatedAmt TEXT," +
                    "date 	TEXT," + "paymentType TEXT," + "paymentNo TEXT," + "customerId INTEGER," + "customerCode TEXT," +
                    "transactionNo TEXT," + "machineNo TEXT," + "chequeDate TEXT," + "accountNo TEXT," + "chequeNo TEXT," + "cardNo TEXT," +
                    "bankName TEXT," + "profileId TEXT)");
            db.setTransactionSuccessful();


        } catch (Exception e) {
            //ErrorMsg.showError(mContext, "Internal Error", "Error while setting up database. Contact Customer service", "DB");
            Log.e("DB", "Error while setting up database. " + e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
        onUpgrade(db, 5, DATABASE_VERSION);


    }

    // Views cannot be altered. Hence drop and create again. Add all views here

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



    private void upgradeTo_v6(SQLiteDatabase db) {
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
