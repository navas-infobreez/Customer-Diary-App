package com.plannet.apps.diarybook.forms;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.plannet.apps.diarybook.R;

public class UcerCrestionActivity extends AppCompatActivity {
    Button addUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ucer_crestion);
        initUi();
    }

    private void initUi() {
        addUser=(Button)findViewById(R.id.add_button);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}