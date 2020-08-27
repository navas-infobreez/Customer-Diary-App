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

import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.adapters.CustomerDiaryAdapter;
import com.plannet.apps.diarybook.databases.Customer;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.forms.UserCreationActivity;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class PendingDiaryFragment extends Fragment implements Callback,OnCompleteCallBack {
    public static final String PENDING = "PENDING";
    public static final String PICKED = "PICKED";
    public static final String COMPLETED = "COMPLETED";
    public static final String APPROVED = "APPROVED";
    public static final String APPROVERETURN = "APPROVE RETURN";

    RecyclerView recyclerView;
    CustomerDiaryAdapter customerDiaryAdapter;
    List<CustomerDiaryModel> customerDiaryModels=new ArrayList<>(  );
    CustomerDiaryDao customerDiaryDao;
    String selected_status=PENDING;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_main, container, false);
        initui(view);
        initDb();
//        refreshview();
        iniView();
        return view;
    }

    private void iniView() {
        List<CustomerDiaryModel> customerDiaryModels = new ArrayList<>(  );
        customerDiaryModels=customerDiaryDao.getAll();
        customerDiaryAdapter = new CustomerDiaryAdapter(customerDiaryModels,this,this);
        recyclerView.setAdapter(customerDiaryAdapter);
        customerDiaryAdapter.notifyDataSetChanged();
    }

    private void refreshview() {
        List<CustomerDiaryModel> customerDiaryModels = new ArrayList<>(  );
        customerDiaryModels=customerDiaryDao.getAll( selected_status );
//        if (customerDiaryModels==null||customerDiaryModels.size()==0) {
//            CustomerDiaryModel customerDiaryModel =new CustomerDiaryModel();
//            customerDiaryModel.setCustomerName( "adv.Salmaan Pulli" );
//            customerDiaryModel.setDate( "25/07/2020" );
//            customerDiaryModel.setCustomerPhone("8606522615");
//            customerDiaryModel.setCustomerAddress("G Road");
//            customerDiaryModel.setStatus( PENDING );
//            customerDiaryModels.add( customerDiaryModel );
//            CustomerDiaryModel customerDiaryModel1 = new CustomerDiaryModel();
//            customerDiaryModel1.setCustomerName( "Amjas Ams" );
//            customerDiaryModel1.setDate( "28/10/2020" );
//            customerDiaryModel.setCustomerPhone("8606522615");
//            customerDiaryModel.setCustomerAddress("G Road");
//            customerDiaryModel1.setStatus( PICKED );
//            customerDiaryModels.add( customerDiaryModel1 );
//            CustomerDiaryModel customerDiaryModel2 = new CustomerDiaryModel();
//            customerDiaryModel2.setCustomerName( "Nazal KK" );
//            customerDiaryModel2.setDate( "25/01/2020" );
//            customerDiaryModel.setCustomerPhone("8606522615");
//            customerDiaryModel.setCustomerAddress("G Road");
//            customerDiaryModel2.setStatus( COMPLETED );
//            customerDiaryModels.add( customerDiaryModel2 );
//            CustomerDiaryModel customerDiaryModel3 = new CustomerDiaryModel();
//            customerDiaryModel3.setCustomerName( "Lukuman luku" );
//            customerDiaryModel3.setDate( "25/07/2020" );
//            customerDiaryModel.setCustomerPhone("8606522615");
//            customerDiaryModel.setCustomerAddress("G Road");
//            customerDiaryModel3.setStatus( APPROVED );
//            customerDiaryModels.add( customerDiaryModel3 );
//            CustomerDiaryModel customerDiaryModel4 = new CustomerDiaryModel();
//            customerDiaryModel4.setCustomerName( "Dilshad" );
//            customerDiaryModel.setCustomerPhone("8606522615");
//            customerDiaryModel.setCustomerAddress("G Road");
//            customerDiaryModel4.setDate( "25/07/2020" );
//            customerDiaryModel4.setStatus( APPROVERETURN );
//            customerDiaryModels.add( customerDiaryModel4 );
//            customerDiaryDao.insertCustomerDiary( customerDiaryModels );
//        }

        customerDiaryAdapter = new CustomerDiaryAdapter(customerDiaryModels,this,this);
        recyclerView.setAdapter(customerDiaryAdapter);
        customerDiaryAdapter.notifyDataSetChanged();
    }

    private void initui(View view) {
        Spinner spinner=(Spinner)view.findViewById( R.id.spinner );
        recyclerView=(RecyclerView)view.findViewById( R.id.recyclerDiary );
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecorator(getContext()));


        int defualtSelection=0;
        final ArrayList<String> statusList= new ArrayList<>();

        statusList.add(PENDING);
        statusList.add(PICKED);
        statusList.add(COMPLETED);
        statusList.add(APPROVED);
        statusList.add(APPROVERETURN);



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
            intent.putExtra("diaryId",customerDiaryModel.getId());
            startActivity(intent);
        }

    }

    @Override
    public void onItemClick(Object object) {
        CustomerDiaryModel customerDiaryModel=(CustomerDiaryModel) object;
        customerDiaryDao.updateStatus(customerDiaryModel.getId(),customerDiaryModel.getStatus());
        refreshview();
    }

    @Override
    public void onItemLongClick(Object object) {

    }
}
