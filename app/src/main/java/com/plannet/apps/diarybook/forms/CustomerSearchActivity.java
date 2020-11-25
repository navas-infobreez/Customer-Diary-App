package com.plannet.apps.diarybook.forms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.activity.CustomerListFragment;


public class CustomerSearchActivity extends AppCompatActivity {
    Button submit;
    CustomerListFragment customerListFragment;
    EditText search;
    CardView cardView;
    private boolean isSearchVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        initUi();
        customerListFragment=new CustomerListFragment();
        setCustomerListFragment(customerListFragment  );
    }

    private void refreshview() {
        if (customerListFragment!=null){
            customerListFragment.refreshview();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        for(int i = 0; i < menu.size(); i++) {
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.search) {
            if (isSearchVisible) {
                cardView.setVisibility(View.GONE);
                isSearchVisible=false;
            }else{
                cardView.setVisibility(View.VISIBLE);
                isSearchVisible=true;
            }

        }else if (i == R.id.add) {
            Intent intent = new Intent( CustomerSearchActivity.this, ReceptionForm.class );
            intent.putExtra( "isEdit",false );
            intent.putExtra( "customerId",false );
            CustomerSearchActivity.this.startActivity( intent );
        }

        return super.onOptionsItemSelected( item );
    }
    private void initUi() {
        cardView=(CardView)findViewById(R.id.cardview);
        search=(EditText) findViewById(R.id.search);
        isSearchVisible=false;
        cardView.setVisibility(View.GONE);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                customerListFragment.search( s.toString() );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        refreshview();
        super.onResume();
    }

    public void setCustomerListFragment(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.customerList_layout, fragment);
        fragmentTransaction.commit();
    }

}