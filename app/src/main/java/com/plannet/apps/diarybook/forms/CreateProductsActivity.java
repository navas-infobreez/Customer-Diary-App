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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.Preference;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.SyncManager.DiaryBookJsonObjectRequest;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.activity.CustomerDiaryActivity;
import com.plannet.apps.diarybook.databases.ProductCategory;
import com.plannet.apps.diarybook.databases.ProductPrice;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.databases.Uom;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.ProductPriceDto;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateProductsActivity extends AppCompatActivity {
    Button submit,addCategory,addUom;
    EditText product_name, code, purchase_amount, details,sales_amount,discount_amount;
    Spinner category_spinner,uom_spinner;
    Products products;
    ProductPrice productPriceDb;
    ProductCategoryModel  selectedCategory;
    ProductCategory productCategoryDb;
    UomModel selectedUom;
    Uom uomDb;
    List<ProductCategoryModel>productCategoryModels=new ArrayList<>();
    List<UomModel>uomModelList=new ArrayList<>();
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_create_products );

        initDb();
        initUi();

    }

    private void initDb() {
        products = new Products( getApplicationContext() );
        productCategoryDb=new ProductCategory(this);
        uomDb = new Uom(this);
        productPriceDb = new ProductPrice(this);
    }
    private void setCategorySpinner() {
        productCategoryModels=productCategoryDb.getAll();
        List<String>category=new ArrayList<>();
        for (ProductCategoryModel temp:productCategoryModels){
            category.add(temp.getCategoryName()!=null?temp.getCategoryName():"");
        }
        ArrayAdapter aa = new ArrayAdapter( this, android.R.layout.simple_spinner_item, category );
        aa.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        category_spinner.setAdapter( aa );
    }

    private void setUomSpinner() {
        List<String>uoms=new ArrayList<>();
        for (UomModel temp:uomModelList){
            uoms.add(temp.getName()!=null?temp.getName():"");
        }
        ArrayAdapter aa = new ArrayAdapter( this, android.R.layout.simple_spinner_item, uoms );
        aa.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        uom_spinner.setAdapter( aa );
    }
    private void initUi() {
        product_name = (EditText) findViewById( R.id.name );
        code = (EditText) findViewById( R.id.product_code );
        purchase_amount = (EditText) findViewById( R.id.purchase_rate );
        sales_amount = (EditText) findViewById( R.id.sales_rate );
        discount_amount = (EditText) findViewById( R.id.discount_rate );
        details = (EditText) findViewById( R.id.details );
        category_spinner = (Spinner) findViewById( R.id.category_spinner );
        uom_spinner = (Spinner) findViewById( R.id.uom_spinner );
        submit = (Button) findViewById( R.id.add_button );
        addCategory=(Button)findViewById(R.id.add_category);
        addUom=(Button)findViewById(R.id.add_uom);

        uomModelList=uomDb.getAll();
        setCategorySpinner();
        setUomSpinner();
        submit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();


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
                selectedCategory = productCategoryModels.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        } );

        uom_spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                selectedUom = uomModelList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        } );


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
                    productCategoryModel.setProductCategoryId(Preference.NextCategoryId(CreateProductsActivity.this));
                    productCategoryModel.setSearchKey(searchKey.getText().toString());
                    Toast.makeText( getApplicationContext(), "Category "+categoryName.getText()+" Added Succes", Toast.LENGTH_SHORT).show();
                    categoryName.getText().clear();
                    description.getText().clear();
                    searchKey.getText().clear();
                    sycCategory(productCategoryModel);
                }

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productCategoryModels=productCategoryDb.getAll();
                setCategorySpinner();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productCategoryModels=productCategoryDb.getAll();
                setCategorySpinner();
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {

        super.onResume();
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
        getAllProductsCategory();
        getallUom();


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
                    uom.setUomId(Preference.NextUomId(CreateProductsActivity.this));
                    uom.setSearchKey(searchKey.getText().toString());
                    Toast.makeText( getApplicationContext(), "Uom "+categoryName.getText()+" Added Succes", Toast.LENGTH_SHORT).show();
                    categoryName.getText().clear();
                    description.getText().clear();
                    searchKey.getText().clear();
                    sycUom(uom);
                }

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uomModelList=uomDb.getAll();
                setUomSpinner();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uomModelList=uomDb.getAll();
                setUomSpinner();
                dialog.cancel();
            }
        });

        builder.show();
    }
    public void getAllProductsCategory() {
        final JsonFormater formatter = new JsonFormater();
        final String url = "https://planet-customerdiary.herokuapp.com/product/getallproductcategory";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            JSONArray jsonArrayChanged = response.getJSONArray("result");
                            List<ProductCategoryModel> productCategoryModels=new ArrayList<>();
                            for(int i=0;i<jsonArrayChanged.length();i++){
                                String str = jsonArrayChanged.getString(i);
                                Gson gson = new Gson();
                                ProductCategoryModel productCategoryModel=gson.fromJson(str, ProductCategoryModel.class);
                                productCategoryModels.add(productCategoryModel);
                            }
                            productCategoryDb.deleteAll();
                            productCategoryDb.insertProductCategory(productCategoryModels);
                            List<ProductCategoryModel>test=productCategoryDb.getAll();
                            setCategorySpinner();
                            Log.d( "Response", test.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );

            }
        } );

        AppController.getInstance().submitServerRequest( req, "getProductCategory" );
    }
    public void getallUom() {
        final String url = " https://planet-customerdiary.herokuapp.com/uom/getalluom";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v( "Response:%n %s", response.toString( 4 ) );
                            Log.d( "Response", response.toString() );
                            JSONArray jsonArrayChanged = response.getJSONArray("result");
                            List<UomModel> uomModels=new ArrayList<>();
                            for(int i=0;i<jsonArrayChanged.length();i++){
                                String str = jsonArrayChanged.getString(i);
                                Gson gson = new Gson();
                                UomModel UomModel=gson.fromJson(str,UomModel.class);
                                uomModels.add(UomModel);
                            }
                            uomDb.deleteAll();
                            uomDb.insertUom(uomModels);
                            List<UomModel>test=uomDb.getAll();
                            Log.d( "Response", test.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "Response", error.toString() );

            }
        } );

        AppController.getInstance().submitServerRequest( req, "getUom" );
    }
    private void sycCategory(ProductCategoryModel productCategoryModel) {
        final String url = "https://planet-customerdiary.herokuapp.com/product/createorupdateproductcategory";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, formatter.CategoryJson(productCategoryModel),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getAllProductsCategory();
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
                            getallUom();

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
        }else if (sales_amount.getText().toString().isEmpty() || sales_amount.getText().toString().equals( "" )) {
            sales_amount.setError( "enter sales rate" );
        } else {
            saveData();
        }
    }

    private void saveData() {

        sweetAlertDialog=new SweetAlertDialog( CreateProductsActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog .setTitleText("Please wait");
        sweetAlertDialog.setContentText( "creating product" );
        sweetAlertDialog .show();

        List<ProductPriceDto> productPriceDtoList = new ArrayList<>();
        ProductModel productModel = new ProductModel();

        int producId=Preference.NextProductId(this);
        ProductPriceDto productPriceDto = new ProductPriceDto();
        productPriceDto.setProductPriceId(Preference.NextProductPriceId(this));
        productPriceDto.setProductId(producId);
        productPriceDto.setPurchasePrice(CommonUtils.toBigDecimal( purchase_amount.getText().toString() ));
        productPriceDto.setSalesPrice(CommonUtils.toBigDecimal( sales_amount.getText().toString() ));
        productPriceDto.setDiscntSalesPrice(CommonUtils.toBigDecimal( discount_amount.getText().toString() ));
        productPriceDto.setUomId(selectedUom.getUomId());
        productPriceDtoList.add(productPriceDto);

        productModel.setProduct_id(producId);
        productModel.setProduct_name( product_name.getText().toString() );
        productModel.setCost_price( CommonUtils.toBigDecimal( purchase_amount.getText().toString() ) );
        productModel.setSale_price( CommonUtils.toBigDecimal( sales_amount.getText().toString() ) );
        productModel.setDescription( details.getText().toString() );
        productModel.setUomId(selectedUom.getUomId());
        productModel.setUom(selectedUom.getName());
        productModel.setProduct_category( selectedCategory.getCategoryName());
        productModel.setProductCategoryId(selectedCategory.getProductCategoryId());
        productModel.setProductPriceDTOList(productPriceDtoList);

        sycProducts( productModel );
    }

    private void clear() {
        product_name.getText().clear();
        purchase_amount.getText().clear();
        sales_amount.getText().clear();
        discount_amount.getText().clear();
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
                            List<ProductModel> productModels = new ArrayList<>();
                            Gson gson = new Gson();
                            JSONObject js =response.getJSONObject("result");
                            String value=js.toString();
                            ProductModel productModel1=gson.fromJson(value,ProductModel.class);
                            productModels.add( productModel1 );
                            products.insertProducts( productModels );
                            sweetAlertDialog.dismiss();
                            new SweetAlertDialog(CreateProductsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success")
                                    .setContentText("product added successfully!")
                                    .show();
                            clear();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sweetAlertDialog.dismiss();
                Log.d( "error Response", error.toString() );
                new SweetAlertDialog(CreateProductsActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error..")
                        .setContentText("product creation failed")
                        .show();
            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitRoll" );
    }

}
