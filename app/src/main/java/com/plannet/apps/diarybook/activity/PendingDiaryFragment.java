package com.plannet.apps.diarybook.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.adapters.CustomerDiaryAdapter;
import com.plannet.apps.diarybook.databases.Customer;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.ArrayList;
import java.util.List;

import static com.plannet.apps.diarybook.DatabaseHandler.ALL;
import static com.plannet.apps.diarybook.DatabaseHandler.APPROVED;
import static com.plannet.apps.diarybook.DatabaseHandler.APPROVERETURN;
import static com.plannet.apps.diarybook.DatabaseHandler.COMPLETED;
import static com.plannet.apps.diarybook.DatabaseHandler.PENDING;
import static com.plannet.apps.diarybook.DatabaseHandler.PICKED;

public class PendingDiaryFragment extends Fragment implements Callback,OnCompleteCallBack {

    RecyclerView recyclerView;
    CustomerDiaryAdapter customerDiaryAdapter;
    private List<CustomerDiaryModel> customerDiaryModels=new ArrayList<>(  );
    CustomerDiaryDao customerDiaryDao;
    private String selected_status=ALL;
    private MainActivity activity;
    private boolean isPendingList;

    public PendingDiaryFragment(boolean b) {
        isPendingList=b;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_main, container, false);

        activity = (MainActivity) getActivity();
        initui(view);
        initDb();
        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Manager" )) {
            if (isPendingList) {
                selected_status = COMPLETED;
                refreshview();
            } else {
                selected_status = ALL;
                currentRefreshview();

            }
        }else  if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Sales man" )) {
            if (isPendingList) {
                selected_status = ALL;
                refreshview();
            } else {
                selected_status = ALL;
                currentRefreshview();
            }
        }

        return view;
    }

    private void currentRefreshview() {

        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Sales man" )){
            customerDiaryModels=customerDiaryDao.getCustomerCurrentDiary(AppController.getInstance().getLoggedUser().getId(),selected_status);
        }else {
            customerDiaryModels=customerDiaryDao.getCustomerDiary(selected_status);
        }

        setadaper();
    }

    private void refreshview() {

        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Sales man" )){
            customerDiaryModels=customerDiaryDao.getCustomerDiary(AppController.getInstance().getLoggedUser().getId(),selected_status);
        }else {
            customerDiaryModels=customerDiaryDao.getCustomerDiary(selected_status);
        }

        setadaper();
    }


    public void setadaper(){
        customerDiaryAdapter = new CustomerDiaryAdapter(customerDiaryModels,this,this);
        recyclerView.setAdapter(customerDiaryAdapter);
        customerDiaryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
      refreshview();
        super.onResume();
    }

    private void initui(View view) {
        Spinner spinner=(Spinner)view.findViewById( R.id.spinner );
        recyclerView=(RecyclerView)view.findViewById( R.id.recyclerDiary );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecorator(getContext()));


        BottomNavigationView bottom_navigation=(BottomNavigationView)view.findViewById( R.id.bottom_navigation );
        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Manager" )){
            bottom_navigation.setVisibility( View.VISIBLE );
        }else {
            bottom_navigation.setVisibility( View.GONE );
        }

        int defualtSelection=0;

        final ArrayList<String> statusList= new ArrayList<>();

        statusList.add(ALL);
        statusList.add(PENDING);
        statusList.add(PICKED);
        statusList.add(COMPLETED);
        statusList.add(APPROVED);
        statusList.add(APPROVERETURN);

        int i =0;
        for (String tuple:statusList) {

            if (tuple.equalsIgnoreCase( selected_status )) {
                defualtSelection = i;
            }
            i++;
        }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, statusList);
            spinner.setAdapter(adapter);
        spinner.setSelection( defualtSelection );
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selected_status =statusList.get(position);
                    refreshview();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

    private void initDb() {
        customerDiaryDao=new CustomerDiaryDao( getContext() );
    }

    @Override
    public void onCompleteCallBack(Object data) {
        if (data instanceof CustomerDiaryModel) {
            CustomerDiaryModel customerDiaryModel = (CustomerDiaryModel) data;
            Intent intent = new Intent(getActivity(), CustomerDiaryActivity.class );
            intent.putExtra("diaryId",customerDiaryModel.getDiaryId());
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(Object object) {
        CustomerDiaryModel customerDiaryModel=(CustomerDiaryModel) object;
        customerDiaryDao.updateStatus(customerDiaryModel.getDiaryId(),customerDiaryModel.getStatus(),AppController.getInstance().getLoggedUser().getId());

            Intent intent = new Intent(getActivity(), CustomerDiaryActivity.class );
            intent.putExtra("diaryId",customerDiaryModel.getDiaryId());
            startActivity(intent);

        refreshview();
    }

    @Override
    public void onItemLongClick(Object object) {

    }
}
