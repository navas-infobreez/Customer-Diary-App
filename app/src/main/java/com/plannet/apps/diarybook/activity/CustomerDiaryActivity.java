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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.adapters.CustomerDiaryAdapter;
import com.plannet.apps.diarybook.adapters.DiaryLineAdapter;
import com.plannet.apps.diarybook.adapters.ProductListAdapter;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.databases.CustomerDiaryLinesDao;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.utils.CommonUtils;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;

import java.util.ArrayList;
import java.util.List;

public class CustomerDiaryActivity extends AppCompatActivity {
    String[] pCategories = {"Tiles", "Ceramic", "Sanitiser"};
    Spinner category;
    Button save,products;
    EditText details;
    RecyclerView productDetails;
    TextView customerName,customerAddress,customerPhone;
    String selectedCategory;
    Products productsDb;
    int diaryId;
    CustomerDiaryModel selectedCustomerDiary=new CustomerDiaryModel();
    CustomerDiaryDao customerDiaryDao;
    CustomerDiaryLinesDao customerDiaryLinesDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custromer_diary);
        diaryId= getIntent().getExtras().getInt("diaryId");
        initDb();
        initUi();

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,pCategories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        category.setAdapter(aa);
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
                dialogue();
            }
        });
        lineRefresh();
    }

    private void initUi() {
        category=(Spinner)findViewById(R.id.category);
        save=(Button)findViewById(R.id.save);
        details=(EditText)findViewById(R.id.details);
        products=(Button)findViewById(R.id.products);
        productDetails=(RecyclerView)findViewById(R.id.product_details);
        customerName=(TextView)findViewById(R.id.customerName);
        customerAddress=(TextView)findViewById(R.id.customerAddress);
        customerPhone=(TextView)findViewById(R.id.customerPhone);

        customerName.setText(selectedCustomerDiary.getCustomerName());
        customerPhone.setText("Phone : "+selectedCustomerDiary.getCustomerPhone());
        customerAddress.setText("Place  : "+selectedCustomerDiary.getCustomerAddress());

    }

    private void initDb() {
        productsDb = new Products(getApplicationContext());
        customerDiaryLinesDao = new CustomerDiaryLinesDao(getApplicationContext());
        customerDiaryDao = new CustomerDiaryDao(getApplicationContext());
        selectedCustomerDiary=customerDiaryDao.getAll(diaryId);
    }

    private void dialogue(){
        List<ProductModel>productModels=productsDb.selectAllByCategory(selectedCategory);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Product");
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. product_detais_list, null);
        builder.setView(dialogView);

        final RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.productList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecorator(getApplicationContext()));
        ProductListAdapter productListAdapter = new ProductListAdapter(productModels, new OnCompleteCallBack() {
            @Override
            public void onCompleteCallBack(Object data) {
                ProductModel productModel=new ProductModel();
                if (data instanceof ProductModel)
                    productModel= (ProductModel) data;
                detailsDialogue(productModel);
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

    private void detailsDialogue(final ProductModel productsModel){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. product_detais_data, null);
        builder.setView(dialogView);

        final EditText sqrft,details;
        TextView name,amount;
        Button save;
        sqrft=(EditText)dialogView.findViewById(R.id.sqrft);
        details=(EditText)dialogView.findViewById(R.id.details);
        name=(TextView) dialogView.findViewById(R.id.name);
        amount=(TextView) dialogView.findViewById(R.id.amount);
        save=(Button) dialogView.findViewById(R.id.save);

        name.setText(productsModel.getProduct_name());
        amount.setText(String.valueOf(productsModel.getSale_price()));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerDiaryLineModel customerDiaryLineModel=new CustomerDiaryLineModel();
                customerDiaryLineModel.setProduct_name(productsModel.getProduct_name());
                String qty=sqrft.getText().toString();
                customerDiaryLineModel.setQty(CommonUtils.toInt(qty));
                customerDiaryLineModel.setDetails(details.getText().toString());
                customerDiaryLineModel.setProduct_id(productsModel.getId());
                customerDiaryLineModel.setHeaderId(diaryId);
                saveLine(customerDiaryLineModel);
                sqrft.getText().clear();
                details.getText().clear();
                lineRefresh();
            }
        });

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

    private void lineRefresh() {
        List<CustomerDiaryLineModel>customerDiaryLineModelList=customerDiaryLinesDao.getAll(diaryId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        productDetails.setLayoutManager(mLayoutManager);
        productDetails.setItemAnimator(new DefaultItemAnimator());
        productDetails.addItemDecoration(new ItemDecorator(getApplicationContext()));
        DiaryLineAdapter diaryLineAdapter = new DiaryLineAdapter(customerDiaryLineModelList, new OnCompleteCallBack() {
            @Override
            public void onCompleteCallBack(Object data) {
                ProductModel productModel=new ProductModel();

            }
        });
        productDetails.setAdapter(diaryLineAdapter);
        diaryLineAdapter.notifyDataSetChanged();
    }

    private void saveLine(CustomerDiaryLineModel customerDiaryLineModel) {
        List<CustomerDiaryLineModel>customerDiaryLineModelList=new ArrayList<>();
        customerDiaryLineModelList.add(customerDiaryLineModel);
        customerDiaryLinesDao.insertCustomerDiaryLines(customerDiaryLineModelList);

    }
}