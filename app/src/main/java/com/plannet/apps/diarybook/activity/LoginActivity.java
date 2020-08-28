package com.plannet.apps.diarybook.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.Preference;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.forms.CustomerSearchActivity;
import com.plannet.apps.diarybook.forms.UserCreationActivity;
import com.plannet.apps.diarybook.models.UserModel;


public class LoginActivity extends AppCompatActivity {
    Button submit;
    EditText user_name, pass_word;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
         user=new User( getApplicationContext() );
        submit = (Button) findViewById( R.id.login );
        user_name = (EditText) findViewById( R.id.username );
        pass_word = (EditText) findViewById( R.id.password );
        submit.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String username=user_name.getText().toString();
                 UserModel userModel=user.selectUser(username);
                 if (userModel!=null) {
                     if (pass_word.getText().toString().toLowerCase().equals( userModel.getPassword().toLowerCase() )) {
                          if (userModel.getRole_name().equalsIgnoreCase( "Reception" )) {
                              Intent intent = new Intent( LoginActivity.this, CustomerSearchActivity.class );
                              intent.putExtra( "role_name", userModel.getRole_name() );
                              LoginActivity.this.startActivity( intent );
                          }else if (userModel.getRole_name().equalsIgnoreCase( "Sales Man" )){
                              Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                              intent.putExtra( "role_name", userModel.getRole_name() );
                              LoginActivity.this.startActivity( intent );
                          }else if (userModel.getRole_name().equalsIgnoreCase( "Manager" )) {
                              Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                              intent.putExtra( "role_name", userModel.getRole_name() );
                              LoginActivity.this.startActivity( intent );
                          }
                          AppController.getInstance().setLoggedUser( userModel );
                     } else {
                         pass_word.setError( "Invalid Password" );
                     }
                 }else {
                     Toast.makeText( getApplicationContext(),"User Not Exsists",Toast.LENGTH_SHORT ).show();
                 }
             }
         } );

    }
}