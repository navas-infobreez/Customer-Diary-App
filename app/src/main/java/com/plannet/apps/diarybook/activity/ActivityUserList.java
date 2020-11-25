package com.plannet.apps.diarybook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.adapters.CustomerDiaryAdapter;
import com.plannet.apps.diarybook.adapters.UserListAdapter;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.forms.UserCreationActivity;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class ActivityUserList extends AppCompatActivity implements Callback {
    EditText searchBox;
    RecyclerView userRecycler;
    CardView searchView;
    boolean isSearchVisible;
    User userDb;
    List<UserModel>userModelList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        initDb();
        initUi();
    }

    private void initUi() {
        searchBox=(EditText) findViewById(R.id.search);
        userRecycler=(RecyclerView)findViewById(R.id.recyclerDiary);
        searchView = (CardView)findViewById(R.id.cardview);
        userModelList=userDb.getAllUser();
        setAdapters(userModelList);
        isSearchVisible=false;
        searchView.setVisibility(View.GONE);


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                search( s.toString() );

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDb() {
        userDb=new User(this);

    }
    private void setAdapters(List<UserModel> customerModels) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        userRecycler.setLayoutManager(mLayoutManager);
        userRecycler.setItemAnimator(new DefaultItemAnimator());
        userRecycler.addItemDecoration(new ItemDecorator(getApplicationContext()));
        if (customerModels!=null&& customerModels.size()>0) {
            UserListAdapter userListAdapter = new UserListAdapter( userModelList, this );
            userRecycler.setAdapter( userListAdapter );
            userListAdapter.notifyDataSetChanged();
        }
    }


    public void search(String text) {
        List<UserModel> filteredList;
        if (userModelList == null || userModelList.isEmpty()) {
            return;
        }

        if (text == null || text.isEmpty()) {
            filteredList = userModelList;
        } else {
            List<UserModel> temp = new ArrayList<>();
            for (UserModel userModel : userModelList) {
                if (userModel.getUserContacts()!=null) {
                    if (userModel.getRole_name() != null && userModel.getRole_name().toLowerCase().contains(text.toLowerCase()) ||
                            userModel.getUserContacts().getPhoneNumber() != null && userModel.getUserContacts().getPhoneNumber().toLowerCase().contains(text.toLowerCase())) {
                        temp.add(userModel);
                    }
                }else {
                    if (userModel.getRole_name() != null && userModel.getRole_name().toLowerCase().contains(text.toLowerCase())) {
                        temp.add(userModel);
                    }
                }
            }
            filteredList = temp;
        }

        setAdapters( filteredList );

    }

    @Override
    public void onItemClick(Object object) {
        UserModel userModel=new UserModel();
        if (object instanceof UserModel)
            userModel = (UserModel) object;
        Intent intent = new Intent(ActivityUserList.this, UserCreationActivity.class);
        intent.putExtra("userId",userModel.getRole_id());
        intent.putExtra("isEdit",true);
        startActivity(intent);


    }

    @Override
    public void onItemLongClick(Object object) {

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.search_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.search) {
            if (isSearchVisible) {
                searchView.setVisibility(View.GONE);
                isSearchVisible=false;
            }else{
                searchView.setVisibility(View.VISIBLE);
                isSearchVisible=true;
            }

        }else   if (i == R.id.add) {
            Intent intent1 = new Intent(ActivityUserList.this, UserCreationActivity.class);
            startActivity(intent1);
        }
        return super.onOptionsItemSelected( item );
    }
}