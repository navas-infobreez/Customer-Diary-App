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
import android.view.View;
import android.widget.EditText;

import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.adapters.ProductListAdapter;
import com.plannet.apps.diarybook.adapters.UserListAdapter;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.forms.CreateProductsActivity;
import com.plannet.apps.diarybook.forms.UserCreationActivity;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class ActivityProductList extends AppCompatActivity implements Callback {
    EditText searchBox;
    RecyclerView userRecycler;
    CardView searchView;
    boolean isSearchVisible;
    Products products;
    List<ProductModel> productModels=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        initDb();
        initUi();

    }
    private void initUi() {
        searchBox=(EditText) findViewById(R.id.search);
        userRecycler=(RecyclerView)findViewById(R.id.recyclerDiary);
        searchView = (CardView)findViewById(R.id.cardview);
        productModels=products.selectAll();
        setAdapters(productModels);
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
        products=new Products(this);

    }
    private void setAdapters(List<ProductModel> productModels) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        userRecycler.setLayoutManager(mLayoutManager);
        userRecycler.setItemAnimator(new DefaultItemAnimator());
        userRecycler.addItemDecoration(new ItemDecorator(getApplicationContext()));
        if (productModels!=null&& productModels.size()>0) {
           ProductDetailsListAdapter userListAdapter = new ProductDetailsListAdapter( productModels, this,this );
            userRecycler.setAdapter( userListAdapter );
            userListAdapter.notifyDataSetChanged();
        }
    }

    public void search(String text) {
        List<ProductModel> filteredList;
        if (productModels == null || productModels.isEmpty()) {
            return;
        }

        if (text == null || text.isEmpty()) {
            filteredList = productModels;
        } else {
            List<ProductModel> temp = new ArrayList<>();
            for (ProductModel productModel : productModels) {
                if (productModel.getSearchKey()!=null&&!productModel.getSearchKey().equals("")&&!productModel.getSearchKey().isEmpty()) {
                    if (productModel.getProduct_name() != null && productModel.getProduct_name().toLowerCase().contains(text.toLowerCase()) ||
                            productModel.getSearchKey()!= null && productModel.getSearchKey().toLowerCase().contains(text.toLowerCase())) {
                        temp.add(productModel);
                    }
                }else {
                    if (productModel.getProduct_name() != null && productModel.getProduct_name().toLowerCase().contains(text.toLowerCase())) {
                        temp.add(productModel);
                    }
                }
            }
            filteredList = temp;
        }

        setAdapters( filteredList );

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

        }else if (i == R.id.add){
            Intent intent1 = new Intent(ActivityProductList.this, CreateProductsActivity.class);
            startActivity(intent1);
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onItemClick(Object object) {
        ProductModel productModel=new ProductModel();
        if (object instanceof ProductModel)
            productModel = (ProductModel) object;
        Intent intent = new Intent(ActivityProductList.this, CreateProductsActivity.class);
        intent.putExtra("productId",productModel.getProduct_id());
        intent.putExtra("isEdit",true);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Object object) {

    }
}