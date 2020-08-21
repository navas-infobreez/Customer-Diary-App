package com.posibolt.apps.diarybook;

import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.text.format.DateFormat;



import java.io.File;
import java.io.IOException;
import java.util.Date;



public class AppController extends MultiDexApplication {

    public static final String FLAG_IS_READ_ONLY = "FLAG_IS_READ_ONLY";
    public static final String FLAG_CUSTOMER_LIST_CHANGED = "CUSTOMER_LIST_CHANGED";
    private static final String DEFAULT_CACHE_DIR = "networkCache";

    private final String TAG="AppController";
    private static AppController mInstance;

//    printer related
    // private SerialPort mSerialPort = null;

    @Override
    public void onCreate() {
        boolean DEVELOPER_MODE = BuildConfig.DEBUG && Settings.Secure.getInt(this.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0) > 0;
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
//                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate();
        mInstance = this;
        DatabaseHandlerController.getSqliteVersion(this);
    }



    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /** Request queue with default parallel threads managed by volley
     *
     * @return
     */

    /**
     * Request queue with single executor thread to serialize requests
     * @returng
     */

//
//    public ImageLoader getImageLoader() {
//        getDefaultRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }




    /*    PRINTER RELATED
    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
    if (mSerialPort == null) {
			/* Read serial port parameters
        //SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
        String path = HdxUtil.GetPrinterPort();;
        int baudrate = 115200;//Integer.decode(sp.getString("BAUDRATE", "-1"));

			/* Open the serial port
        mSerialPort = new SerialPort(new File(path), baudrate, 0);
    }
    return mSerialPort;
}

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }*/


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 3:

                        break;

                    case 4:

                        break;
                    case 5:
                        break;

                    case -1:
                        break;
                }
            }
            super.handleMessage(msg);
        }

    };

    public Handler getHandler() {
        return handler;
    }




}

