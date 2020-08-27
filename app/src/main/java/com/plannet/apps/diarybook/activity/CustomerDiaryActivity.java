package com.plannet.apps.diarybook.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.adapters.CustomerDiaryAdapter;
import com.plannet.apps.diarybook.adapters.ProductListAdapter;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;

import java.util.ArrayList;
import java.util.List;

public class CustomerDiaryActivity extends AppCompatActivity {
    String[] pCategories = { "Tiles", "Sanitaries", "Other"};
    Spinner category;
    Button save,products;
    EditText details;
    RecyclerView productDetails;
    String selectedCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custromer_diary);
        initUi();
        initDb();

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedCategory=pCategories[position];
                Toast.makeText(getApplicationContext(),pCategories[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilogue();
            }
        });
    }

    private void initUi() {
        category=(Spinner)findViewById(R.id.category);
        save=(Button)findViewById(R.id.save);
        details=(EditText)findViewById(R.id.details);
        products=(Button)findViewById(R.id.products);
        productDetails=(RecyclerView)findViewById(R.id.product_details);

    }

    private void initDb() {

    }

    private void dilogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. product_detais_list, null);
        builder.setView(dialogView);

        final RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.productList);
        CustomerDiaryLineModel lineModel=new CustomerDiaryLineModel();
        List<CustomerDiaryLineModel>lineModelList=new ArrayList<>();
        lineModel.setProduct_name("Tiles");
        lineModelList.add(lineModel);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecorator(getApplicationContext()));
        ProductListAdapter productListAdapter = new ProductListAdapter(lineModelList, new OnCompleteCallBack() {
            @Override
            public void onCompleteCallBack(Object data) {
                CustomerDiaryLineModel customerDiaryLineModel=new CustomerDiaryLineModel();
                if (data instanceof CustomerDiaryLineModel)
                    customerDiaryLineModel= (CustomerDiaryLineModel) data;
                detailsDilogue(customerDiaryLineModel);
            }
        });
        recyclerView.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void detailsDilogue(CustomerDiaryLineModel customerDiaryLineModel){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. product_detais_data, null);
        builder.setView(dialogView);

        CustomerDiaryLineModel lineModel=new CustomerDiaryLineModel();
        List<CustomerDiaryLineModel>lineModelList=new ArrayList<>();
        lineModel.setProduct_name("Tiles");
        lineModelList.add(lineModel);

//        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//            }
//        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}