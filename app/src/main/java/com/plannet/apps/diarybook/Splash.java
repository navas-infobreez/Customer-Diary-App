package com.plannet.apps.diarybook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.plannet.apps.diarybook.activity.LoginActivity;

public class Splash extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main );
        AppController.getInstance();
        Thread timer = new Thread(){
            public void run(){

                try{
                    sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{


                    Intent openMainActivity = new Intent(Splash.this, LoginActivity.class);
                    startActivity(openMainActivity);
                    finish();
                }
            }
        };
        timer.start();
    }



        @Override
        protected void onPause() {
            super.onPause();
            //finish(); // Causing dialog to leak
        }

    }
