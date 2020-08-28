package com.plannet.apps.diarybook.forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.plannet.apps.diarybook.R;
import com.plannet.apps.diarybook.databases.Customer;
import com.plannet.apps.diarybook.databases.CustomerDiaryDao;
import com.plannet.apps.diarybook.models.CustomerDiaryModel;
import com.plannet.apps.diarybook.models.CustomerModel;
import com.plannet.apps.diarybook.utils.CommonUtils;

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
            address.setText( customerModel.getAddress1() );
            phone.setText( customerModel.getPhone_no() );
            email.setText( customerModel.getEmail() );
            place.setText( customerModel.getCity() );
            noOfperson.setVisibility( View.GONE );
            qtyRequierd.setVisibility( View.GONE );

        }


    }

    private void getData() {
        if (customerId==0) {//for check is not edit
            List<CustomerModel> customerModelList = new ArrayList<>();

            CustomerModel customerModel = new CustomerModel();
            customerModel.setCustomerName( name.getText().toString() );
            customerModel.setAddress1( address.getText().toString() );
            customerModel.setPhone_no( phone.getText().toString() );
            customerModel.setEmail( email.getText().toString() );
            customerModel.setCity( place.getText().toString() );
            customerModelList.add( customerModel );
            customerDb.insertCustomers( customerModelList );
        }
         insertDiary();
        cearData();

    }

    private void insertDiary() {
        List<CustomerDiaryModel>customerDiaryModels=new ArrayList<>();
        CustomerDiaryModel customerDiaryModel=new CustomerDiaryModel();
        customerDiaryModel.setCustomerName(name.getText().toString());
        customerDiaryModel.setCustomerAddress(address.getText().toString());
        customerDiaryModel.setCustomerPhone(phone.getText().toString());
        customerDiaryModels.add(customerDiaryModel);
        customerDiaryModel.setInvoiced(isInvoiced);
        customerDiaryModel.setQuotation(isQuotation);
        customerDiaryModel.setVisit(isVisit);
        customerDiaryModel.setDate(CommonUtils.getCurrentDateAndTime());
        customerDiaryModel.setStatus(PENDING);
        customerDiaryDao.insertCustomerDiary(customerDiaryModels);
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