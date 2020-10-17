package com.plannet.apps.diarybook.SyncManager;

import android.content.Context;
import android.util.Log;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.SyncManager.DefaultErrorListener;

import org.json.JSONArray;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class JsonRequest extends JsonArrayRequest {
    private final Context context;
    public JsonRequest(Context context, String url, Response.Listener<JSONArray> listener, final Response.ErrorListener errorListener) {
        // Pass a CatchAll listener to trap InvalidToken errors
        super(url, listener, new DefaultErrorListener(errorListener, new DefaultErrorListener.RetryCallback() {
            @Override
            public void retry(Request request) {
                AppController.getInstance().submitServerRequest(request, "resubmit");
            }
        }));
        ((DefaultErrorListener)super.getErrorListener()).setRequest(this); // 'this' is only accessible after super ctor is called
        Log.d("ArrayRequest url", url);

        this.context = context;
        // Retries are OK with GET requests
        // Set timeout=5s & retries = 2 & back off multiplier = 2
        // issue related to multiple number of  rquest  can be handle  setting  initiaol value of timeout as 0
        setRetryPolicy(new DefaultRetryPolicy(30000, 2, 2));
    }

    public JsonRequest(Context context, String url,JSONArray jsonData, Response.Listener<JSONArray> listener, final Response.ErrorListener errorListener) {
        // Pass a CatchAll listener to trap InvalidToken errors
        super(Method.POST, url, jsonData, listener, new DefaultErrorListener(errorListener, new DefaultErrorListener.RetryCallback() {
            @Override
            public void retry(Request request) {
                AppController.getInstance().submitServerRequest(request, "resubmit");
            }
        }));
        ((DefaultErrorListener)super.getErrorListener()).setRequest(this); // 'this' is only accessible after super ctor is called
        Log.d("ArrayRequest url", url);

        this.context = context;
        // Retries are OK with GET requests
        // Set timeout=5s & retries = 2 & back off multiplier = 2
        // issue related to multiple number of  rquest  can be handle  setting  initiaol value of timeout as 0
        setRetryPolicy(new DefaultRetryPolicy(30000, 2, 2));
    }

    @Override
    public final Map<String, String> getHeaders() throws AuthFailureError {
        if(!AppController.getInstance().isAuthenticated())
            throw new AuthFailureError("User Not authenticated on server or Token expired");

        Map<String, String> params = new HashMap<String, String>();

        //params.put("Accept", "application/json");
        //params.put("Content-Type", "application/json; charset=utf-8");

        try {
            params.put("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlcnVzZXIiLCJzY29wZXMiOlt7ImF1dGhvcml0eSI6IkFETUlOIn1dLCJpc3MiOiJodHRwczovL3d3dy5wbGFuZXQuY29tIiwiaWF0IjoxNjAyNjk4NjA2LCJleHAiOjE2MDI3MTY2MDZ9.5Ck-o0mOd9YvNqEWxPb7ZWVWmqafa630b_YB_XelTAg");
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthFailureError("Bad Authentication token", e);
        }
        return params;
    }
}
