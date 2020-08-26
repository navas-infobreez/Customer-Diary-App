package com.plannet.apps.diarybook.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.Preference;
import com.plannet.apps.diarybook.R;


public class LoginActivity extends AppCompatActivity {
    Button submit;
    EditText user_name, pass_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        submit = (Button) findViewById( R.id.login );
        user_name = (EditText) findViewById( R.id.username );
        pass_word = (EditText) findViewById( R.id.password );
         submit.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (user_name.getText().toString().equals( Preference.getUserName())&&
                         (pass_word.getText().toString().equals(Preference.getPassword()))){
                     Intent intent = new Intent(LoginActivity.this, MainActivity.class );
                     LoginActivity.this.startActivity(intent);
                 }else {
                     pass_word.setError("Invalid Password");
                 }
             }
         } );

    }
}