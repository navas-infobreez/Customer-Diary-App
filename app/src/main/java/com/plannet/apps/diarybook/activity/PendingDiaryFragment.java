package com.plannet.apps.diarybook.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.adapters.CustomerDiaryAdapter;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;

import java.util.ArrayList;
import java.util.List;

public class PendingDiaryFragment extends Fragment {

    RecyclerView recyclerView;
    CustomerDiaryAdapter customerDiaryAdapter;
    List<CustomerDiaryModel> customerDiaryModels=new ArrayList<>(  );
    CustomerDiaryDao customerDiaryDao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_main, container, false);
        initui(view);
        initDb();
        refreshview();
        return view;
    }

    private void refreshview() {
        CustomerDiaryModel customerDiaryModel=new CustomerDiaryModel();
        customerDiaryModel.setCustomerName( "adv.Salmaan Pulli" );
        customerDiaryModel.setDate( "25/07/2020" );
        customerDiaryModels.add( customerDiaryModel );
        CustomerDiaryModel customerDiaryModel1=new CustomerDiaryModel();
        customerDiaryModel1.setCustomerName( "Amjas Ams" );
        customerDiaryModel1.setDate( "28/10/2020" );
        customerDiaryModels.add( customerDiaryModel1 );
        CustomerDiaryModel customerDiaryModel2=new CustomerDiaryModel();
        customerDiaryModel2.setCustomerName( "Nazal KK" );
        customerDiaryModel2.setDate( "25/01/2020" );
        customerDiaryModels.add( customerDiaryModel2 );
        CustomerDiaryModel customerDiaryModel3=new CustomerDiaryModel();
        customerDiaryModel3.setCustomerName( "Lukuman luku" );
        customerDiaryModel3.setDate( "25/07/2020" );
        customerDiaryModels.add( customerDiaryModel3 );

        customerDiaryAdapter = new CustomerDiaryAdapter(customerDiaryModels);
        recyclerView.setAdapter(customerDiaryAdapter);
        customerDiaryAdapter.notifyDataSetChanged();
    }

    private void initui(View view) {
        recyclerView=(RecyclerView)view.findViewById( R.id.recyclerDiary );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecorator(getContext()));
    }

    private void initDb() {
        customerDiaryDao=new CustomerDiaryDao( getContext() );
    }
}
