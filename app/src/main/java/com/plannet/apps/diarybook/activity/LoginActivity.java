package com.plannet.apps.diarybook.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;



public class LoginActivity extends AppCompatActivity {
    Button submit;
    EditText user_name, pass_word;
    User user;
    UserModel userModel=new UserModel();
    SweetAlertDialog sweetAlertDialog;


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
                String password = null,username = null;
                if (user_name.getText().toString().equals( "" )||user_name.getText().toString().isEmpty()){
                    user_name.setError( "Please enter username" );
                }else if (pass_word.getText().toString().equals( "" )||pass_word.getText().toString().isEmpty()){
                    pass_word.setError( "Please enter password" );
                }else {
                    password = pass_word.getText().toString();
                    username = user_name.getText().toString();
                    sweetAlertDialog=new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.PROGRESS_TYPE);
                    sweetAlertDialog .setTitleText("Login");
                    sweetAlertDialog.setContentText( "Please wait" );
                    sweetAlertDialog .show();
                }


                userModel = user.selectUser( username );

                if (AppController.getInstance().isAuthenticated()&&userModel!=null) {
                    login(  userModel );
                } else {
                    final LoginManager loginManager = new LoginManager();
                    loginManager.login( new OnCompleteCallback() {
                        @Override
                        public void onError(Exception error) {
                            AppController.getInstance().setAuthToken( null );
                            sweetAlertDialog.dismiss();
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Login Error..")
                                    .setContentText("User Not Found")
                                    .show();
                        }

                        @Override
                        public void onComplete() {
                            AppController.getInstance().setAuthToken(loginManager.getAuthModel());
                            login( userModel );
                            sweetAlertDialog.dismiss();

                        }

                        @Override
                        public void onComplete(Object object) {
                            if (object instanceof UserModel){
                                AppController.getInstance().setAuthToken(loginManager.getAuthModel());
                                List<UserModel>userModels= new ArrayList<>(  );
                                UserModel userModel1= (UserModel) object;
                                userModel1.setRole_name( userModel1.getRoles().get( 0 ).getRoleName() );//todo...
                                User userDb=new User( LoginActivity.this );
                                userDb.deleteUser(userModel1.getRole_id());
                                userModels.add( userModel1 );
                                userDb.insertUser(userModels);
                                userModel = user.selectUser( userModel1.getUserName() );
                                login( userModel );
                                sweetAlertDialog.dismiss();
                            }

                        }
                    },username,password);
                }
            }
        } );
    }

    private void login(UserModel userModel) {
        if (userModel!=null) {
            if (userModel.getRole_name().equalsIgnoreCase( "Reception" )) {
                Intent intent = new Intent( LoginActivity.this, CustomerSearchActivity.class );
                intent.putExtra( "role_name", userModel.getRole_name() );
                LoginActivity.this.startActivity( intent );
                sweetAlertDialog.dismiss();
            } else if (userModel.getRole_name().equalsIgnoreCase( "Sales Man" ) || userModel.getRole_name().equalsIgnoreCase( "Accountant" )) {
                Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                intent.putExtra( "role_name", userModel.getRole_name() );
                LoginActivity.this.startActivity( intent );
                sweetAlertDialog.dismiss();
            } else if (userModel.getRole_name().equalsIgnoreCase( "Manager" ) || userModel.getRole_name().equalsIgnoreCase( "ADMIN" )) {
                Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                intent.putExtra( "role_name", userModel.getRole_name() );
                LoginActivity.this.startActivity( intent );
                sweetAlertDialog.dismiss();
            }

            AppController.getInstance().setLoggedUser( userModel );
        }else {
            sweetAlertDialog.dismiss();
        }
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

