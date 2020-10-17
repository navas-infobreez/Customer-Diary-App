package com.plannet.apps.diarybook.SyncManager;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.SyncManager.DefaultErrorListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DiaryBookJsonObjectRequest extends com.android.volley.toolbox.JsonObjectRequest{
    private final Context context;

    public DiaryBookJsonObjectRequest(Context context, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        // Pass a CatchAll listener to trap InvalidToken errors
        super(url, jsonRequest, listener, new DefaultErrorListener(errorListener, new DefaultErrorListener.RetryCallback() {
            @Override
            public void retry(Request request) {
                AppController.getInstance().submitServerRequest(request, "resubmit");
            }
        }));
        ((DefaultErrorListener)super.getErrorListener()).setRequest(this);// 'this' is only accessible after super ctor is called
        this.context = context;
        Log.d("ObjectRequest url", url + " Context: " + context);

        // Timeout of 5 secs is found in sufficient.. in case record is having many items
        if(jsonRequest != null) {// Retries cause request to be submitted in duplicate. Hence do not use with POST
            // Set timeout=30s & retries = 0 & back off multiplier = 2
            setRetryPolicy(new DefaultRetryPolicy(60000, 0, 1));
        } else {
            // Retries are OK with GET requests
            // Set timeout=5s & retries = 2 & back off multiplier = 2
            setRetryPolicy(new DefaultRetryPolicy(30000, 2, 2));
        }
    }


    @Override
    public final Map<String, String> getHeaders() throws AuthFailureError {
        if(!AppController.getInstance().isAuthenticated())
            throw new AuthFailureError("User Not authenticated on server or Token expired");

        Map<String, String> params = new HashMap<String, String>();
        //params.put("Accept", "application/json");
       // params.put("Content-Type", "application/json; charset=utf-8");
        try {
            params.put("Authorization","Bearer " + AppController.getInstance().getAuthToken().getResult().getToken());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthFailureError("Bad Authentication token", e);
        }
        return params;
    }
}
