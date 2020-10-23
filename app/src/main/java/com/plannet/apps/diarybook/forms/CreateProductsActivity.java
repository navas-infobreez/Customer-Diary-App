package com.plannet.apps.diarybook.forms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.material.textfield.TextInputLayout;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.SyncManager.DiaryBookJsonObjectRequest;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.activity.CustomerDiaryActivity;
import com.plannet.apps.diarybook.databases.ProductCategory;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateProductsActivity extends AppCompatActivity {
    Button submit,addCategory,addUom;
    EditText product_name, code, amount, details;
    Spinner category_spinner;
    String[] categories =  {"Tiles", "PVC Pipe & Fittings", "Sanitaryware"};;
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
        addCategory=(Button)findViewById(R.id.add_category);
        addUom=(Button)findViewById(R.id.add_uom);
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
//                        Intent intent = new Intent( CreateProductsActivity.this, CustomerDiaryActivity.class );
//                        startActivity(intent);

            }
        } );
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCategoryDialogue();
            }
        });

        addUom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewUomDialogue();
            }
        });




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


    private void addNewCategoryDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. add_category_activity, null);
        builder.setView(dialogView);
        final EditText categoryName=(EditText)dialogView.findViewById(R.id.category_name);
        final EditText description=(EditText)dialogView.findViewById(R.id.description);
        final EditText searchKey=(EditText)dialogView.findViewById(R.id.searchkey);
        final CheckBox isActive=(CheckBox)dialogView.findViewById(R.id.isActive);
        Button submit=(Button)dialogView.findViewById(R.id.add_button);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductCategoryModel productCategoryModel=new ProductCategoryModel();
                if (categoryName.getText().toString().isEmpty()||categoryName.getText().toString().equals("")){
                    categoryName.setError("please enter a Category name");
                }else if (description.getText().toString().equals("")||description.getText().toString().isEmpty()){
                    description.setError("please enter a description");
                }else {
                    productCategoryModel.setCategoryName(categoryName.getText().toString());
                    productCategoryModel.setDescription(description.getText().toString());
                    productCategoryModel.setActive(isActive.isChecked());
                    productCategoryModel.setSearchKey(searchKey.getText().toString());
                    sycCategory(productCategoryModel);
                }

            }
        });

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

    private void addNewUomDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("");
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. add_category_activity, null);
        builder.setView(dialogView);
        final TextView header=(TextView)dialogView.findViewById(R.id.header);
        header.setText( "ADD NEW UOM" );
        final EditText categoryName=(EditText)dialogView.findViewById(R.id.category_name);
        final EditText description=(EditText)dialogView.findViewById(R.id.description);
        final EditText searchKey=(EditText)dialogView.findViewById(R.id.searchkey);
        final CheckBox isActive=(CheckBox)dialogView.findViewById(R.id.isActive);
        Button submit=(Button)dialogView.findViewById(R.id.add_button);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UomModel uom=new UomModel();
                if (categoryName.getText().toString().isEmpty()||categoryName.getText().toString().equals("")){
                    categoryName.setError("please enter a Uom name");
                }else if (description.getText().toString().equals("")||description.getText().toString().isEmpty()){
                    description.setError("please enter a description");
                }else {
                    uom.setName(categoryName.getText().toString());
                    uom.setDescription(description.getText().toString());
                    uom.setActive(isActive.isChecked());
                    uom.setSearchKey(searchKey.getText().toString());
                    sycUom(uom);
                }

            }
        });

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


    private void sycCategory(ProductCategoryModel productCategoryModel) {
        final String url = "https://planet-customerdiary.herokuapp.com/product/createorupdateproductcategory";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, formatter.CategoryJson(productCategoryModel),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d( "error Response", error.toString() );
            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitRoll" );
    }


    private void sycUom(UomModel uomModel) {
        final String url = " https://planet-customerdiary.herokuapp.com/uom/createorupdateuom";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, formatter.UomJson(uomModel),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d( "error Response", error.toString() );
            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitRoll" );
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
        ProductModel productModel = new ProductModel();
        productModel.setProduct_name( product_name.getText().toString() );
        productModel.setSale_price( CommonUtils.toBigDecimal( amount.getText().toString() ) );
        productModel.setDescription( details.getText().toString() );
        productModel.setProduct_category( selectedCategory );
        productModels.add( productModel );
        products.insertProducts( productModels );
        Toast.makeText( this, "Product "+product_name.getText()+" Added Succes", Toast.LENGTH_SHORT).show();
        sycProducts( productModel );
        clear();
    }

    private void clear() {
        product_name.getText().clear();
        amount.getText().clear();
        details.getText().clear();
        code.getText().clear();
        category_spinner.setSelection( 0 );


    }


    private void sycProducts(ProductModel productModel) {
        final String url = "https://planet-customerdiary.herokuapp.com/product/createorupdateproduct";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, formatter.ProductJson(productModel),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d( "error Response", error.toString() );
            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitRoll" );
    }

}
