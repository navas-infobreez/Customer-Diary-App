package com.plannet.apps.diarybook.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.MainActivity;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.adapters.CustomerDiaryAdapter;
import com.plannet.apps.diarybook.adapters.DiaryLineAdapter;
import com.plannet.apps.diarybook.adapters.ProductListAdapter;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.databases.CustomerDiaryLinesDao;
import com.plannet.apps.diarybook.databases.Products;
import com.plannet.apps.diarybook.databases.User;
import com.plannet.apps.diarybook.forms.ReceptionForm;
import com.plannet.apps.diarybook.models.CustomerDiaryLineModel;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.ProductModel;
import com.plannet.apps.diarybook.models.UserModel;
import com.plannet.apps.diarybook.utils.CommonUtils;
import com.plannet.apps.diarybook.utils.OnCompleteCallBack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.plannet.apps.diarybook.DatabaseHandler.APPROVED;
import static com.plannet.apps.diarybook.DatabaseHandler.APPROVERETURN;
import static com.plannet.apps.diarybook.DatabaseHandler.COMPLETED;

public class CustomerDiaryActivity extends AppCompatActivity {
    String[] pCategories = {"Tiles", "PVC Pipe & Fittings", "Sanitaryware"};
    Spinner category;
    RadioButton visit,invoiced,quotation;
    boolean isVisit,isInvoiced,isQuotation;
    Button save,products,rejectButton;
    EditText details,quotationNo,invoiceNo;
    RecyclerView productDetails;
    TextView customerName,customerAddress,customerPhone,salesMan;
    String selectedCategory;
    Products productsDb;
    int diaryId;
    RadioGroup radioGroup;
    CustomerDiaryModel selectedCustomerDiary=new CustomerDiaryModel();
    CustomerDiaryDao customerDiaryDao;
    CustomerDiaryLinesDao customerDiaryLinesDao;
    TextView grandTotal;
    boolean isEdit,isManager;
    User userDb;
    BigDecimal grant_Total;
    UserModel userModel=new UserModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custromer_diary);
        diaryId= getIntent().getExtras().getInt("diaryId");
        if (AppController.getInstance().getLoggedUser().getRole_name().equalsIgnoreCase( "Manager" )) {
            isEdit=false;
            isManager=true;
        }else {
            isManager=false;
            isEdit=true;
        }
        initDb();
        initUi();
        initView();

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,pCategories);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        category.setAdapter(aa);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory=pCategories[position];
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
        customerName.setText(selectedCustomerDiary.getCustomerName());
        customerPhone.setText("Phone : "+selectedCustomerDiary.getCustomerPhone());
        customerAddress.setText("Place  : "+selectedCustomerDiary.getCustomerAddress());
        if (selectedCustomerDiary.getInvoice_no()!=null)
            invoiceNo.setText(selectedCustomerDiary.getInvoice_no());
        if (selectedCustomerDiary.getQuotationNo()!=null)
            quotationNo.setText(selectedCustomerDiary.getQuotationNo());
        grandTotal.setText(String.valueOf(selectedCustomerDiary.getTotalAmount()));
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
                Intent intent2 = new Intent(CustomerDiaryActivity.this, MainActivity.class );
                startActivity(intent2);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerDiaryDao.updateDiaryStatus(diaryId,APPROVERETURN);
                Intent intent2 = new Intent(CustomerDiaryActivity.this, MainActivity.class );
                startActivity(intent2);
            }
        });
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
        selectedCustomerDiary=customerDiaryDao.getAll(diaryId);
        userModel=userDb.getUser(selectedCustomerDiary.getSalesmanId());

    }

    private void initDb() {
        productsDb = new Products(getApplicationContext());
        customerDiaryLinesDao = new CustomerDiaryLinesDao(getApplicationContext());
        customerDiaryDao = new CustomerDiaryDao(getApplicationContext());
        userDb=new User(getApplicationContext());

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

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout. product_detais_data, null);
        builder.setView(dialogView);

        final EditText sqrft,details;
        TextView name,amount;
        Button save;
        sqrft=(EditText)dialogView.findViewById(R.id.sqrft);
        details=(EditText)dialogView.findViewById(R.id.details);
        name=(TextView) dialogView.findViewById(R.id.name);
        amount=(TextView) dialogView.findViewById(R.id.amount);
        save=(Button) dialogView.findViewById(R.id.save);
        save.setVisibility(View.GONE);

        name.setText(productsModel.getProduct_name());
        amount.setText("Price : "+String.valueOf(productsModel.getSale_price()));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (sqrft.getText().toString().isEmpty()||sqrft.getText().toString().equals("")){
                    sqrft.setError("Enter a Quantity");
                }else {
                    CustomerDiaryLineModel customerDiaryLineModel = new CustomerDiaryLineModel();
                    customerDiaryLineModel.setProduct_name(productsModel.getProduct_name());
                    String qty = sqrft.getText().toString();
                    customerDiaryLineModel.setQty(CommonUtils.toInt(qty));
                    customerDiaryLineModel.setDetails(details.getText().toString());
                    customerDiaryLineModel.setProduct_id(productsModel.getId());
                    customerDiaryLineModel.setHeaderId(diaryId);
                    customerDiaryLineModel.setCategory(productsModel.getProduct_category());
                    customerDiaryLineModel.setPrice(productsModel.getSale_price().multiply(CommonUtils.toBigDecimal(qty)));
                    saveLine(customerDiaryLineModel);
                    sqrft.getText().clear();
                    details.getText().clear();
                    lineRefresh();
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
        lineRefresh();
        grant_Total=customerDiaryLinesDao.getSumOfLineTotal(diaryId);
        grandTotal.setText(grant_Total!=null?grant_Total.toPlainString():"0.00"  );

    }
}