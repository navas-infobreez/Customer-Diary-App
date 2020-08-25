package com.plannet.apps.diarybook;

import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import androidx.multidex.MultiDexApplication;


public class AppController extends MultiDexApplication {


    private final String TAG="AppController";
    private static AppController mInstance;

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

