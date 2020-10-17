package com.plannet.apps.diarybook;

import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.models.Authentication;
import com.plannet.apps.diarybook.models.UserModel;
import com.android.volley.RequestQueue;


public class AppController extends MultiDexApplication {


    private final String TAG="AppController";
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private RequestQueue mSingleRequestQueue;
    private Authentication authToken;

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


    UserModel loggerUser;
    public int getLoggedUserId() {
        UserModel userModel = getLoggedUser();
        return userModel != null ? userModel.getId() : 0;
    }
    public UserModel getLoggedUser() {
        if(loggerUser != null)
            return loggerUser;
        User user = new User(getApplicationContext());
        if(loggerUser == null) {
            int loggerUserId = Preference.getLoggedUserId();
            loggerUser = user.getUser(loggerUserId);
        }

        return loggerUser;
    }

    public void setLoggedUser(UserModel userModel) {
        this.loggerUser = userModel;
        Preference.setLoggedUserId(userModel.getId());
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

    public <T> void submitServerRequest(final Request<T> req, final String tag) {
        submitServerRequest(req, tag, false);
    }

    public <T> void submitServerRequest(final Request<T> req, final String tag, boolean serial) {

        req.setTag( TextUtils.isEmpty(tag) ? TAG : tag);
        if(!AppController.getInstance().isAuthenticated()) {
            // TODO handle multiple requests coming in parallel
            final LoginManager loginManager = new LoginManager();
            loginManager.login(new OnCompleteCallback() {
                @Override
                public void onComplete() {
                    AppController.getInstance().setAuthToken(loginManager.getAuthModel());
                    getSerialRequestQueue().add(req);
                }

                @Override
                public void onError(Exception error) {
                    AppController.getInstance().setAuthToken(null);
                    req.deliverError((VolleyError) error);
                    Log.d(tag,"Login failed. " + (error != null ? error.getMessage() : "Unknown error"));
                }
            });
        } else {
            if(serial) {
                getSerialRequestQueue().add(req);
            } else {
                getDefaultRequestQueue().add(req);
            }
        }
    }


    public RequestQueue getDefaultRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this); // also starts the queue
        }
        return mRequestQueue;
    }

    public RequestQueue getSerialRequestQueue() {
        if (mSingleRequestQueue == null) {
            int MAX_SERIAL_THREAD_POOL_SIZE = 1;
            Cache cache = new DiskBasedCache(getCacheDir());
            BasicNetwork network = new BasicNetwork(new HurlStack()); // if (Build.VERSION.SDK_INT >= 9) {
            mSingleRequestQueue = new RequestQueue(cache, network, MAX_SERIAL_THREAD_POOL_SIZE);
            mSingleRequestQueue.start();
        }
        return mSingleRequestQueue;
    }


    public Authentication getAuthToken() {
        return authToken;
    }

    public void setAuthToken(Authentication authToken) {
        this.authToken = authToken;

    }

    public boolean isAuthenticated() {
        if(authToken == null)
            return false;
        return true;
    }
}

