package com.plannet.apps.diarybook;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plannet.apps.diarybook.SyncManager.DefaultErrorListener;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.models.Authentication;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LoginManager {


    private Authentication authentication;
    private OnCompleteCallback onCompleteCallback;

    public Authentication getAuthModel() {
        return authentication;
    }





        public void login(final OnCompleteCallback onCompleteCallback){

            final String url = "https://planet-customerdiary.herokuapp.com/login/generate-token";
            //final String url = "https://planet-customerdiary.herokuapp.com/user/getalluserdetails";

            final JsonFormater formatter = new JsonFormater();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest( url, formatter.toJson(), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d( "Result", response.toString() );
                    try {
                        Gson gson = new Gson();

                        Type type = new TypeToken<Authentication>() {
                        }.getType();
                        authentication = gson.fromJson( response.toString(), type );
                        AppController.getInstance().setAuthToken(authentication);
                        if (onCompleteCallback != null) {
                            onCompleteCallback.onComplete();
                        }
                    } catch (Exception e) {
                        if (onCompleteCallback != null)
                            onCompleteCallback.onError( new Exception( "Error while parsing login response", e ) );
                    }

                }


            }, new DefaultErrorListener( new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (onCompleteCallback != null)
                        onCompleteCallback.onError( error );
                }
            }, new DefaultErrorListener.RetryCallback() {
                @Override
                public void retry(Request request) {
                    AppController.getInstance().getSerialRequestQueue().add( request );
                }
            } ) ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put( "Accept", "application/json" );
                    params.put( "Authorization", String.format( "Basic %s", url ) );
                    return params;
                }


            };
            ((DefaultErrorListener) jsonObjReq.getErrorListener()).setRequest( jsonObjReq );// 'this' is only accessible after super ctor is called
            // Set timeout=5s & retries = 2 & back off multiplier = 2
            jsonObjReq.setRetryPolicy( new DefaultRetryPolicy( 15000, 2, 2 ) );

            // Adding request to request queue
            jsonObjReq.setTag( "LoginManager" );
            AppController.getInstance().getSerialRequestQueue().add( jsonObjReq );

        }

}


