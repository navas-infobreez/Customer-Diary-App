package com.plannet.apps.diarybook.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.plannet.apps.diarybook.R;


public class CustomerSearchActivity extends AppCompatActivity {
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);
        initUi();
    }

    private void initUi() {
        submit=(Button)findViewById(R.id.search_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ReceptionForm.class);
                startActivity(intent);
            }
        });
    }
}