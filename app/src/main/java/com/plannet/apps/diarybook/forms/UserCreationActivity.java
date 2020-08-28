package com.plannet.apps.diarybook.forms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.activity.CustomerDiaryActivity;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserCreationActivity extends AppCompatActivity {
    Button addUser;
    EditText name,password,confirmPassword,userName;
    String roll;
    Spinner rollSpinner;
    String[] rolls = { "Sales man", "Manager", "Reception", "Accountant", "Other"};
    User userDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucer_crestion);
        initUi();
        initDb();
    }

    private void initDb() {
        userDb=new User(getApplicationContext());
    }

    private void initUi() {
        name=(EditText)findViewById(R.id.name);
        userName=(EditText)findViewById(R.id.user_name);
        password=(EditText)findViewById(R.id.password);
        confirmPassword=(EditText)findViewById(R.id.confirm_password);
        rollSpinner=(Spinner) findViewById(R.id.roll);
        addUser=(Button)findViewById(R.id.add_button);
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
        userModelList.add(userModel);
        userDb.insertUser(userModelList);
        clear();
    }

    private void clear() {
        name.getText().clear();
        userName.getText().clear();
        password.getText().clear();
        confirmPassword.getText().clear();

    }
}