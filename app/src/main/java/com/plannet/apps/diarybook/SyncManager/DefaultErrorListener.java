package com.plannet.apps.diarybook.SyncManager;

import android.util.Log;

import com.android.volley.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.plannet.apps.diarybook.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;


public class DefaultErrorListener implements Response.ErrorListener {


    private final RetryCallback callback;
    private Request request;
    private final Response.ErrorListener originalListener;

    public DefaultErrorListener(Response.ErrorListener originalListener, RetryCallback callback) {
        this.originalListener = originalListener;
        this.callback = callback;
    }


    public Request getRequest() {
        return request;
    }

    /**
     * request object should be set
     * @param request
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        // Handle invalid token automatically
        // Expected json: {"error":"invalid_token","error_description":"Invalid access token: 9f6e928d-a56d-47bc-924f-bf70f7646d00"}
        boolean isInvalidLoginToken = false;
        final int status = error.networkResponse != null ? error.networkResponse.statusCode : 0;
        if (error instanceof AuthFailureError) {
            NetworkResponse response = error.networkResponse;
            if (response != null && response.data != null) {

                try {
                    JSONObject jsonObject = new JSONObject(new String(response.data));
                    isInvalidLoginToken = "invalid_token".equals(jsonObject.optString("error"));
                } catch (JSONException e) {
                }
            }
            if (isInvalidLoginToken) { // Resubmit automatically
                //AppController.getInstance().setAuthToken(null);
                callback.retry(request);
                Log.w("Request", "Request failed due to invalid/expired token. Retrying...");
            } else {
                originalListener.onErrorResponse(error);
            }
        } else if (request != null && (status == HttpURLConnection.HTTP_MOVED_PERM ||
                status == HttpURLConnection.HTTP_MOVED_TEMP ||
                status == HttpURLConnection.HTTP_SEE_OTHER)) {
            // Handle 30x

            final String location = error.networkResponse.headers.get("Location");
            Log.w("JsonReq", "Url Redirect: " + location);
            try {
                // update the request with new location
                Class volleyRequestClass  = request.getClass();
                // Iterate to get to supermost class.. where url is saved
                while(volleyRequestClass != Object.class && volleyRequestClass != Request.class) {
                    volleyRequestClass = volleyRequestClass.getSuperclass();
                }
                Field urlField = volleyRequestClass.getDeclaredField("mUrl");
                urlField.setAccessible(true); // To override private accessor
                // Now modify final modifier...
                Field modifiersField = Field.class.getDeclaredField("accessFlags"); // android uses field "accessFlags" instead of "modifiers"
                modifiersField.setAccessible(true);
                modifiersField.setInt(urlField, urlField.getModifiers() & ~Modifier.FINAL);
                urlField.set(request, location);
                // Resend the same request with new Url
                callback.retry(request);
                // TODO: Send notification to user to correct url.. in order to avoid redirect overhead for every request
            } catch (Exception e) {
                originalListener.onErrorResponse(error);
            }
        } else {
            originalListener.onErrorResponse(error);
        }

    }

    public interface RetryCallback {
        void retry(Request request);
    }
}
