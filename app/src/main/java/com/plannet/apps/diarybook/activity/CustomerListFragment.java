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
import com.plannet.apps.diarybook.forms.ReceptionForm;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.ArrayList;
import java.util.List;

import static com.plannet.apps.diarybook.DatabaseHandler.PENDING;

public class CustomerListFragment extends Fragment implements Callback {
    public static final String PENDING = "PN";
    public static final String PICKED = "PK";
    public static final String COMPLETED = "CO";
    public static final String APPROVED = "AP";
    public static final String APPROVERETURN = "AR";

    RecyclerView recyclerView;
    CustomerDiaryAdapter customerDiaryAdapter;
    List<CustomerModel> customerModels = new ArrayList<>(  );
    Customer customer;
    String selected_status=PENDING;
    CustomerDiaryDao customerDiaryDao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_main, container, false);
        initui(view);
        initDb();
        refreshview();
        return view;
    }

    public void refreshview() {
        customerModels=customer.getAll();
        setAdapters( customerModels );
    }
    public void search(String text) {
        List<CustomerModel> filteredList;
        if (customerModels == null || customerModels.isEmpty()) {
            return;
        }

            if (text == null || text.isEmpty()) {
                filteredList = customerModels;
            } else {
                List<CustomerModel> temp = new ArrayList<>();
                for (CustomerModel productModel : customerModels) {
                    if (productModel.getPhone_no() != null && productModel.getPhone_no().toLowerCase().contains( text.toLowerCase() ) ||
                            productModel.getCustomerName() != null && productModel.getCustomerName().toLowerCase().contains( text.toLowerCase() )) {
                        temp.add( productModel );
                    }
                }
                filteredList = temp;
            }


        setAdapters( filteredList );

    }

    private void setAdapters(List<CustomerModel> customerModels) {
        if (customerModels!=null&& customerModels.size()>0) {
            customerDiaryAdapter = new CustomerDiaryAdapter( customerModels, true,this );
            recyclerView.setAdapter( customerDiaryAdapter );
            customerDiaryAdapter.notifyDataSetChanged();
        }
    }

    private void initui(View view) {
        Spinner spinner=(Spinner)view.findViewById( R.id.spinner );
        spinner.setVisibility( View.GONE );
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
        customer=new Customer( getContext() );
        customerDiaryDao=new CustomerDiaryDao( getContext() );
    }

    @Override
    public void onItemClick(Object object) {
        CustomerModel customerModel=(CustomerModel)object;
        Intent intent = new Intent(getActivity(), ReceptionForm.class );
        Bundle bundle=new Bundle(  );
        bundle.putBoolean( "IsEdit",true );
        intent.putExtras( bundle );
        intent.putExtra( "customerId",customerModel.getCustomerName() );
        startActivity(intent);
    }

    private void createNewDiary() {
        List<CustomerDiaryModel> customerDiaryModels=new ArrayList<>(  );
        CustomerDiaryModel customerDiaryModel=new CustomerDiaryModel();
        customerDiaryModel.setCustomerName(customerDiaryModel.getCustomerName().toString());
        customerDiaryModel.setCustomerAddress(customerDiaryModel.getCustomerAddress().toString());
        customerDiaryModel.setCustomerPhone(customerDiaryModel.getCustomerPhone().toString());
        customerDiaryModels.add(customerDiaryModel);
        customerDiaryModel.setStatus(PENDING);

        customerDiaryDao.insertCustomerDiary(customerDiaryModels);
    }

    @Override
    public void onItemLongClick(Object object) {

    }
}
