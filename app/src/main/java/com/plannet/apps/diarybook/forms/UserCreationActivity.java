package com.plannet.apps.diarybook.forms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.SyncManager.DiaryBookJsonObjectRequest;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.activity.CustomerDiaryActivity;
import com.plannet.apps.diarybook.activity.ItemDecorator;
import com.plannet.apps.diarybook.adapters.ProductListAdapter;
import com.plannet.apps.diarybook.databases.Role;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.models.UserContacts;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserCreationActivity extends AppCompatActivity {
    Button addUser,addRoll;
    EditText name,password,confirmPassword,userName;
    String roll;
    Spinner rollSpinner;
    String[] rolls = { "Sales man", "Manager", "Reception", "Accountant", "Other"};
    User userDb;
    Role roleDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucer_crestion);
        initUi();
        initDb();
        getRoll();
    }

    private void initDb() {
        userDb=new User(getApplicationContext());
        roleDb=new Role(getApplicationContext());
    }

    private void initUi() {
        name=(EditText)findViewById(R.id.name);
        userName=(EditText)findViewById(R.id.user_name);
        password=(EditText)findViewById(R.id.password);
        confirmPassword=(EditText)findViewById(R.id.confirm_password);
        rollSpinner=(Spinner) findViewById(R.id.roll);
        addUser=(Button)findViewById(R.id.add_button);
        addRoll=(Button)findViewById(R.id.add_roll);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();


            }
        });
        rollSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                roll=rolls[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        addRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRollDialogue();
            }
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,rolls);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        rollSpinner.setAdapter(aa);
    }

    private void getData() {
        if (name.getText().toString().isEmpty()||name.getText().toString().equals("")){
            name.setError("enter a name");
        }else if(userName.getText().toString().isEmpty()||userName.getText().toString().equals("")){
            userName.setError("enter a user name");
        } else if (password.getText().toString().isEmpty()||password.getText().toString().equals("")){
            password.setError("set password");
        }else if (confirmPassword.getText().toString().isEmpty()||confirmPassword.getText().toString().equals("")){
            confirmPassword.setError("confirm your password");
        }else if (password.getText().toString().equals(confirmPassword.getText().toString())){
            saveData();
        }
    }

    private void saveData() {
        List<UserModel>userModelList=new ArrayList<>();
        UserModel userModel=new UserModel();
        userModel.setName(name.getText().toString());
        userModel.setRole_name(roll);
        userModel.setPassword(password.getText().toString());
        userModel.setConfirmPassword(confirmPassword.getText().toString());
        userModel.setUserName(userName.getText().toString());
        UserContacts userContacts=new UserContacts();
        userContacts.setCity("Mukkam"  );
        userContacts.setAddress1("Mukkam"  );
        userContacts.setAddress2("Mukkam"  );
        userContacts.setEmail("Mukkam@gmail.com" );
        userContacts.setPhoneNumber("5053265531" );
        userContacts.setCountry( "India" );
        userModel.setUserContacts( userContacts );

        userModelList.add(userModel);
        userDb.insertUser(userModelList);
        syncUser(userModel);
        clear();
    }

    private void syncUser(UserModel userModel) {

       // https://planet-customerdiary.herokuapp.com/user/createorupdateuser  post

        final String url ="https://planet-customerdiary.herokuapp.com/user/createorupdateuser";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, formatter.toUserJson(userModel),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitUser" );
    }

    private void getRoll() {
        final String url = " https://planet-customerdiary.herokuapp.com/userrole/getalluserrole";
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Gson gson = new Gson();
                            RoleModel roleModel=gson.fromJson(response.toString(),RoleModel.class);
                            List<RoleModel>roleModels=new ArrayList<>();
                            roleModels.add(roleModel);
                            roleDb.insertRole(roleModels);
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d( "error Response", error.toString() );
            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitRoll" );
    }

    private void synRoll(RoleModel roleModel) {
        final String url = "https://planet-customerdiary.herokuapp.com/userrole/createorupdateuserrole";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, formatter.RollCreationJson(roleModel),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d( "error Response", error.toString() );
            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitRoll" );
    }

    private void addRollDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Product");
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. add_roll_activity, null);
        builder.setView(dialogView);
        final EditText rollName=(EditText)dialogView.findViewById(R.id.roll_name);
        final EditText description=(EditText)dialogView.findViewById(R.id.description);
        final CheckBox isActive=(CheckBox)dialogView.findViewById(R.id.isActive);
        Button submit=(Button)dialogView.findViewById(R.id.add_button);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RoleModel roleModel=new RoleModel();
                if (rollName.getText().toString().isEmpty()||rollName.getText().toString().equals("")){
                    rollName.setError("please enter a roll name");
                }else if (description.getText().toString().equals("")||description.getText().toString().isEmpty()){
                    description.setError("please enter a description");
                }else {
                    roleModel.setRoleName(rollName.getText().toString());
                    roleModel.setDescription(description.getText().toString());
                    roleModel.setActive(isActive.isChecked());
                    roleModel.setPriority(1);
                    synRoll(roleModel);
                }

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void clear() {
        name.getText().clear();
        userName.getText().clear();
        password.getText().clear();
        confirmPassword.getText().clear();

    }
}