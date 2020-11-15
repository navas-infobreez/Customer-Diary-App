package com.plannet.apps.diarybook.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.plannet.apps.diarybook.AppController;
import com.plannet.apps.diarybook.Preference;
import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.SyncManager.DiaryBookJsonObjectRequest;
import com.plannet.apps.diarybook.SyncManager.JsonFormater;
import com.plannet.apps.diarybook.databases.Customer;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.models.CustomerContact;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.models.RoleModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.plannet.apps.diarybook.DatabaseHandler.PENDING;


public class ReceptionForm extends AppCompatActivity {
    EditText name, place, address, email, phone, profession, purpose, noOfperson, qtyRequierd;
    Button add;
    Customer customerDb;
    CustomerDiaryDao customerDiaryDao;
    RadioGroup radioGroup;
    RadioButton visit,invoiced,quotation;
    boolean isVisit,isInvoiced,isQuotation;
    boolean isEdit;
    int customerId=0;
    TextView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception_form);
        Intent intent = getIntent();
        customerId= intent != null && intent.hasExtra("customerId")?intent.getIntExtra("customerId", 0) : 0;
        intDb();
        initUi();

    }

    private void intDb() {
        customerDb=new Customer(getApplicationContext());
        customerDiaryDao=new CustomerDiaryDao(getApplicationContext());
    }

    private void initUi() {

        name = (EditText) findViewById(R.id.txt_name);
        place = (EditText) findViewById(R.id.txt_place);
        address = (EditText) findViewById(R.id.txt_address);
        email = (EditText) findViewById(R.id.txt_email);
        phone = (EditText) findViewById(R.id.txt_phone);
        profession = (EditText) findViewById(R.id.txt_profession);
        //purpose = (EditText) findViewById(R.id.txt_purpose);
        noOfperson = (EditText) findViewById(R.id.txt_noAccoumpaining);
        qtyRequierd = (EditText) findViewById(R.id.no_qty_requerd);
        add = (Button) findViewById(R.id.add_custoner_button);
        header=(TextView)findViewById( R.id.header ) ;
        quotation=(RadioButton)findViewById(R.id.quotation);
        visit=(RadioButton)findViewById(R.id.visit);
        invoiced=(RadioButton)findViewById(R.id.invoiced);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);

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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        if (customerId>0){
            header.setText( "CREATE DIARY" );
            add.setText( "SAVE" );
            setData();
        }
    }

    private void saveData() {
        if (name.getText().toString().isEmpty() || name.getText().toString().equals("")) {
            name.setError("please enter name");
        } else if (phone.getText().toString().isEmpty() || phone.getText().toString().equals("")) {
            phone.setError("please enter phone number");
        }else {
            getData();
        }
    }

    private void setData() {
        CustomerModel customerModel=customerDb.getCustomer(customerId);
        if (customerModel!=null) {
            name.setText( customerModel.getCustomerName() );
            address.setText( customerModel.getCustomerContact().getAddress1() );
            phone.setText( customerModel.getCustomerContact().getContactNo() );
            email.setText( customerModel.getCustomerContact().getEmail() );
            place.setText( customerModel.getCustomerContact().getCity() );
            noOfperson.setVisibility( View.GONE );
            qtyRequierd.setVisibility( View.GONE );

        }


    }

    private void getData() {
        if (customerId==0) {//for check is not edit
            List<CustomerModel> customerModelList = new ArrayList<>();
            CustomerContact customerContact=new CustomerContact();

            CustomerModel customerModel = new CustomerModel();
            customerModel.setCustomerName( name.getText().toString() );
            customerContact.setAddress1( address.getText().toString() );
            customerContact.setContactNo( phone.getText().toString() );
            customerContact.setEmail( email.getText().toString() );
            customerContact.setCity( place.getText().toString() );
            customerModel.setCustomerContact( customerContact );
            customerModelList.add( customerModel );
            customerDb.insertCustomers( customerModelList );
            syncCustomer(customerModel);
        }
         insertDiary();
        cearData();

    }

    private void syncDiary(CustomerDiaryModel cDiary) {
        final String url = " https://planet-customerdiary.herokuapp.com/customerdiary/createorupdatecustomerdiary";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, formatter.customerDiaryJson(cDiary),
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

    private void syncCustomer(CustomerModel customerModel) {
        final String url = " https://planet-customerdiary.herokuapp.com/customer/createorupdatecustomer";
        final JsonFormater formatter = new JsonFormater();
        DiaryBookJsonObjectRequest req = new DiaryBookJsonObjectRequest(this,  url, formatter.customerJson(customerModel),
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

        AppController.getInstance().submitServerRequest( req, "submitCustomer" );
    }

    private void insertDiary() {
        List<CustomerDiaryModel>customerDiaryModels=new ArrayList<>();
        CustomerDiaryModel customerDiaryModel=new CustomerDiaryModel();
        customerDiaryModel.setCustomerName(name.getText().toString());
        customerDiaryModel.setCustomerAddress(address.getText().toString());
        customerDiaryModel.setCustomerPhone(phone.getText().toString());
        customerDiaryModels.add(customerDiaryModel);
        customerDiaryModel.setInvoiced(isInvoiced);
        customerDiaryModel.setInvoice_no( "" );
        customerDiaryModel.setQuotation(isQuotation);
        customerDiaryModel.setQuotationNo( "" );
        customerDiaryModel.setDescripion( "" );
        customerDiaryModel.setTotalAmount( BigDecimal.ZERO );
        customerDiaryModel.setVisit(isVisit);
        customerDiaryModel.setDate(CommonUtils.getCurrentDateAndTime());
        customerDiaryModel.setStatus(PENDING);
        customerDiaryModel.setCustomerId( customerId );
        customerDiaryModel.setSalesmanId( Preference.getLoggedUserId() );
        //customerDiaryModel.set
        syncDiary(customerDiaryModel);
        //customerDiaryDao.insertCustomerDiary(customerDiaryModels);
    }

    private void cearData() {
        name.getText().clear();
        profession.getText().clear();
        phone.getText().clear();
        address.getText().clear();
        email.getText().clear();
        place.getText().clear();
        noOfperson.getText().clear();
        qtyRequierd.getText().clear();
        //purpose.getText().clear();
        finish();
    }
}