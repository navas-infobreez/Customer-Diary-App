package com.plannet.apps.diarybook.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.SyncManager.DiaryBookJsonObjectRequest;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.adapters.CustomerDiaryAdapter;
import com.plannet.apps.diarybook.adapters.DiaryLineAdapter;
import com.plannet.apps.diarybook.adapters.ProductListAdapter;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.databases.CustomerDiaryLinesDao;
import com.plannet.apps.diarybook.databases.ProductCategory;
import com.plannet.apps.diarybook.databases.ProductPrice;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.databases.Uom;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.forms.ReceptionForm;
import com.plannet.apps.diarybook.forms.UomModel;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.models.ProductCategoryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.ProductPriceDto;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.Callback;
import com.plannet.apps.diarybook.utils.CommonUtils;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.plannet.apps.diarybook.DatabaseHandler.APPROVED;
import static com.plannet.apps.diarybook.DatabaseHandler.APPROVERETURN;
import static com.plannet.apps.diarybook.DatabaseHandler.COMPLETED;

public class CustomerDiaryActivity extends AppCompatActivity {
    Spinner category;
    RadioButton visit,invoiced,quotation;
    boolean isVisit,isInvoiced,isQuotation;
    Button save,products,rejectButton;
    EditText details,quotationNo,invoiceNo;
    RecyclerView productDetails;
    TextView customerName,customerAddress,customerPhone,salesMan;
    ProductCategoryModel selectedCategory=new ProductCategoryModel();
    Products productsDb;
    Uom uomDb;
    ProductPrice productPriceDb;
    int diaryId;
    RadioGroup radioGroup;
    CustomerDiaryModel selectedCustomerDiary=new CustomerDiaryModel();
    CustomerDiaryModel syncCustomerDiary=new CustomerDiaryModel();
    CustomerDiaryDao customerDiaryDao;
    CustomerDiaryLinesDao customerDiaryLinesDao;
    TextView grandTotal;
    boolean isEdit,isManager;
    User userDb;
    BigDecimal grant_Total;
    ProductPriceDto selectedPrice=new ProductPriceDto();
    UserModel userModel=new UserModel();
    private IntentIntegrator qrScan;
    ProductCategory productCategoryDb;
    UomModel selectedUomModel=new UomModel();

    List<ProductCategoryModel>productCategoryModelList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custromer_diary);
        diaryId= getIntent().getExtras().getInt("diaryId");
        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Manager" )||
                AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Admin" )){
            isEdit=false;
            isManager=true;
        }else {
            isManager=false;
            isEdit=true;
        }
        initDb();
        initUi();
        initView();
        getAllProducts();

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = productCategoryModelList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

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

    private void setCategorySpinner() {
        List<String>categoryList=new ArrayList<>();
        for (ProductCategoryModel temp:productCategoryModelList){
            categoryList.add(temp.getCategoryName()!=null?temp.getCategoryName():"");
        }
        ArrayAdapter aa = new ArrayAdapter( this, android.R.layout.simple_spinner_item, categoryList );
        aa.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        category.setAdapter( aa );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    ProductModel productModel=new ProductModel();
                    String id=result.getContents();
                    productModel=productsDb.selectProductById(CommonUtils.toInt(id));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    if (productModel!=null)
                        detailsDialogue(productModel,false,0);
//                    Toast.makeText(this, ""+productModel, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    ProductModel productModel=new ProductModel();
                    String id=result.getContents();
                    productModel=productsDb.selectProductById(CommonUtils.toInt(id));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    if (productModel!=null)
                        detailsDialogue(productModel,false,0);
//                    Toast.makeText(this, ""+productModel, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.barcode_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.qrcCde) {
            qrScan = new IntentIntegrator(this);
            qrScan.setCaptureActivity(OriantationActivity.class);
            qrScan.setPrompt("Scan QR Code");
            qrScan.setOrientationLocked(false);
            qrScan.initiateScan();

        }
        return super.onOptionsItemSelected( item );
    }

    public void getAllProducts() {
        final String url = "https://planet-customerdiary.herokuapp.com/product/getallproduct";
        JsonObjectRequest req = new DiaryBookJsonObjectRequest( this, url, null,
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
                Log.d( "Response", error.toString() );

            }
        } );

        AppController.getInstance().submitServerRequest( req, "submitShipmet" );
    }

    private void initView() {
        if (selectedCustomerDiary.isInvoiced()){
            visit.toggle();
            isInvoiced=true;
            isQuotation=false;
            isVisit=false;
        }else if (selectedCustomerDiary.isQuotation()){
            quotation.toggle();
            isInvoiced=false;
            isQuotation=true;
            isVisit=false;
        }else if (selectedCustomerDiary.isVisit()){
            visit.toggle();
            isInvoiced=false;
            isQuotation=false;
            isVisit=true;
        }
        if (selectedCustomerDiary.getDescripion()!=null)
            details.setText(selectedCustomerDiary.getDescripion());
        customerName.setText(selectedCustomerDiary.getCustomerModel().getCustomerName());
        customerPhone.setText("Phone : "+selectedCustomerDiary.getCustomerModel().getCustomerContact().getContactNo());
        customerAddress.setText("Place  : "+selectedCustomerDiary.getCustomerModel().getCustomerContact().getCity());
        if (selectedCustomerDiary.getInvoice_no()!=null)
            invoiceNo.setText(selectedCustomerDiary.getInvoice_no());
        if (selectedCustomerDiary.getQuotationNo()!=null)
            quotationNo.setText(selectedCustomerDiary.getQuotationNo());
        grant_Total=customerDiaryLinesDao.getSumOfLineTotal(diaryId);
        grandTotal.setText(grant_Total!=null?grant_Total.toPlainString():"0.00");
        if (userModel!=null)
            salesMan.setText(userModel.getName());



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
        quotationNo=(EditText)findViewById(R.id.txt_quotation_no);
        invoiceNo=(EditText)findViewById(R.id.txt_invoice_no);
        quotation=(RadioButton)findViewById(R.id.quotation);
        visit=(RadioButton)findViewById(R.id.visit);
        invoiced=(RadioButton)findViewById(R.id.invoiced);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        grandTotal=(TextView)findViewById(R.id.total);
        rejectButton=(Button)findViewById(R.id.reject);
        salesMan=(TextView)findViewById(R.id.salesman);
        productCategoryModelList=productCategoryDb.getAll();
        setCategorySpinner();
        selectedCustomerDiary=customerDiaryDao.getAll(diaryId);
        userModel=userDb.getUser(selectedCustomerDiary.getSalesmanId());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.visit) {
                    isInvoiced=false;
                    isQuotation=false;
                    isVisit=true;
                }else if (i==R.id.quotation){
                    quotation.toggle();
                    isInvoiced=false;
                    isQuotation=true;
                    isVisit=false;
                }else if (i==R.id.invoiced){
                    isInvoiced=true;
                    isQuotation=false;
                    isVisit=false;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isManager){
                    customerDiaryDao.updateDiaryStatus(diaryId,APPROVED);
                }else {
                    customerDiaryDao.updateDiary(diaryId, COMPLETED, invoiceNo.getText().toString(), quotationNo.getText().toString(), details.getText().toString(),
                            isVisit, isInvoiced, isQuotation, grant_Total != null ? grant_Total.toPlainString() : "00.00");
                }
                syncCustomerDiary=customerDiaryDao.getAll(diaryId);
                sycDairy(syncCustomerDiary);
                Intent intent2 = new Intent(CustomerDiaryActivity.this, MainActivity.class );
                startActivity(intent2);
                finish();
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerDiaryDao.updateDiaryStatus(diaryId,APPROVERETURN);
                syncCustomerDiary=customerDiaryDao.getAll(diaryId);
                sycDairy(syncCustomerDiary);
                Intent intent2 = new Intent(CustomerDiaryActivity.this, MainActivity.class );
                startActivity(intent2);
                finish();
            }
        });

        if (!selectedCustomerDiary.getStatus().equals(COMPLETED)&&isManager){
            rejectButton.setEnabled(false);
            save.setEnabled(false);
            save.setBackgroundResource(R.drawable.picked_button);
            rejectButton.setBackgroundResource(R.drawable.picked_button);
        }else {
            rejectButton.setEnabled(true);
            save.setEnabled(true);
            save.setBackgroundResource(R.drawable.login_button_bk);
            rejectButton.setBackgroundResource(R.drawable.reject_button);
        }

        if (isManager){
            rejectButton.setVisibility(View.VISIBLE);
            save.setText("APPROVE");
        }else {
            rejectButton.setVisibility(View.INVISIBLE);
            save.setText("COMPLETE");
        }

        if (isEdit){
            category.setEnabled(true);
            products.setEnabled(true);
            invoiceNo.setEnabled(true);
            quotationNo.setEnabled(true);
            radioGroup.setEnabled(true);
            invoiced.setEnabled(true);
            quotation.setEnabled(true);
            visit.setEnabled(true);
        }else {
            category.setEnabled(false);
            products.setEnabled(false);
            invoiceNo.setEnabled(false);
            quotationNo.setEnabled(false);
            radioGroup.setEnabled(false);
            invoiced.setEnabled(false);
            quotation.setEnabled(false);
            visit.setEnabled(false);
        }


    }

    private void initDb() {
        productsDb = new Products(getApplicationContext());
        customerDiaryLinesDao = new CustomerDiaryLinesDao(getApplicationContext());
        customerDiaryDao = new CustomerDiaryDao(getApplicationContext());
        userDb=new User(getApplicationContext());
        uomDb = new Uom(this);
        productPriceDb = new ProductPrice(this);
        productCategoryDb = new ProductCategory(this);

    }


    private void dialogue(){
        final List<ProductModel>productModels=productsDb.selectAllByCategory(selectedCategory.getProductCategoryId());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Product");
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout. product_detais_list, null);
        builder.setView(dialogView);
        EditText search=(EditText) dialogView.findViewById(R.id.search);
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
                detailsDialogue(productModel,false,0);
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

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<ProductModel> filteredList;
                if (productModels == null || productModels.isEmpty()) {
                    return;
                }

                if (s.toString() == null || s.toString().isEmpty()) {
                    filteredList = productModels;
                } else {
                    List<ProductModel> temp = new ArrayList<>();
                    for (ProductModel productModel : productModels) {
                        if (productModel.getProduct_name() != null && productModel.getProduct_name().toLowerCase().contains( s.toString().toLowerCase() ) ||
                                productModel.getSearchKey() != null && productModel.getSearchKey().toLowerCase().contains( s.toString().toLowerCase() )) {
                            temp.add( productModel );
                        }
                    }
                    filteredList = temp;
                }


                ProductListAdapter productListAdapter = new ProductListAdapter(filteredList, new OnCompleteCallBack() {
                    @Override
                    public void onCompleteCallBack(Object data) {
                        ProductModel productModel=new ProductModel();
                        if (data instanceof ProductModel)
                            productModel= (ProductModel) data;
                        detailsDialogue(productModel,false,0);
                    }
                });
                recyclerView.setAdapter(productListAdapter);
                productListAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        builder.show();
    }

    private void detailsDialogue(final ProductModel productsModel, final boolean isEditOption, final int lineId){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout. product_detais_data, null);
        builder.setView(dialogView);

        CustomerDiaryLineModel lineModel=customerDiaryLinesDao.getLine(lineId);
        final List<ProductPriceDto>productPriceList=productPriceDb.getAllById(productsModel.getProduct_id());

        final EditText qty,details;
        final TextView name,amount,uom;
        Spinner price_spinner;
        Button save;
        qty=(EditText)dialogView.findViewById(R.id.sqrft);
        details=(EditText)dialogView.findViewById(R.id.details);
        uom=(TextView) dialogView.findViewById(R.id.uom);
        name=(TextView) dialogView.findViewById(R.id.name);
        amount=(TextView) dialogView.findViewById(R.id.amount);
        save=(Button) dialogView.findViewById(R.id.save);
        price_spinner=(Spinner)dialogView.findViewById(R.id.price_spinner);

        final List<String>priceList=new ArrayList<>();
        for (ProductPriceDto temp:productPriceList){
            priceList.add(String.valueOf(temp.getSalesPrice()));
        }
        ArrayAdapter aa = new ArrayAdapter( CustomerDiaryActivity.this, android.R.layout.simple_spinner_item, priceList );
        aa.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        price_spinner.setAdapter( aa );

        if (isEditOption)
            qty.setText(String.valueOf(lineModel.getQty()));

        price_spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPrice = productPriceList.get(position);
                selectedUomModel=uomDb.getUom(selectedPrice.getUomId());
                uom.setText("uom: "+selectedUomModel.getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        } );
        save.setVisibility(View.GONE);

        name.setText(productsModel.getProduct_name());
//        amount.setText("Price : "+String.valueOf(selectedPrice.getSalesPrice()));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (qty.getText().toString().isEmpty()||qty.getText().toString().equals("")){
                    qty.setError("Enter a Quantity");
                }else {
                    CustomerDiaryLineModel customerDiaryLineModel = new CustomerDiaryLineModel();
                    customerDiaryLineModel.setProduct_name(productsModel.getProduct_name());
                    String qtyValue = qty.getText().toString();
                    if (isEditOption){
                        customerDiaryLineModel.setId(lineId);
                    }
                    customerDiaryLineModel.setQty(CommonUtils.toInt(qtyValue));
                    customerDiaryLineModel.setDetails(details.getText().toString());
                    customerDiaryLineModel.setProduct_id(productsModel.getProduct_id());
                    customerDiaryLineModel.setHeaderId(diaryId);
                    customerDiaryLineModel.setCategory(productsModel.getProduct_category());
                    customerDiaryLineModel.setUomId(selectedUomModel.getUomId());
                    customerDiaryLineModel.setCategoryId(selectedCategory.getProductCategoryId());
                    customerDiaryLineModel.setPrice(selectedPrice.getSalesPrice().multiply(CommonUtils.toBigDecimal(qtyValue)));
                    saveLine(customerDiaryLineModel,isEditOption);
                    qty.getText().clear();
                    details.getText().clear();
                    lineRefresh();
                    initView();
                }

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void sycDairy(CustomerDiaryModel customerDiaryModel) {
        final String url = " https://planet-customerdiary.herokuapp.com/customerdiary/createorupdatecustomerdiary";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, formatter.customerDiaryJson(customerDiaryModel),
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

        AppController.getInstance().submitServerRequest( req, "sycDairy" );
    }

    private void lineRefresh() {
        final List<CustomerDiaryLineModel>customerDiaryLineModelList=customerDiaryLinesDao.getAll(diaryId);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        productDetails.setLayoutManager(mLayoutManager);
        productDetails.setItemAnimator(new DefaultItemAnimator());
        productDetails.addItemDecoration(new ItemDecorator(getApplicationContext()));
        DiaryLineAdapter diaryLineAdapter = new DiaryLineAdapter(customerDiaryLineModelList, new Callback() {
            @Override
            public void onItemClick(Object object) {
                if (object instanceof CustomerDiaryLineModel){
                    CustomerDiaryLineModel cDiary= (CustomerDiaryLineModel) object;
                    ProductModel productModel=productsDb.selectProductById(cDiary.getProduct_id());
                    detailsDialogue(productModel,true,cDiary.getId());
                }
            }

            @Override
            public void onItemLongClick(Object object) {
                if (object instanceof CustomerDiaryLineModel) {
                    final CustomerDiaryLineModel cDiary = (CustomerDiaryLineModel) object;

                new SweetAlertDialog(CustomerDiaryActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to delete this line!")
                        .setConfirmText("Yes,delete it!")
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                               customerDiaryLinesDao.deleteLine(cDiary.getId());
                                sDialog.dismissWithAnimation();
                                lineRefresh();
                                grant_Total=customerDiaryLinesDao.getSumOfLineTotal(diaryId);
                                grandTotal.setText(grant_Total!=null?grant_Total.toPlainString():"0.00"  );
                            }
                        })
                        .show();
                }

            }
        });
        productDetails.setAdapter(diaryLineAdapter);
        diaryLineAdapter.notifyDataSetChanged();
    }

    private void saveLine(CustomerDiaryLineModel customerDiaryLineModel,boolean isEditDiary) {
        List<CustomerDiaryLineModel>customerDiaryLineModelList=new ArrayList<>();
        customerDiaryLineModelList.add(customerDiaryLineModel);
        if (isEditDiary){
            customerDiaryLinesDao.updateDiary(customerDiaryLineModel);
        }else {
            customerDiaryLinesDao.insertCustomerDiaryLines(customerDiaryLineModelList);
        }
        lineRefresh();
        grant_Total=customerDiaryLinesDao.getSumOfLineTotal(diaryId);
        grandTotal.setText(grant_Total!=null?grant_Total.toPlainString():"0.00"  );

    }
}