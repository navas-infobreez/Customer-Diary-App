package com.plannet.apps.diarybook.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.ErrorMsg;
import com.plannet.apps.diarybook.LoginManager;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.OnCompleteCallback;
import com.plannet.apps.diarybook.Preference;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.SyncManager.DefaultErrorListener;
import com.plannet.apps.diarybook.SyncManager.DiaryBookJsonObjectRequest;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.SyncManager.JsonRequest;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.forms.CustomerSearchActivity;
import com.plannet.apps.diarybook.forms.UserCreationActivity;
import com.plannet.apps.diarybook.models.Authentication;
import com.plannet.apps.diarybook.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {
    Button submit;
    EditText user_name, pass_word;
    User user;
    ProgressDialog progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        user = new User( getApplicationContext() );
        submit = (Button) findViewById( R.id.login );
        user_name = (EditText) findViewById( R.id.username );
        pass_word = (EditText) findViewById( R.id.password );
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getAllUsers();
                progressBar = new ProgressDialog(LoginActivity.this);
                progressBar.setCancelable(true);//you can cancel it by pressing back button
                progressBar.setMessage(" Auathenticating ...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);//initially progress is 0
                progressBar.setMax(100);//sets the maximum value 100
                progressBar.show();//displays t

                String username = user_name.getText().toString();
                final UserModel userModel = user.selectUser( username );
//                if (userModel != null) {
//                    if (pass_word.getText().toString().toLowerCase().equals( userModel.getPassword().toLowerCase() )) {
//                        if (userModel.getRole_name().equalsIgnoreCase( "Reception" )) {
//                            Intent intent = new Intent( LoginActivity.this, CustomerSearchActivity.class );
//                            intent.putExtra( "role_name", userModel.getRole_name() );
//                            LoginActivity.this.startActivity( intent );
//                        } else if (userModel.getRole_name().equalsIgnoreCase( "Sales Man" )) {
//                            Intent intent = new Intent( LoginActivity.this, MainActivity.class );
//                            intent.putExtra( "role_name", userModel.getRole_name() );
//                            LoginActivity.this.startActivity( intent );
//                        } else if (userModel.getRole_name().equalsIgnoreCase( "Manager" )) {
//                            Intent intent = new Intent( LoginActivity.this, MainActivity.class );
//                            intent.putExtra( "role_name", userModel.getRole_name() );
//                            LoginActivity.this.startActivity( intent );
//                        }
//                        AppController.getInstance().setLoggedUser( userModel );
//                    } else {
//                        pass_word.setError( "Invalid Password" );
//                    }
//                } else {
//                    Toast.makeText( getApplicationContext(), "User Not Exsists", Toast.LENGTH_SHORT ).show();
//                }
                // final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
                // progressDialog.show();
                if (AppController.getInstance().isAuthenticated()) {
                    login(  userModel );
                } else {
                    final LoginManager loginManager = new LoginManager();
                    loginManager.login( new OnCompleteCallback() {
                        @Override
                        public void onError(Exception error) {
                            AppController.getInstance().setAuthToken( null );
                            Toast.makeText( getApplicationContext(), "User Not Exsists", Toast.LENGTH_SHORT ).show();
                        }

                        @Override
                        public void onComplete() {
                            AppController.getInstance().setAuthToken(loginManager.getAuthModel());
                            login( userModel );

                        }
                    } ); //
                }
            }
        } );
    }

    private void login(UserModel userModel) {

        //progressDialog.dismiss();
        if (userModel.getRole_name().equalsIgnoreCase( "Reception" )) {
            Intent intent = new Intent( LoginActivity.this, CustomerSearchActivity.class );
            intent.putExtra( "role_name", userModel.getRole_name() );
            LoginActivity.this.startActivity( intent );
        } else if (userModel.getRole_name().equalsIgnoreCase( "Sales Man" )||userModel.getRole_name().equalsIgnoreCase( "Accountant" )) {
            Intent intent = new Intent( LoginActivity.this, MainActivity.class );
            intent.putExtra( "role_name", userModel.getRole_name() );
            LoginActivity.this.startActivity( intent );
        } else if (userModel.getRole_name().equalsIgnoreCase( "Manager" )|| userModel.getRole_name().equalsIgnoreCase( "ADMIN" )) {
            Intent intent = new Intent( LoginActivity.this, MainActivity.class );
            intent.putExtra( "role_name", userModel.getRole_name() );
            LoginActivity.this.startActivity( intent );
        }

        AppController.getInstance().setLoggedUser( userModel );
        progressBar.dismiss();
    }


    public void makeLfogin() {
        final String url = "https://planet-customerdiary.herokuapp.com/user/getalluserdetails";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest(this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Log.d("Response", response.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });

        AppController.getInstance().submitServerRequest(req,"submitShipmet");
    }
}

