package com.plannet.apps.diarybook.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.activity.CustomerDiaryActivity;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateProductsActivity extends AppCompatActivity {
    Button submit;
    EditText product_name, code, amount, details;
    Spinner category_spinner;
    String[] categories = {"Tiles", "Ceramic", "Sanitiser"};
    Products products;
    String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_create_products );


        initUi();
        initDb();
    }

    private void initDb() {
        products = new Products( getApplicationContext() );
    }

    private void initUi() {
        product_name = (EditText) findViewById( R.id.name );
        code = (EditText) findViewById( R.id.product_code );
        amount = (EditText) findViewById( R.id.rate );
        details = (EditText) findViewById( R.id.details );
        category_spinner = (Spinner) findViewById( R.id.category_spinner );
        submit = (Button) findViewById( R.id.add_button );
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
//                        Intent intent = new Intent( CreateProductsActivity.this, CustomerDiaryActivity.class );
//                        startActivity(intent);

            }
        } );
        category_spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedCategory = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        } );

        selectedCategory = categories[0];
        ArrayAdapter aa = new ArrayAdapter( this, android.R.layout.simple_spinner_item, categories );
        aa.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        //Setting the ArrayAdapter data on the Spinner
        category_spinner.setAdapter( aa );
    }

    private void getData() {
        if (product_name.getText().toString().isEmpty() || product_name.getText().toString().equals( "" )) {
            product_name.setError( "enter product Name" );
        }else {
            saveData();
        }
    }

    private void saveData() {
        List<ProductModel> productModels = new ArrayList<>();
        ProductModel userModel = new ProductModel();
        userModel.setProduct_name( product_name.getText().toString() );
        userModel.setSale_price( CommonUtils.toBigDecimal( amount.getText().toString() ) );
        userModel.setDescription( details.getText().toString() );
        userModel.setProduct_category( selectedCategory );
        productModels.add( userModel );
        products.insertProducts( productModels );
        Toast.makeText( this, "Product "+product_name.getText()+" Added Succes", Toast.LENGTH_SHORT).show();
        clear();
    }

    private void clear() {
        product_name.getText().clear();
        amount.getText().clear();
        details.getText().clear();
        code.getText().clear();
        category_spinner.setSelection( 0 );


    }
}
